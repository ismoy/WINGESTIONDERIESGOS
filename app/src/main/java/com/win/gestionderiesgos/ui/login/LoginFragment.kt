package com.win.gestionderiesgos.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentLoginBinding
import com.win.gestionderiesgos.presentation.login.LoginViewModel
import com.win.gestionderiesgos.ui.activity.Home.HomeActivity
import com.win.gestionderiesgos.ui.activity.Home.HomeClientActivity
import com.win.gestionderiesgos.ui.activity.registerFuncion.RegisterFuncionActivity
import com.win.gestionderiesgos.utils.ShowDialog

class LoginFragment : Fragment() {
    private var binding:FragmentLoginBinding?=null
    private lateinit var navController: NavController
    private lateinit var mShowDialog: ShowDialog
    private lateinit var mAuthProvider: AuthProvider
    private  val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentLoginBinding.inflate(inflater,container,false)
        validateRealTime()
        mShowDialog = ShowDialog(requireContext())
        mAuthProvider = AuthProvider()
        return binding!!.root
    }

    private fun validateRealTime() {
        binding!!.emailLogin.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding!!.layoutEmail.helperText =getString(R.string.erroremptyfield)
                        binding!!.btnLogin.isEnabled=false
                    }else->{
                    binding!!.layoutEmail.helperText =""
                    binding!!.btnLogin.isEnabled=true
                }

                }
            }

        }

        binding!!.password.apply {
            doOnTextChanged { textInput, start, before, count ->
                when{
                    textInput!!.isEmpty()->{
                        binding!!.layoutpassword.helperText =getString(R.string.erroremptyfield)
                    }else->{
                    binding!!.layoutpassword.helperText =""
                    binding!!.btnLogin.isEnabled=true
                }


                }
            }
        }
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        binding?.apply {
         btnRegistrate.setOnClickListener {
             navController.navigate(R.id.action_loginFragment_to_registerFragment)
         }
            btnLogin.setOnClickListener {
                login(emailLogin.text.toString(),password.text.toString())
            }
            resetPassword.setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
        }
    }

    private fun login(email: String , password: String) {
        if (email.isEmpty() && password.isEmpty()){
            binding!!.layoutEmail.helperText  =getString(R.string.erroremptyfield)
            binding!!.layoutpassword.helperText  =getString(R.string.erroremptyfield)
            binding!!.btnLogin.isEnabled=false
        }else{
            binding!!.layoutEmail.helperText  =""
            binding!!.btnLogin.isEnabled=true
            signInUser(email,password)
        }

    }

    private fun signInUser(email: String , password: String) {
        viewModel.login(email, password)
        mShowDialog.showDialog()
       viewModel.responseLoginRepository.observe(viewLifecycleOwner, Observer {
        it.addOnCompleteListener { task->
            if (task.isSuccessful){
                if (mAuthProvider.existSession()){
                    viewModel.getOnlyUser(mAuthProvider.getId().toString())
                }
                viewModel.responseUsers.observe(viewLifecycleOwner, Observer { user->
                    if (user.isSuccessful){
                        if (user.body()?.role=="Client"){
                            startActivity(Intent(requireContext(), HomeClientActivity::class.java))
                            requireActivity().finish()
                            mShowDialog.dismissDialog()
                        }else{
                            startActivity(Intent(requireContext(),HomeActivity ::class.java))
                            requireActivity().finish()
                            mShowDialog.dismissDialog()
                        }
                    }else{
                        Toast.makeText(requireContext() , user.message().toString() , Toast.LENGTH_SHORT).show()
                        mShowDialog.dismissDialog()
                    }
                })

            }else{
                Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_LONG).show()
                mShowDialog.dismissDialog()
            }
        }
       })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding =null
    }
}