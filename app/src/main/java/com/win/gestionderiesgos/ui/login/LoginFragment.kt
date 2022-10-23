package com.win.gestionderiesgos.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var binding:FragmentLoginBinding?=null
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentLoginBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        binding?.apply {
            gotoregister.setOnClickListener {
             navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding =null
    }
}