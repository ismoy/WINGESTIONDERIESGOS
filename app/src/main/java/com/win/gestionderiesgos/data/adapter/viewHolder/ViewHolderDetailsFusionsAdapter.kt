package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.GetDetailsFusionActivityProvider
import com.win.gestionderiesgos.databinding.ItemListActivityBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.presentation.registerActivity.RegisterActivityViewModel
import com.win.gestionderiesgos.services.TimerService
import kotlin.math.roundToInt

class ViewHolderDetailsFusionsAdapter(view: View): RecyclerView.ViewHolder(view) {
    private val binding =ItemListActivityBinding.bind(view)
    private var timerStarted =false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    private var idKeyActivity:String?=null
    private lateinit var detailsProvider:GetDetailsFusionActivityProvider
    private var currentItem:Actividad?=null

    @SuppressLint("ResourceAsColor")
    fun renderDetailsFusionActivity(
        currentList: Actividad ,
        holder: ViewHolderDetailsFusionsAdapter) {
        currentItem=currentList
        binding.apply {
            nameActivity.text =currentList.actividad
            if (currentList.QuantityPercent>"0"){
                timerActivity.text =currentList.timeFinish.toString()
                startActivity.visibility =View.GONE
                power.visibility =View.VISIBLE
                power.isEnabled=false
                status.visibility =View.VISIBLE
                Log.d("calculos", (currentList.QuantityPercent.toInt()).toString())
            }else{
                itemView.context .registerReceiver(updateTime,IntentFilter(TimerService.TIMER_UPDATED))
            }
        }
        serviceIntent= Intent(binding.nameActivity.context.applicationContext,TimerService::class.java)


        detailsProvider = GetDetailsFusionActivityProvider()
        idKeyActivity =currentList.idKeyActivity
        onClickListener(holder)
    }
    private fun startTimer() {
     serviceIntent.putExtra(TimerService.TIMER_EXTRA,time)
        binding.timerActivity.context.startService(serviceIntent)
        binding.startActivity.visibility=View.GONE
        binding.power.visibility =View.VISIBLE
        binding.status.visibility=View.VISIBLE
        binding.status.text ="Start"
    }

    private fun stopTimer() {
        binding.timerActivity.context.stopService(serviceIntent)
        binding.startActivity.visibility=View.VISIBLE
        binding.power.visibility =View.GONE
        binding.status.visibility=View.VISIBLE
        binding.status.text ="Finish"
    }
    private val updateTime:BroadcastReceiver =object :BroadcastReceiver(){
        override fun onReceive(context: Context , intent: Intent) {
            time=intent.getDoubleExtra(TimerService.TIMER_EXTRA,0.0)
            if (binding.startActivity.isVisible){
                binding.timerActivity.text= ""
            }else{
                detailsProvider.updateTimer(idKeyActivity.toString(),time.toString())?.addOnSuccessListener {}
                binding.timerActivity.text =getTimeStringFromDouble(time)
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

    private fun onClickListener(holder: ViewHolderDetailsFusionsAdapter) {

        binding.apply {
            startActivity.setOnClickListener {
                startTimer()
            }
            power.setOnClickListener {
                stopTimer()
                detailsProvider.updateQuantityPercent(idKeyActivity.toString(),"25")
            }
        }
    }

}