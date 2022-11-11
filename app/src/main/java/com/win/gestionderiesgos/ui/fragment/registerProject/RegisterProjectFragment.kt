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
import com.google.android.material.textfield.TextInputEditText
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterProjectBinding
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.utils.Constants
import java.util.*


class RegisterProjectFragment : Fragment() {
 private lateinit var binding:FragmentRegisterProjectBinding
 private lateinit var mAuthProvider: AuthProvider
 private var selected:String?=null
    private var day = 0
    private  var month:Int = 0
    private  var year:Int = 0
 private  val viewModel by lazy { ViewModelProvider(this)[RegisterProjectViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterProjectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mAuthProvider = AuthProvider()
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
            } , year,month,day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()-1000
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
            } , year,month,day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()-1000
        datePickerDialog.show()
    }
    private fun observeViewModel() {
        viewModel.getListUser()
        viewModel.listUsers.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                Log.d("llegoLista",it.toString())
                setUpDataInSpinner(it)
            }
        })
    }

    private fun setUpDataInSpinner(it: List<Users>?) {
        val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item,it!!)
        binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            selected =adapterView.getItemAtPosition(i).toString()
            Log.d("seleciono",selected.toString())
        }
    }

    private fun createProject() {
        val project= selected?.let {
            Project(binding.nameProject.text.toString(),binding.initialDate.text.toString(),binding.endDate.text.toString(),
                it ,mAuthProvider.getId().toString(),Constants.CURRENTTIME.toString())
        }
        binding.apply {
                when {
                    nameProject.text!!.isEmpty() -> {
                        layoutNameProject.helperText = getString(R.string.erroremptyfield)
                    }
                    initialDate.text.isEmpty() -> {
                        layoutinitialDate.helperText=getString(R.string.erroremptyfield)
                    }
                    endDate.text.isEmpty() -> {
                        layoutendDate.helperText=getString(R.string.erroremptyfield)
                    }
                    autoCompleteTextView.text.isEmpty() -> {
                        layoutdrop.helperText =getString(R.string.erroremptyfield)
                    }
                    else -> {
                        if (project != null) {
                            createdProject(project)
                        }
                    }
                }
        }

    }

    private fun createdProject(project: Project) {
        viewModel.createProject(project)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful){
                Toast.makeText(requireContext() , "se guardo" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext() , "no se puede registrar" , Toast.LENGTH_SHORT).show()
            }
        })
    }
}