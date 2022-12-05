package com.win.gestionderiesgos.ui.fragment.listActivities

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
import com.win.gestionderiesgos.data.adapter.ListActivitiesAdapter
import com.win.gestionderiesgos.databinding.FragmentListActivitiesBinding
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel


class ListActivitiesFragment : Fragment() {
   private lateinit var binding:FragmentListActivitiesBinding
    private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private lateinit var listActivitiesAdapter:ListActivitiesAdapter


    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListActivitiesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        listActivitiesAdapter = ListActivitiesAdapter()
        binding.recyclerView.apply {
            adapter=listActivitiesAdapter
            layoutManager= LinearLayoutManager(context)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getAllActivities()
        viewModel.responseGetAllActivities.observe(viewLifecycleOwner, Observer {
            listActivitiesAdapter.setData(it)
        })
    }

}