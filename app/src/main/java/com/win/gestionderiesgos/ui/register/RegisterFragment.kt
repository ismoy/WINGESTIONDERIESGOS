package com.win.gestionderiesgos.ui.register

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentRegisterBinding
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.presentation.register.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mAuthProvider:AuthProvider
    private  val viewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }
    private lateinit var navController: NavController
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentRegisterBinding.inflate(inflater,container,false)
        mAuthProvider = AuthProvider()
        dialog = Dialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.btnRegister.setOnClickListener {
            register()
        }
        validateRealTime()
    }

    private fun validateRealTime() {
         binding.emailRegister.apply {
           doOnTextChanged { textInput, start, before, count ->
               when{
                   textInput!!.isEmpty()->{
                       binding.layoutRegister.helperText =getString(R.string.erroremptyfield)
                       binding.btnRegister.isEnabled=false
                   }else->{
                   binding.layoutRegister.helperText =""
                   binding.btnRegister.isEnabled=true
               }

               }
           }

       }

        binding.nameComplete.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding.layoutCompleteName.helperText =getString(R.string.erroremptyfield)
                    }else->{
                    binding.layoutCompleteName.helperText =""
                    binding.btnRegister.isEnabled=true
                }


                }
            }
        }

        binding.username.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding.layoutUsername.helperText =getString(R.string.erroremptyfield)
                        binding.btnRegister.isEnabled=false
                    }else->{
                    binding.layoutUsername.helperText =""
                    binding.btnRegister.isEnabled=true
                    }



                }
            }
        }

        binding.passwordRegister.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding.layoutPassword.helperText =getString(R.string.erroremptyfield)
                        binding.btnRegister.isEnabled=false
                    }else->{
                    binding.layoutPassword.helperText =""
                    binding.btnRegister.isEnabled=true
                    }
                }
            }
        }

    }

    private fun register() {
        binding.apply {
            if(emailRegister.text.toString().isEmpty() && nameComplete.text.toString().isEmpty() && username.text.toString().isEmpty() && passwordRegister.text.toString().isEmpty()){
                layoutRegister.helperText =getString(R.string.erroremptyfield)
                layoutCompleteName.helperText =getString(R.string.erroremptyfield)
                layoutUsername.helperText =getString(R.string.erroremptyfield)
                layoutPassword.helperText =getString(R.string.erroremptyfield)
                btnRegister.isEnabled = false
            }else{
                layoutRegister.helperText =""
                layoutCompleteName.helperText =""
                layoutUsername.helperText =""
                layoutPassword.helperText =""
                btnRegister.isEnabled = true
                createUserAuthentificationWithFirebase(emailRegister.text.toString(),passwordRegister.text.toString())
            }
        }

    }
    private fun createUserAuthentificationWithFirebase(email:String,password:String){
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            mAuthProvider.register(email, password).addOnCompleteListener { tast->
             if (tast.isSuccessful){
               registerUserInDatatabase(email,binding.nameComplete.text.toString(),binding.username.text.toString(),password)
             }else{
                 binding.layoutRegister.helperText=tast.exception!!.message
             }
            }
        }

    }

    private fun registerUserInDatatabase(email: String , nameComplete: String , userName: String , password: String) {
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        if (dialog.window!=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
        val saveData =Users(email,nameComplete,userName,password,0)
        viewModel.register(mAuthProvider.getId().toString(),saveData)
        viewModel.responseRegister.observe(viewLifecycleOwner, Observer {response->
         if (response.isSuccessful){
             Toast.makeText(requireContext() , "La cuenta fue creada con exito" , Toast.LENGTH_SHORT).show()
             navController.navigate(R.id.action_registerFragment_to_loginFragment)
             dialog.dismiss()
         }else{
             Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show()
         }
        })
    }
}