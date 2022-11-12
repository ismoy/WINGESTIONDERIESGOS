package com.win.gestionderiesgos.ui.fragment.listFuscionsProject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.ListFuncionsAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentListFuscionsProjectBinding
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.login.LoginViewModel
import com.win.gestionderiesgos.presentation.registerFuncions.RegisterFuncionsViewModel
import com.win.gestionderiesgos.utils.Constants

class ListFuscionsProjectFragment : Fragment() {
 private lateinit var binding:FragmentListFuscionsProjectBinding
 private lateinit var listFuncionsAdapter: ListFuncionsAdapter
    private  val viewModel by lazy { ViewModelProvider(this)[RegisterFuncionsViewModel::class.java] }
    private  val viewModelMain by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var mAuthProvider:AuthProvider

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentListFuscionsProjectBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mAuthProvider =AuthProvider()
        listFuncionsAdapter = ListFuncionsAdapter()
        binding.recyclerview.apply {
            adapter =listFuncionsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        viewModelMain.getOnlyUser(mAuthProvider.getId().toString())
        viewModelMain.responseUsers.observe(viewLifecycleOwner, Observer { user->
            if (user.isSuccessful){
                if (user.body()?.role =="Cliente"){
                    viewModel.getFusionsList()
                    viewModel.getFusionsList().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()){
                            listFuncionsAdapter.setData(it)
                        }
                    })
                }else{
                    binding.recyclerview.visibility =View.GONE
                }
            }
        })

    }
}