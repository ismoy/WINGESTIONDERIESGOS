package com.win.gestionderiesgos.ui.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentResetPasswordBinding
import com.win.gestionderiesgos.utils.ShowDialog

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var mAuth:AuthProvider
    private lateinit var dialog: ShowDialog
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentResetPasswordBinding.inflate(inflater,container,false)
        mAuth = AuthProvider()
        dialog = ShowDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.apply {
            btnResetPassword.setOnClickListener {
                resetPassword(emailResetPassword.text.toString())
            }
            btnRegistrate.setOnClickListener {
                navController.navigate(R.id.action_resetPasswordFragment_to_registerFragment)
            }
        }
        validateRealTime()
    }

    private fun validateRealTime() {
        binding.emailResetPassword.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding.layoutEmail.helperText =getString(R.string.erroremptyfield)
                        binding.btnResetPassword.isEnabled=false
                    }else->{
                    binding.layoutEmail.helperText =""
                    binding.btnResetPassword.isEnabled=true
                }

                }
            }

        }
    }

    private fun resetPassword(email:String) {
        dialog.showDialog()
        mAuth.language()
        mAuth.resetPassword(email).addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                    dialog.dismissDialog()
                    Toast.makeText(requireContext() ,
                        "Se ha enviado un correo electrónico para restablecer su contraseña revise su bandeja de entrada" , Toast.LENGTH_LONG).show()
                } else {
                    dialog.dismissDialog()
                    binding.layoutEmail.helperText =task.exception!!.message
                }
            }
    }
}