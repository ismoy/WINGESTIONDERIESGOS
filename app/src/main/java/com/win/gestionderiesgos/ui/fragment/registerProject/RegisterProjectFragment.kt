package com.win.gestionderiesgos.ui.fragment.registerProject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.TokenProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterProjectBinding
import com.win.gestionderiesgos.domain.model.*
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.notification.NotificationViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.utils.Constants
import com.win.gestionderiesgos.utils.Constants.TITLENOTIFICATION
import com.win.gestionderiesgos.utils.ShowDialog
import kotlinx.coroutines.CoroutineScope
import java.util.*


class RegisterProjectFragment : Fragment() {
    private lateinit var binding: FragmentRegisterProjectBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var mTokensProvider: TokenProvider
    private var selected: String? = null
    private var idUserSelected: String? = null
    private lateinit var mShowDialog: ShowDialog
    private var day = 0
    private var month: Int = 0
    private var year: Int = 0
    private val viewModel by lazy { ViewModelProvider(this)[RegisterProjectViewModel::class.java] }
    private val viewModelFusion by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val notificationViewModel by lazy { ViewModelProvider(this)[NotificationViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterProjectBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mAuthProvider = AuthProvider()
        mTokensProvider = TokenProvider()
        mShowDialog = ShowDialog(requireContext())
        observeViewModel()
        binding.btnRegister.setOnClickListener {
            createProject()
        }

        binding.apply {
            initialDate.setOnClickListener { initialDate(initialDate) }
            endDate.setOnClickListener { endDate(endDate) }
        }

    }


    private fun initialDate(date: AutoCompleteTextView) {
        val c = Calendar.getInstance()
        day = c[Calendar.DAY_OF_MONTH]
        month = c[Calendar.MONTH]
        year = c[Calendar.YEAR]
        @SuppressLint("SetTextI18n") val datePickerDialog = DatePickerDialog(requireContext() ,
            { datePicker: DatePicker? , year: Int , moth: Int , day: Int ->
                val mess = moth + 1
                date.setText("$day/$mess/$year")
                binding.layoutinitialDate.helperText = ""
            } , year , month , day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun endDate(date: AutoCompleteTextView) {
        val c = Calendar.getInstance()
        day = c[Calendar.DAY_OF_MONTH]
        month = c[Calendar.MONTH]
        year = c[Calendar.YEAR]
        @SuppressLint("SetTextI18n") val datePickerDialog = DatePickerDialog(requireContext() ,
            { datePicker: DatePicker? , year: Int , moth: Int , day: Int ->
                val mess = moth + 1
                date.setText("$day/$mess/$year")
                binding.layoutendDate.helperText = ""
            } , year , month , day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun observeViewModel() {
        viewModel.getListUser()
        viewModel.listUsers.observe(viewLifecycleOwner , Observer {
            if (it.isNotEmpty()) {
                setUpDataInSpinner(it)
            }
        })

        viewModelFusion.getFusionOnlyAdmin(requireActivity())

    }


    private fun setUpDataInSpinner(it: List<Users>?) {
        val adapterItems = ArrayAdapter(requireContext() , R.layout.dropdowm_item , it!!)
        binding.apply {
            autoCompleteTextView.setAdapter(adapterItems)
            autoCompleteTextView.setOnItemClickListener { adapterView , view , i , l ->
                selected = adapterView.getItemAtPosition(i).toString()
                viewModel.getIdUserSelected(selected!!)
                viewModel.responseIdUser.observe(viewLifecycleOwner , Observer { userSelected ->
                    idUserSelected = userSelected
                })

            }
        }
    }

    private fun createProject() {
        val project = idUserSelected?.let {
            Project(
                binding.idProject.text.toString(),
                binding.nameProject.text.toString().uppercase(Locale.ROOT) ,
                binding.initialDate.text.toString() ,
                binding.endDate.text.toString() ,
                it ,
                mAuthProvider.getId().toString() ,
                Constants.CURRENTTIME.toString() ,
                "0",
                "","")
        }
        binding.apply {
            when {
                idProject.text!!.isEmpty()->{
                    layoutIdProject.helperText = getString(R.string.erroremptyfield)
                }
                nameProject.text!!.isEmpty() -> {
                    layoutNameProject.helperText = getString(R.string.erroremptyfield)
                }
                initialDate.text.isEmpty() -> {
                    layoutinitialDate.helperText = getString(R.string.erroremptyfield)
                }
                endDate.text.isEmpty() -> {
                    layoutendDate.helperText = getString(R.string.erroremptyfield)
                }
                autoCompleteTextView.text.isEmpty() -> {
                    layoutdrop.helperText = getString(R.string.erroremptyfield)
                }
                else -> {
                    if (project != null) {
                        createdProject(project)
                    }
                }
            }
        }

    }

    private fun sendNotificationToOtherDevice() {
        mTokensProvider.getTokenClient()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val tokenClient: String = ds.child("token").value.toString()
                            PushNotification(
                                NotificationData(
                                    TITLENOTIFICATION , "tienes un nuevo proyecto ${binding.nameProject.text.toString()}"
                                ) , tokenClient
                            ).also { push ->
                                notificationViewModel.sendNotification(push)
                                notificationViewModel.responseNotification.observe(
                                    requireActivity()
                                ) {
                                    if (it.isSuccessful) {

                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext() ,
                            "El usuario que estas asignando no tiene el token instalado por enden no se puede enviarle notification" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun createdProject(project: Project) {
        mShowDialog.showDialog()
        viewModel.createProject(project)
        viewModel.myResponse.observe(viewLifecycleOwner , Observer {
            if (it.isSuccessful) {
                sendNotificationToOtherDevice()
                Toast.makeText(
                    requireContext() ,
                    "se asigno correctamente el proyecto al usuario $selected" ,
                    Toast.LENGTH_SHORT
                ).show()
                Navigation.findNavController(requireActivity() , R.id.container_fragment).navigate(R.id.action_registerProjectFragment_to_homeFragment)
                mShowDialog.dismissDialog()
                sendDataInGoogleSheet()
            } else {
                Toast.makeText(requireContext() , "no se puede registrar" , Toast.LENGTH_SHORT)
                    .show()
                mShowDialog.dismissDialog()
            }
        })
    }

    private fun sendDataInGoogleSheet() {
        viewModel.sendDataInGoogleSheet(Constants.ACTION,binding.idProject.text.toString(),binding.nameProject.text.toString().uppercase(),binding.initialDate.text.toString(),
        binding.endDate.text.toString(),"","","","","","","","",selected!!,
        "","","","","","","","")
    }
}