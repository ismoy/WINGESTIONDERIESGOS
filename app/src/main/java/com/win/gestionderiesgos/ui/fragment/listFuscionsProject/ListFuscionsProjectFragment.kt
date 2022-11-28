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
import com.win.gestionderiesgos.data.adapter.ListFuncionsAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.databinding.FragmentListFuscionsProjectBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.presentation.registerFuncions.ListFusionViewModel
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.utils.Constants
import com.win.gestionderiesgos.utils.ShowDialog

class ListFuscionsProjectFragment : Fragment() {
 private lateinit var binding:FragmentListFuscionsProjectBinding
 private lateinit var listFuncionsAdapter: ListFuncionsAdapter
    private  val viewModel by lazy { ViewModelProvider(this)[ListFusionViewModel::class.java] }

    private lateinit var mAuthProvider:AuthProvider
    private var idKeyProject:String?=null
    private var nameFusion:String?=null
    private var status:String?=null
    private lateinit var fusionProvider: GetFusionProvider
    private lateinit var showDialog: ShowDialog
    private var totalQuantity:Int?=null
    private var resultPercent:Float?=null
    private lateinit var projectListProvider: ProjectListProvider

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentListFuscionsProjectBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        fusionProvider = GetFusionProvider()
        idKeyProject =requireActivity().intent.getStringExtra("idProject")
        mAuthProvider =AuthProvider()
        projectListProvider= ProjectListProvider()
        listFuncionsAdapter = ListFuncionsAdapter{onListener(it)}
        showDialog = ShowDialog(requireContext())
        binding.recyclerview.apply {
            adapter =listFuncionsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        if (idKeyProject!=null){
            observerViewModel()
        }


    }

    private fun onListener(it: Funcions) {
       if (it.status=="Finish"){
           nameFusion=it.name
           status =it.status
       }
    }

    private fun observerViewModel() {
        showDialog.showDialog()
        idKeyProject?.let { viewModel.getFusionsByIdProject(it) }
       viewModel.responseGetFusionsByIdProject.observe(viewLifecycleOwner, Observer {
           totalQuantity=it.size
           setUpRecyclerView(it)
       })
        viewModel.noExistFusionForProject.observe(viewLifecycleOwner, Observer { noExist->
            if (noExist){
                binding.apply {
                    noData.visibility =View.VISIBLE
                    showDialog.dismissDialog()
                }
            }
        })
        viewModel.getFusionFinished()
        viewModel.responseGetFusionFinished.observe(viewLifecycleOwner, Observer {totalFinish->
            if (totalFinish!=null){
                 resultPercent= totalFinish / totalQuantity!! * 100
                Log.d("dfjdjddjdjdj",resultPercent.toString())
               updatePercentProject(resultPercent!!)
            }else{
                 resultPercent= 0F
                updatePercentProject(resultPercent!!)
            }

        })
        Log.d("dfjdjddjdjdj",totalQuantity.toString())
    }

    private fun updatePercentProject(resultPercent: Float) {
        projectListProvider.updateQuantityPercent(idKeyProject!!,resultPercent.toString())?.addOnCompleteListener {  }
        projectListProvider.updateIdKeyProject(idKeyProject!!,idKeyProject)?.addOnCompleteListener {  }
    }

    private fun setUpRecyclerView(list: List<Funcions>) {
        if (list.isNotEmpty()){
            listFuncionsAdapter.setData(list)
            binding.noData.visibility =View.GONE
            showDialog.dismissDialog()

        }

    }


    override fun onResume() {
        super.onResume()
        idKeyProject?.let { viewModel.getFusionsByIdProject(it) }
    }
}