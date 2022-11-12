
package com.win.gestionderiesgos.ui.fragment.detailsFusionActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.win.gestionderiesgos.data.adapter.DetailsFusionActivityAdapter
import com.win.gestionderiesgos.databinding.FragmentDetailsFusionsActivityBinding
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel


class DetailsFusionsActivityFragment : Fragment() {
    private lateinit var binding:FragmentDetailsFusionsActivityBinding
    private var nameFusion:String?=null
    private var progr = 0
    private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private lateinit var detailsFusionActivityAdapter: DetailsFusionActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsFusionsActivityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
       nameFusion= requireActivity().intent.getStringExtra("NameFusion")
        detailsFusionActivityAdapter= DetailsFusionActivityAdapter()
        binding.recyclerViewActivity.apply {
            adapter =detailsFusionActivityAdapter
            layoutManager = LinearLayoutManager(context)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        nameFusion?.let { viewModel.getDetailsFusionActivity(it) }
        viewModel.responseDetailsActivity.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                detailsFusionActivityAdapter.setData(it)
            }
            it.forEach { percent->
                if (percent.QuantityPercent.contains("25") && percent.QuantityPercent!="0"){

                    val  quantityPercent = (percent.QuantityPercent.toInt() * it.size).toString()
                    Log.d("valorpercent",quantityPercent)
                    setUpDataInProgress(percent.QuantityPercent)
                }

            }

        })
    }

    private fun setUpDataInProgress(quantityPercent: String) {
        Log.d("valorpercent",quantityPercent)
        binding.apply {
            if (progr <= 90){
                progr += 20
                updateProgressBar()
            }
            btnFusion.text=nameFusion
        }
    }
    private fun updateProgressBar(){
        binding.progress.progress=progr
    }
}