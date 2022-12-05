package com.win.gestionderiesgos.data.adapter.viewHolder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.data.remote.provider.GetDetailsFusionActivityProvider
import com.win.gestionderiesgos.data.remote.provider.GetRiskListProvider
import com.win.gestionderiesgos.databinding.ItemRiskListBinding
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.RiskByUser
import com.win.gestionderiesgos.services.TimerService
import kotlin.math.roundToInt

class ViewHolderRiskAdapter(view: View):RecyclerView.ViewHolder(view) {
    private  val binding =ItemRiskListBinding.bind(view)
    private var time = 0.0
    private lateinit var serviceIntent: Intent
    private lateinit var riskProvider:GetRiskListProvider
    private var idKeyActivity:String=""

    fun renderListRisk(currentList: RiskByUser , holder: ViewHolderRiskAdapter) {
        binding.apply {
            nameRisk.text =currentList.riesgoNoIndificado

            if (currentList.status=="Finish"){
                timerRisk.text =currentList.timeRisk
                startActivity.visibility =View.GONE
                power.visibility =View.VISIBLE
                power.isEnabled=false
                statusRisk.visibility =View.VISIBLE
            }else{
                itemView.context .registerReceiver(updateTime,IntentFilter(TimerService.TIMER_UPDATED))
            }
        }
        serviceIntent= Intent(binding.nameRisk.context.applicationContext,TimerService::class.java)
        riskProvider = GetRiskListProvider()
        idKeyActivity=currentList.idKeyActivity
        Log.d("llegoaquianciano",currentList.idKeyActivity)
        onClickListener(holder)
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA,time)
        binding.timerRisk.context.startService(serviceIntent)
        binding.startActivity.visibility=View.GONE
        binding.power.visibility =View.VISIBLE
        binding.statusRisk.visibility=View.VISIBLE
        binding.statusRisk.text ="Start"
    }

    private fun stopTimer() {
        binding.timerRisk.context.stopService(serviceIntent)
        binding.startActivity.visibility=View.VISIBLE
        binding.power.visibility =View.GONE
        binding.statusRisk.visibility=View.VISIBLE
        binding.statusRisk.text ="Finish"
    }
    private val updateTime: BroadcastReceiver =object : BroadcastReceiver(){
        override fun onReceive(context: Context , intent: Intent) {
            time=intent.getDoubleExtra(TimerService.TIMER_EXTRA,0.0)
            if (binding.startActivity.isVisible){
                binding.timerRisk.text= ""
            }else{
                riskProvider.updateTimer(idKeyActivity,getTimeStringFromDouble(time))
                    .addOnSuccessListener {}
                binding.timerRisk.text =getTimeStringFromDouble(time)
            }
        }

    }

    private fun getTimeStringFromDouble(time: Double):String {
        val resultInt =time.roundToInt()
        val hours =resultInt % 86400 /3600
        val minutes =resultInt % 86400 % 3600 /60
        val seconds =resultInt % 86400 % 3600 % 60
        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hour: Int , min: Int , sec: Int): String =String.format("%02d:%02d:%02d",hour,min,sec)

    private fun onClickListener(holder: ViewHolderRiskAdapter) {
        binding.apply {
            startActivity.setOnClickListener {
                startTimer()
            }
            power.setOnClickListener {
                stopTimer()
                riskProvider.updateStatus(idKeyActivity,"Finish")
            }
        }
    }
}