package com.win.gestionderiesgos.ui.fragment.registerActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterActivityBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.ListFusionViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.utils.Constants.CURRENTTIME
import com.win.gestionderiesgos.utils.ShowDialog


@Suppress("UNCHECKED_CAST")
class RegisterActivityFragment : Fragment() {
    private lateinit var binding:FragmentRegisterActivityBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var navController: NavController
    private var idKeyFusion:String?=null
    private lateinit var mShowDialog: ShowDialog
    private var selected:String?=null
    private var projectSelected:String?=null
    private var idProjectSelected:String?=null
    private lateinit var GetFusionProvider: GetFusionProvider
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val viewModelRegisterActivity by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private val viewModelProject by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private  val viewModelListFusion by lazy { ViewModelProvider(this)[ListFusionViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentRegisterActivityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = NavController(requireContext())
        viewModel.getFusionOnlyAdmin(requireActivity())
        mAuthProvider = AuthProvider()
        GetFusionProvider=GetFusionProvider()
        mShowDialog = ShowDialog(requireContext())
        viewModelObserver()
        binding.btnAgregar.setOnClickListener {
            createActivity()
        }

    }

    private fun viewModelObserver() {
        viewModelProject.getAllProject()
        viewModelProject.responseGetAllProject.observe(viewLifecycleOwner, Observer { project->
            setUpProjectInSpinner(project)
        })

        viewModelProject.noExistProject.observe(viewLifecycleOwner) { noExistProject ->
            if (noExistProject) {
                binding.apply {
                    layoutProject.helperText = "No tienes Proyectos registrados Por favor registra primero un proyecto"
                    btnAgregar.isEnabled = false
                    layoutdrop.isEnabled=false
                }
            }else{
                binding.apply {
                    btnAgregar.isEnabled = false
                    layoutdrop.isEnabled=false
                    layoutProject.helperText = ""
                }

            }
        }
        viewModel.noExistSnapshot.observe(viewLifecycleOwner) { noExist ->
            if (noExist) {
                binding.apply {
                    layoutdrop.helperText = "No hay Funcion Registrado en el sistema por favor ingrese una"
                    btnAgregar.isEnabled=false
                }
            }else{
                binding.apply {
                    layoutdrop.helperText = ""
                    btnAgregar.isEnabled=false
                }
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

    }

    private fun createActivity() {
            when {
                binding.autoCompleteTextViewProject.text.toString().isEmpty() -> {
                    binding.layoutProject.helperText =getString(R.string.erroremptyfield)
                }
                binding.autoCompleteTextView.text.toString().isEmpty() -> {
                    binding.layoutdrop.helperText =getString(R.string.erroremptyfield)
                }
                binding.registerActivity.text.toString().isEmpty() -> {
                    binding.layoutRegisteractivity.helperText =getString(R.string.erroremptyfield)
                }
                else -> {
                    mShowDialog.showDialog()
                    binding.layoutRegisteractivity.helperText =""
                    binding.layoutProject.helperText =""
                    binding.layoutdrop.helperText =""
                    val activity= idKeyFusion?.let { it1 ->
                        Actividad(selected!!,binding.registerActivity.text.toString(),mAuthProvider.getId().toString(),CURRENTTIME.toString(),"0","","",
                            it1,"","")
                    }
                    if (activity != null) {
                        viewModelRegisterActivity.createActivity(activity)
                    }
                    viewModelRegisterActivity.responseActivity.observe(viewLifecycleOwner, Observer {
                        if (it.isSuccessful){
                            mShowDialog.dismissDialog()
                            Navigation.findNavController(requireActivity(),R.id.container_fragment).navigate(R.id.action_registerActivityFragment_to_homeFragment)
                            updateIdKeyActivityInFusion(idKeyFusion!!)
                            Toast.makeText(requireContext() , "Actividad fue registrado correctamente" , Toast.LENGTH_SHORT).show()


                        }else{
                            mShowDialog.dismissDialog()
                            Toast.makeText(requireContext() , "error ${it.message()}" , Toast.LENGTH_SHORT).show()
                        }
                    })
                }
        }


    }

    private fun updateIdKeyActivityInFusion(idKeyFusion: String) {
        GetFusionProvider.updateIdKeyFusion(idKeyFusion,"created")

    }
}