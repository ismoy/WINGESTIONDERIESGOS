package com.win.gestionderiesgos.ui.fragment.showRiskOnlyAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.ListRiskByUserAdapter
import com.win.gestionderiesgos.databinding.FragmentShowListRistOnlyAdminBinding
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel

class ShowListRiskOnlyAdminFragment : Fragment() {
    private lateinit var binding:FragmentShowListRistOnlyAdminBinding
    private lateinit var listRiskByUserAdapter: ListRiskByUserAdapter
    private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowListRistOnlyAdminBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        listRiskByUserAdapter= ListRiskByUserAdapter()
        binding.recyclerView.apply {
            adapter=listRiskByUserAdapter
            layoutManager=LinearLayoutManager(context)
        }
       observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.getAllRiskUserOnlyAdmin()
        viewModel.responseGetAllRiskUserOnlyAdmin.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                listRiskByUserAdapter.setData(it)
            }
        })
    }
}