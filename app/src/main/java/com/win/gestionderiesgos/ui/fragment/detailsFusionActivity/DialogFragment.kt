package com.win.gestionderiesgos.ui.fragment.detailsFusionActivity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.DialogFragmentBinding
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.RiskByUser
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.presentation.registerProject.RegisterProjectViewModel
import com.win.gestionderiesgos.presentation.registerRisk.RegisterRiskViewModel
import com.win.gestionderiesgos.utils.Constants

class DialogFragment:DialogFragment() {
  private val viewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }
    private val viewModelRegisterRisk by lazy { ViewModelProvider(this)[RegisterRiskViewModel::class.java] }
    private val viewModelRegisterProject by lazy { ViewModelProvider(this)[RegisterProjectViewModel::class.java] }

    private var selectedItem:String=""
    private var nameFusion:String?=null
    private var idKeyActivity:String?=null
    private var idKeyFusion:String?=null
    private lateinit var binding:DialogFragmentBinding
    private lateinit var mAuthProvider: AuthProvider
    private var arrayRisk=ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding= DialogFragmentBinding.inflate(inflater,container,false)
        dialog!!.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        mAuthProvider= AuthProvider()
        nameFusion= requireActivity().intent.getStringExtra("nameFusion")
        idKeyActivity= requireActivity().intent.getStringExtra("idKeyActivity")
        idKeyFusion= requireActivity().intent.getStringExtra("idKeyFusion")
        viewModelObserver()

    }

    private fun viewModelObserver() {
        viewModel.getAllRisk()
        viewModel.responseRisk.observe(viewLifecycleOwner, Observer {
            showRisk(it)
        })
        binding.apply {
            btnAccept.setOnClickListener {
                val risk= RiskByUser(nameFusion!!,selectedItem,binding.nameRisk.text.toString(),mAuthProvider.getId().toString(),Constants.CURRENTTIME.toString(),idKeyActivity!!,idKeyFusion!!,"","","","",
                    Constants.getValueSharedPreferences(requireActivity(),"nameUser"),"${mAuthProvider.getId()}_$nameFusion")
                viewModelRegisterRisk.registerRiskUser(risk)
                viewModelRegisterRisk.responseRiskByUser.observe(viewLifecycleOwner, Observer { response->
                    if (response.isSuccessful){
                        viewModelRegisterProject.updateRiesgoInGoogleSheet(Constants.ACTION_UPDATE_RIESGOS,Constants.getValueSharedPreferences(requireActivity(),"idProjectSelected"),binding.nameRisk.text.toString())
                        Toast.makeText(requireContext() , "se registro correctamente" , Toast.LENGTH_SHORT).show()
                        dialog!!.dismiss()
                    }
                })
            }
        }
    }

    private fun showRisk(list: List<String>) {
        val  adapterItems = ArrayAdapter(requireContext(), R.layout.dropdowm_item,list)
        binding.autoCompleteTextViewfusion.setAdapter(adapterItems)
        binding.autoCompleteTextViewfusion.setOnItemClickListener { adapterView, view, i, l ->
             selectedItem = adapterView.getItemAtPosition(i).toString()

        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window

        val x = Constants.screenDisplay(window = window!!.windowManager, tolerance = 0.95)

        window.setLayout(x, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)
    }
}