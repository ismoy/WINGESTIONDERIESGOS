package com.win.gestionderiesgos.ui.fragment.detailsFusionActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.adapter.DetailsFusionActivityAdapter
import com.win.gestionderiesgos.data.adapter.ListRiskAdapter
import com.win.gestionderiesgos.data.remote.provider.*
import com.win.gestionderiesgos.databinding.FragmentDetailsFusionsActivityBinding
import com.win.gestionderiesgos.domain.model.CountClickRisk
import com.win.gestionderiesgos.domain.model.NotificationData
import com.win.gestionderiesgos.domain.model.PushNotification
import com.win.gestionderiesgos.presentation.notification.NotificationViewModel
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailsFusionsActivityFragment : Fragment() {
    private lateinit var binding: FragmentDetailsFusionsActivityBinding
    private var nameFusion: String? = null
    private var idKeyFusion: String? = null
    private var progr = 0
    private var onclick = 0
    private var totalQuantity: Int? = null
    private var idKeyProject: String? = null
    private var idProject: String? = null
    private var resultPercent: Float? = 0F
    private var quantityClickRisk: Int = 0
    private var fusionArrive: String? = null
    private var nameProject:String?=null
    private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private lateinit var detailsFusionActivityAdapter: DetailsFusionActivityAdapter
    private lateinit var riskAdapter: ListRiskAdapter
    private lateinit var detailsProvider: GetFusionProvider
    private lateinit var onClickRiskProvider: OnClickRiskProvider
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var getFusionsProvider: GetFusionProvider
    private val viewModelRegisterProject by lazy { ViewModelProvider(this)[RegisterProjectViewModel::class.java] }
    private lateinit var countClickRiskProvider: CountClickRiskProvider
    private var quantityCountRisk: Int = 0
    private lateinit var mTokensProvider:TokenProvider
    private val notificationViewModel by lazy { ViewModelProvider(this)[NotificationViewModel::class.java] }



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
        nameProject =requireActivity().intent.getStringExtra("nameProject")
        detailsFusionActivityAdapter = DetailsFusionActivityAdapter()
        riskAdapter = ListRiskAdapter()
        detailsProvider = GetFusionProvider()
        mAuthProvider = AuthProvider()
        getFusionsProvider = GetFusionProvider()
        onClickRiskProvider = OnClickRiskProvider()
        countClickRiskProvider = CountClickRiskProvider()
        mTokensProvider= TokenProvider()
        binding.recyclerViewActivity.apply {
            adapter = detailsFusionActivityAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.recyclerViewRisk.apply {
            adapter = riskAdapter
            layoutManager = LinearLayoutManager(context)
        }
        Constants.setValueSharedPreferences(requireActivity(),"nameProjects",nameProject!!)
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
                Constants.setValueSharedPreferences(
                    requireActivity() ,
                    "idProjectSelected" ,
                    idProject!!
                )
                updateDataInGoogleSheet()
            })

        viewModel.getOnClickRisk(nameFusion!!)
        viewModel.getResponseOnclick.observe(viewLifecycleOwner , Observer { totClick ->
            totClick.forEach {
                quantityClickRisk = it.quantityClick
                fusionArrive = it.nameFusion
            }

        })
        onclickriskButton()


        viewModel.onClickRisk(nameFusion!!)
        viewModel.responseClickRisk.observe(viewLifecycleOwner , Observer { countTotal ->
            quantityCountRisk = countTotal.toInt()
            Log.d("fjfjhfhfhfhffhfh",countTotal.toString())
            if (countTotal!=null){
                updateClickInGoogleSheet(countTotal)
            }

        })

        viewModel.getTimeRealiseByActivities(idKeyProject!!)
        viewModel.responsegetTimeRealiseByActivities.observe(viewLifecycleOwner, Observer {list->
        //Todo this function is for call all times any project and calculate the total time this project realise this in the future

        })

    }

    private fun onclickriskButton() {
        binding.button.setOnClickListener {
            onclick += 1
            val countClickRisk = CountClickRisk(quantityCountRisk.plus(onclick).toString())
            viewModel.createSnapshotRisk(nameFusion!! , countClickRisk)
            sendNotificationToOtherDevice()
        }
    }

    private fun updateClickInGoogleSheet(countTotal: String) {
        viewModel.responsecreateSnapshotRisk.observe(viewLifecycleOwner , Observer { response ->
            if (response.isSuccessful) {
                when (nameFusion) {
                    "PLANTA EXTERNA" -> {
                        viewModelRegisterProject.updateClickRiesgoPlantaExternaInGoogleSheet(
                            Constants.ACTION_UPDATE_CLICK_RIESGOS_PLANTA_EXTERNA ,
                            idProject!! ,
                            countTotal
                        )
                    }
                    "PLANTA INTERNA" -> {
                        viewModelRegisterProject.updateClickRiesgoPlantaInternaInGoogleSheet(
                            Constants.ACTION_UPDATE_CLICK_RIESGOS_PLANTA_INTERNA ,
                            idProject!! ,
                            countTotal
                        )
                    }
                    "OBRA CIVIL" -> {
                        viewModelRegisterProject.updateClickRiesgoObraCivilInGoogleSheet(
                            Constants.ACTION_UPDATE_CLICK_RIESGOS_OBRA_CIVIL ,
                            idProject!! ,
                            countTotal
                        )
                    }
                    "FUSIONES" -> {
                        viewModelRegisterProject.updateClickRiesgoFusionesInGoogleSheet(
                            Constants.ACTION_UPDATE_CLICK_RIESGOS_FUSIONES ,
                            idProject!! ,
                            countTotal
                        )
                    }
                }

                Toast.makeText(
                    requireContext() ,
                    "se registro correctamente" ,
                    Toast.LENGTH_SHORT
                ).show()


            } else {
                Toast.makeText(
                    requireContext() ,
                    response.errorBody().toString() ,
                    Toast.LENGTH_SHORT
                ).show()

            }
        })
    }

    private fun updateStatusFusion() {
        getFusionsProvider.updateIdKeyFusion(idKeyFusion!! , "Finish")
    }

    private fun updateStatusFusionIdKeyProject() {
        getFusionsProvider.updateStatusIdKeyProject(idKeyFusion!! , "${idKeyProject}_Finish")
    }

    private fun setUpRecyclerView() {
        idKeyFusion?.let { viewModel.getDetailsFusionActivity(it) }
        viewModel.responseDetailsActivity.observe(viewLifecycleOwner , Observer {
            detailsFusionActivityAdapter.setData(it)
            totalQuantity = it.size

        })

        viewModel.getAllRiskByIdUser("${mAuthProvider.getId()}_$nameFusion")
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
                updateStatusFusionIdKeyProject()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgressBar() {
        binding.progress.progress = progr
        detailsProvider.updateQuantityPercent(idKeyFusion.toString() , progr.toString())
        binding.tvpercent.text = "$progr%"
    }

    private fun updateDataInGoogleSheet() {
        when (nameFusion) {
            "PLANTA INTERNA" -> {
                viewModelRegisterProject.updatePlantaInternaInGoogleSheet(
                    Constants.ACTION_UPDATE_INTERNA ,
                    idProject!! ,
                    progr.toString()
                )
            }
            "PLANTA EXTERNA" -> {
                viewModelRegisterProject.updatePlantaExternaInGoogleSheet(
                    Constants.ACTION_UPDATE_EXTERNA ,
                    idProject!! ,
                    progr.toString()
                )
            }
            "OBRA CIVIL" -> {
                viewModelRegisterProject.updatePlantaObraCivilInGoogleSheet(
                    Constants.ACTION_UPDATE_OBRA_CIVIL ,
                    idProject!! ,
                    progr.toString()
                )
            }
            "FUSIONES" -> {
                viewModelRegisterProject.updateFusionesInGoogleSheet(
                    Constants.ACTION_UPDATE_FUSIONES ,
                    idProject!! ,
                    progr.toString()
                )
            }
        }
    }

    private fun sendNotificationToOtherDevice() {
        mTokensProvider.getTokenAdmin()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val tokenClient: String = ds.child("token").value.toString()
                            PushNotification(
                                NotificationData(
                                    Constants.TITLENOTIFICATION , "El usuario ${Constants.getValueSharedPreferences(requireActivity(),"nameUser")} ha reportado un riesgo en el proyecto $nameProject y el fusion reportado es $nameFusion"
                                ) , tokenClient
                            ).also { push ->
                                notificationViewModel.sendNotification(push)
                                notificationViewModel.responseNotification.observe(
                                    viewLifecycleOwner
                                ) {
                                    if (it.isSuccessful) {
                                        Toast.makeText( requireContext(), "su riesgo ha sido reportado" , Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext() ,
                            "El usuario que estas asignando no tiene el token instalado por enden no se puede enviarle notification" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}