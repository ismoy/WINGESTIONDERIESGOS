package com.win.gestionderiesgos.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.ListProjectAdapter
import com.win.gestionderiesgos.data.adapter.ListProjectAdminAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.TokenProvider
import com.win.gestionderiesgos.databinding.FragmentHomeBinding
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.login.LoginViewModel


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var mTokensProvider: TokenProvider
    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private lateinit var projectAdapter: ListProjectAdminAdapter
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
       binding =FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mTokensProvider = TokenProvider()
        navController = NavController(requireContext())
        mAuthProvider = AuthProvider()
        projectAdapter = ListProjectAdminAdapter()
        binding.recyclerView.apply {
            adapter =projectAdapter
            layoutManager = LinearLayoutManager(context)
        }
        createSwipe()
        generateToken()
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.getAllProject()
        setUpRecyclerView()
        viewModel.responseGetAllProject.observe(viewLifecycleOwner, Observer { project->
            if (project.isNotEmpty()){
                projectAdapter.setData(project)
            }
        })

    }

    private fun createSwipe() {
        binding.swiperefresh.apply {
            setOnRefreshListener {
                delayed()
            }
            setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary)
        }

    }

    private fun delayed() {
        Handler(Looper.getMainLooper()).postDelayed({
            observeViewModel()
            binding.swiperefresh.isRefreshing=false
        },2000)
    }
    private fun setUpRecyclerView() {

    }
    private fun generateToken(){
        mTokensProvider.createTokenAdmin(mAuthProvider.getId().toString())
    }
}