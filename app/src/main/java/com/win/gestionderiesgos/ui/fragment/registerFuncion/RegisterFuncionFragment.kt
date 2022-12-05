package com.win.gestionderiesgos.ui.fragment.registerFuncion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterFuncionBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.utils.ShowDialog
import java.util.*


class RegisterFuncionFragment : Fragment() {
    private lateinit var  binding:FragmentRegisterFuncionBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var mShowDialog: ShowDialog
    private var selected:String?=null
    private var nameProject:String?=null
    private var nameFusions:String?=null
    private var noExistSnapshot:Boolean?=null
    private var nameProjectFusionExited:String?=null
    private lateinit var projectListProvider: ProjectListProvider
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val viewModelProject by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterFuncionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mAuthProvider = AuthProvider()
        mShowDialog = ShowDialog(requireContext())
        projectListProvider = ProjectListProvider()
        viewModelObserver()
        validateRealTime()
        registerFuncion()
    }

    private fun viewModelObserver() {
        viewModelProject.getAllProject()
        viewModelProject.responseGetAllProject.observe(viewLifecycleOwner) {
          if (it.isNotEmpty()){
              setUpDataInSpinner(it)
          }

        }
        viewModel.getFuncions(requireActivity())
        viewModel.responsegetFuncions.observe(viewLifecycleOwner) { listFusion ->
            listFusion.forEach {
                nameFusions = it.name
                nameProjectFusionExited = it.nameProject
            }

        }
        viewModel.noExistSnapshot.observe(viewLifecycleOwner) { noExist ->
            if (noExist) {
                binding.apply {
                    layoutRegisterfuncion.helperText =
                        "No hay Funcion Registrado en el sistema por favor ingrese una"
                }
                noExistSnapshot = noExist
            }
        }

        viewModelProject.noExistProject.observe(viewLifecycleOwner) { noExistProject ->
            if (noExistProject) {
                binding.apply {
                    layoutdrop.helperText =
                        "No tienes Proyectos registrados Por favor registra primero un proyecto"
                    btnAgregar.isEnabled = false
                }
            }
        }
    }

    private fun setUpDataInSpinner(it: List<Project>?) {
        val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item,it!!)
        binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            val selecteditem = adapterView.getItemAtPosition(i)
            nameProject=selecteditem.toString()
          viewModel.getIdProjectSelected(selecteditem.toString())
            viewModel.responseIdProjectSelected.observe(viewLifecycleOwner) { idSelected ->
                selected = idSelected
            }

        }
    }

    private fun registerFuncion() {
        binding.apply {
            btnAgregar.setOnClickListener {
                when {
                    registerFuncion.text.toString().isEmpty() -> {
                        layoutRegisterfuncion.helperText =getString(R.string.erroremptyfield)
                    }
                    autoCompleteTextView.text.toString().isEmpty() -> {
                        layoutdrop.helperText=getString(R.string.erroremptyfield)
                    }
                    nameProject == nameProjectFusionExited && registerFuncion.text.toString().uppercase(Locale.ROOT) ==nameFusions-> {
                        layoutRegisterfuncion.helperText ="Ya tienes Registrado ese nombre de Fucion"
                    }
                    else -> {
                        layoutRegisterfuncion.helperText =""
                        layoutdrop.helperText =""
                        btnAgregar.isEnabled=true
                        createFuncios(registerFuncion.text.toString())
                    }
                }
            }
        }

    }

    private fun createFuncios(tvfuncions: String) {
        mShowDialog.showDialog()
        val currentTime = Calendar.getInstance().time
       val funcions =Funcions(tvfuncions.uppercase(Locale.ROOT),mAuthProvider.getId().toString(),currentTime.toString(),"0",selected!!,nameProject!!,"","","${selected}_Created")
        viewModel.registerFuncions(funcions)
        viewModel.responseFuncions.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext() ,
                    "Se registro correctamente la funcion" ,
                    Toast.LENGTH_SHORT
                ).show()
                mShowDialog.dismissDialog()
                Navigation.findNavController(requireActivity(),R.id.container_fragment).navigate(R.id.action_registerFuncionFragment_to_homeFragment)
                projectListProvider.updateStatusProject(selected!! ,"created")
            } else {
                Toast.makeText(
                    requireContext() ,
                    "No se puede registrar la funcion" ,
                    Toast.LENGTH_SHORT
                ).show()
                mShowDialog.dismissDialog()
            }
        }
    }

    private fun validateRealTime() {
        binding.registerFuncion.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding.layoutRegisterfuncion.helperText =getString(R.string.erroremptyfield)
                    }else->{
                    binding.layoutRegisterfuncion.helperText =""
                    binding.layoutRegisterfuncion.isEnabled=true
                }

                }
                if (nameProject == nameProjectFusionExited && textInput.toString().uppercase(Locale.ROOT) == nameFusions){
                    binding.apply {
                        layoutRegisterfuncion.helperText ="Ya tienes Registrado ese nombre de Fucion"
                    }
                }
            }

        }
    }

}