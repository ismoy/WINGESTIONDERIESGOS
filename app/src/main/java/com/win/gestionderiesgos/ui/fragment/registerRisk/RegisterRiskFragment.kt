package com.win.gestionderiesgos.ui.fragment.registerRisk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterRiskBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.ListFusionViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.presentation.registerRisk.RegisterRiskViewModel
import com.win.gestionderiesgos.utils.Constants

class RegisterRiskFragment : Fragment() {
  private lateinit var binding:FragmentRegisterRiskBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var navController: NavController
    private var selected:String?=null
    private var projectSelected:String?=null
    private var idProjectSelected:String?=null
    private var idKeyFusion:String?=null
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val viewModelRegisterActivity by lazy { ViewModelProvider(this)[RegisterRiskViewModel::class.java] }
    private  val viewModelListFusion by lazy { ViewModelProvider(this)[ListFusionViewModel::class.java] }
    private val viewModelProject by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentRegisterRiskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = NavController(requireContext())
        viewModel.getFuncions(requireActivity())
        mAuthProvider = AuthProvider()
        observeViewModel()
        binding.btnAgregar.setOnClickListener {
            createRisk()
        }

    }

    private fun observeViewModel() {
        viewModelProject.getAllProject()
        viewModelProject.responseGetAllProject.observe(viewLifecycleOwner, Observer { project->
            setUpProjectInSpinner(project)
        })

        viewModel.noExistSnapshot.observe(viewLifecycleOwner) { noExist ->
            if (noExist) {
                binding.apply {
                    layoutdrop.helperText = "No hay Funcion Registrado en el sistema por favor ingrese una"
                btnAgregar.isEnabled=false
                }
            }else{
                binding.btnAgregar.isEnabled=true
            }
        }
    }

    private fun setUpProjectInSpinner(project: List<Project>) {
        val adapterItem =ArrayAdapter(requireContext(),R.layout.dropdowm_item,project)
        binding.autoCompleteTextViewProject.setAdapter(adapterItem)
        binding.autoCompleteTextViewProject.setOnItemClickListener { adapterView, view, i, l ->
            val  projectSelect=adapterView.getItemAtPosition(i)
            projectSelected=projectSelect.toString()
            viewModel.getIdProjectSelected(projectSelected.toString())
            viewModel.responseIdProjectSelected.observe(viewLifecycleOwner) { idSelected ->
                idProjectSelected=idSelected
                idProjectSelected?.let { viewModelListFusion.getFusionsByIdProject(it) }
                viewModelListFusion.responseGetFusionsByIdProject.observe(viewLifecycleOwner, Observer {listFusion->
                    if (listFusion.isNotEmpty()){
                        setUpDataInSpinner(listFusion as List<String>)
                    }

                })
            }

        }
    }
    private fun setUpDataInSpinner(list: List<String>) {
        val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item, list)
        binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            selected =adapterView.getItemAtPosition(i).toString()
            viewModel.getIdFusionSelected(selected!!)
            viewModel.responseIdFusionSelected.observe(viewLifecycleOwner, Observer {
                idKeyFusion=it
            })
        }







       /* binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            val selected =adapterView.getItemAtPosition(i).toString()
            createRisk(selected)
        }*/

    }

    private fun createRisk(){
        binding.apply {
          if (autoCompleteTextViewProject.text.toString().isEmpty()){
              layoutProject.helperText=getString(R.string.erroremptyfield)
          }else if (autoCompleteTextView.text.toString().isEmpty()){
              layoutdrop.helperText=getString(R.string.erroremptyfield)
          }else if (binding.registerActivity.text.toString().isEmpty()){
              layoutRegisteractivity.helperText =getString(R.string.erroremptyfield)
          }else{
              layoutRegisteractivity.helperText =""
              layoutProject.helperText=""
              layoutdrop.helperText=""
              val risk= Risk(selected!!,binding.registerActivity.text.toString(),mAuthProvider.getId().toString(), Constants.CURRENTTIME.toString(),"",idKeyFusion!!,"","",idProjectSelected!!,projectSelected!!)
              viewModelRegisterActivity.registerRisk(risk)
              viewModelRegisterActivity.responseRisk.observe(viewLifecycleOwner, Observer {
                  if (it.isSuccessful){
                      Toast.makeText(requireContext() , "Riesgo fue registrado correctamente" , Toast.LENGTH_SHORT).show()
                      Navigation.findNavController(requireActivity(),R.id.container_fragment).navigate(R.id.action_registerRiskFragment_to_homeFragment)


                  }else{
                      Toast.makeText(requireContext() , "error ${it.message()}" , Toast.LENGTH_SHORT).show()
                  }
              })
          }
        }

    }
}