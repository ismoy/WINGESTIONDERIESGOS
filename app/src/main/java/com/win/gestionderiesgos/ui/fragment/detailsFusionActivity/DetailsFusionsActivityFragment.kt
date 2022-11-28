package com.win.gestionderiesgos.ui.fragment.detailsFusionActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.win.gestionderiesgos.data.adapter.DetailsFusionActivityAdapter
import com.win.gestionderiesgos.data.adapter.ListRiskAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.data.remote.provider.OnClickRiskProvider
import com.win.gestionderiesgos.databinding.FragmentDetailsFusionsActivityBinding
import com.win.gestionderiesgos.domain.model.OnclickRisk
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.utils.Constants


class DetailsFusionsActivityFragment : Fragment() {
    private lateinit var binding: FragmentDetailsFusionsActivityBinding
    private var nameFusion: String? = null
    private var idKeyFusion: String? = null
    private var progr = 0
    private var onclick = 0
    private var totalQuantity: Int? = null
    private var idKeyProject: String? = null
    private var idProject: String? = null
    private var resultPercent: Float? =0F
    private var quantityClickRisk:Int=0
    private var fusionArrive:String?=null
    private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private lateinit var detailsFusionActivityAdapter: DetailsFusionActivityAdapter
    private lateinit var riskAdapter: ListRiskAdapter
    private lateinit var detailsProvider: GetFusionProvider
    private lateinit var onClickRiskProvider: OnClickRiskProvider
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var getFusionsProvider: GetFusionProvider
    private val viewModelRegisterProject by lazy { ViewModelProvider(this)[RegisterProjectViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsFusionsActivityBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        nameFusion = requireActivity().intent.getStringExtra("NameFusion")
        idKeyFusion = requireActivity().intent.getStringExtra("idKeyFusion")
        idKeyProject = requireActivity().intent.getStringExtra("idProject")

        detailsFusionActivityAdapter = DetailsFusionActivityAdapter()
        riskAdapter = ListRiskAdapter()
        detailsProvider = GetFusionProvider()
        mAuthProvider = AuthProvider()
        getFusionsProvider = GetFusionProvider()
        onClickRiskProvider= OnClickRiskProvider()
        binding.recyclerViewActivity.apply {
            adapter = detailsFusionActivityAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.recyclerViewRisk.apply {
            adapter = riskAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.btnFusion.text = nameFusion
        setUpRecyclerView()
        viewModelObserver()

    }

    private fun viewModelObserver() {
        viewModel.getActivitiesFinished("${idKeyFusion}_Finish")
        viewModel.responseGetActivitiesFinished.observe(
            viewLifecycleOwner ,
            Observer { totalFinish ->
                if (totalFinish != null) {
                    resultPercent = totalFinish / totalQuantity!! * 100
                    setUpDataInProgress(resultPercent!!)
                } else {
                    val resultPercent = 0F
                    setUpDataInProgress(resultPercent)
                }

            })

        idKeyProject?.let { viewModelRegisterProject.getIdProject(it) }
        viewModelRegisterProject.responseIdProject.observe(
            viewLifecycleOwner ,
            Observer { id_Project ->
                idProject = id_Project
                updateDataInGoogleSheet()
            })

            viewModel.getOnClickRisk(nameFusion!!)
            viewModel.getResponseOnclick.observe(viewLifecycleOwner, Observer { totClick->
                totClick.forEach {
                    quantityClickRisk =it.quantityClick
                    fusionArrive =it.nameFusion
                }
                Log.d("ffjdjddjdjj",quantityClickRisk.toString())
                Log.d("ffjdjddjdjj",fusionArrive.toString())

            })
        binding.button.setOnClickListener {
            onclick += 1
            val resultClicks =onclick.plus(quantityClickRisk)
            val onclickRisk=OnclickRisk(resultClicks,nameFusion!!)
            viewModel.onClickRisk(onclickRisk)
            if (fusionArrive==nameFusion){
               onClickRiskProvider.updateClicks("-NHuyLgPFLZlUMyTBXLv",resultClicks.toString()).addOnCompleteListener {  }
            }else{
                viewModel.responseClickRisk.observe(viewLifecycleOwner, Observer { response->
                    if (response.isSuccessful){
                        Toast.makeText(requireContext() , "se registro otro" , Toast.LENGTH_SHORT).show()
                    }
                })
            }



        }


    }

    private fun updateStatusFusion() {
        getFusionsProvider.updateIdKeyFusion(idKeyFusion!! , "Finish")
    }

    private fun setUpRecyclerView() {
        idKeyFusion?.let { viewModel.getDetailsFusionActivity(it) }
        viewModel.responseDetailsActivity.observe(viewLifecycleOwner , Observer {
            detailsFusionActivityAdapter.setData(it)
            totalQuantity = it.size

        })

        viewModel.getAllRiskByIdUser(mAuthProvider.getId().toString())
        viewModel.responseRiskByIdUser.observe(viewLifecycleOwner , Observer { listRisk ->
            if (listRisk.isNotEmpty()) {
                riskAdapter.setData(listRisk)
                binding.tvRisk.visibility = View.VISIBLE
            }

        })

    }

    private fun setUpDataInProgress(resultPercent: Float) {
        binding.apply {
            if (progr <= 90) {
                progr += resultPercent.toInt()
                updateProgressBar()
            }
            if (progr == 100) {
                updateStatusFusion()
            }
        }
    }

    private fun updateProgressBar() {
        binding.progress.progress = progr
        detailsProvider.updateQuantityPercent(idKeyFusion.toString() , progr.toString())
    }

    private fun updateDataInGoogleSheet() {
        when (nameFusion) {
            "PLANTA INTERNA" -> {
                viewModelRegisterProject.updatePlantaInternaInGoogleSheet(
                    Constants.ACTION_UPDATE_INTERNA ,
                    idProject!! ,
                    resultPercent!!.toInt().toString()
                )
            }
            "PLANTA EXTERNA" -> {
                viewModelRegisterProject.updatePlantaExternaInGoogleSheet(
                    Constants.ACTION_UPDATE_EXTERNA ,
                    idProject!! ,
                    resultPercent!!.toInt().toString()
                )
            }
            "OBRA CIVIL" -> {
                viewModelRegisterProject.updatePlantaObraCivilInGoogleSheet(
                    Constants.ACTION_UPDATE_OBRA_CIVIL ,
                    idProject!! ,
                    resultPercent!!.toInt().toString()
                )
            }
            "FUSIONES" -> {
                viewModelRegisterProject.updateFusionesInGoogleSheet(
                    Constants.ACTION_UPDATE_FUSIONES ,
                    idProject!! ,
                    resultPercent!!.toInt().toString()
                )
            }
        }
    }
}