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
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterActivityBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.utils.Constants.CURRENTTIME
import kotlinx.coroutines.NonDisposableHandle.parent


class RegisterActivityFragment : Fragment() {
    private lateinit var binding:FragmentRegisterActivityBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var navController: NavController
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private val viewModelRegisterActivity by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
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
            createActivity(selected)
        }

    }

    private fun createActivity(selected: String) {
        binding.btnAgregar.setOnClickListener {
            if (binding.registerActivity.text.toString().isEmpty()){
                binding.layoutRegisteractivity.helperText =getString(R.string.erroremptyfield)
            }else{
                binding.layoutRegisteractivity.helperText =""
                val activity=Actividad(selected,binding.registerActivity.text.toString(),mAuthProvider.getId().toString(),CURRENTTIME.toString(),"0",0.0,"")
                viewModelRegisterActivity.createActivity(activity)
                viewModelRegisterActivity.responseActivity.observe(viewLifecycleOwner, Observer {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext() , "Actividad fue registrado correctamente" , Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(requireContext() , "error ${it.message()}" , Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


    }
}