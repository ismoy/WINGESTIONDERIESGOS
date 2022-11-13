package com.win.gestionderiesgos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.win.gestionderiesgos.data.adapter.ListProjectAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.FragmentHomeClientBinding
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.login.LoginViewModel


class HomeClientFragment : Fragment() {
    private lateinit var binding: FragmentHomeClientBinding
    private lateinit var mAuthProvider: AuthProvider
    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private  val viewModelMain by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var projectAdapter: ListProjectAdapter
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentHomeClientBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = NavController(requireContext())
        projectAdapter= ListProjectAdapter { onSectorPickingItemSelected(it) }
        mAuthProvider = AuthProvider()
        binding.recyclerView.apply {
            adapter =projectAdapter
            layoutManager =LinearLayoutManager(context)
        }

        setUpRecyclerView()
    }

    private fun onSectorPickingItemSelected(project: Project) {
        onClickItem(project)
    }

    private fun onClickItem(project: Project) {
        requireActivity().intent.putExtra("project",project.QuantityPercent)
    }

    private fun setUpRecyclerView() {
        viewModelMain.getOnlyUser(mAuthProvider.getId().toString())
        viewModelMain.responseUsers.observe(viewLifecycleOwner, Observer { user->
            if (user.isSuccessful){
                    viewModel.getProject(mAuthProvider.getId().toString())
                    viewModel.responseGetProject.observe(viewLifecycleOwner, Observer {
                        Log.d("llegouserss",it.toString())
                        if (it.isNotEmpty()){
                            projectAdapter.setData(it)
                        }
                    })
            }
        })

    }
}