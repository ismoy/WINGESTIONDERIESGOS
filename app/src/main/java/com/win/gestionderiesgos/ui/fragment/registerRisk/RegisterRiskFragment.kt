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
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterRiskBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.presentation.registerRisk.RegisterRiskViewModel
import com.win.gestionderiesgos.utils.Constants

class RegisterRiskFragment : Fragment() {
  private lateinit var binding:FragmentRegisterRiskBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var navController: NavController
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val viewModelRegisterActivity by lazy { ViewModelProvider(this)[RegisterRiskViewModel::class.java] }
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
        viewModel.getFuncions()
        mAuthProvider = AuthProvider()
        viewModel.responsegetFuncions.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                setUpDataInSpinner(it)
            }

        })
    }

    private fun setUpDataInSpinner(list: List<Funcions>?) {
        val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item,list!!)
        binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            val selected =adapterView.getItemAtPosition(i).toString()
            createRisk(selected)
        }

    }

    private fun createRisk(selected: String) {
        binding.btnAgregar.setOnClickListener {
            if (binding.registerActivity.text.toString().isEmpty()){
                binding.layoutRegisteractivity.helperText =getString(R.string.erroremptyfield)
            }else{
                binding.layoutRegisteractivity.helperText =""
                val risk= Risk(selected,binding.registerActivity.text.toString(),mAuthProvider.getId().toString(), Constants.CURRENTTIME.toString())
                viewModelRegisterActivity.registerRisk(risk)
                viewModelRegisterActivity.responseRisk.observe(viewLifecycleOwner, Observer {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext() , "Riesgo fue registrado correctamente" , Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(requireContext() , "error ${it.message()}" , Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}