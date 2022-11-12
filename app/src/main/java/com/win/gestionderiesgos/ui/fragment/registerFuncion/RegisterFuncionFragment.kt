package com.win.gestionderiesgos.ui.fragment.registerFuncion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterFuncionBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.utils.ShowDialog
import java.time.LocalDate
import java.util.*

class RegisterFuncionFragment : Fragment() {
    private lateinit var  binding:FragmentRegisterFuncionBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var mShowDialog: ShowDialog
    private val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
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
        validateRealTime()
        registerFuncion()
    }

    private fun registerFuncion() {
        binding.apply {
            btnAgregar.setOnClickListener {
                if (registerFuncion.text.toString().isEmpty()){
                    layoutRegisterfuncion.helperText =getString(R.string.erroremptyfield)
                }else{
                    layoutRegisterfuncion.helperText =""
                    layoutRegisterfuncion.isEnabled=true
                    createFuncios(registerFuncion.text.toString())
                }
            }
        }

    }

    private fun createFuncios(tvfuncions: String) {
        mShowDialog.showDialog()
        val currentTime = Calendar.getInstance().time
       val funcions =Funcions(tvfuncions.uppercase(Locale.ROOT),mAuthProvider.getId().toString(),currentTime.toString(),"0")
        viewModel.registerFuncions(funcions)
        viewModel.responseFuncions.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext() ,
                    "Se registro correctamente la funcion" ,
                    Toast.LENGTH_SHORT
                ).show()
                mShowDialog.dismissDialog()
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
            }

        }
    }
}