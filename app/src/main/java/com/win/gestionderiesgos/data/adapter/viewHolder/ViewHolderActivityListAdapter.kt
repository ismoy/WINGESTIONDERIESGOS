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
import com.win.gestionderiesgos.databinding.ItemListActivitiesBinding
import com.win.gestionderiesgos.databinding.ItemRiskListBinding
import com.win.gestionderiesgos.databinding.ItemShowRiskUsersBinding
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.RiskByUser
import com.win.gestionderiesgos.services.TimerService
import kotlin.math.roundToInt

class ViewHolderActivityListAdapter(view: View):RecyclerView.ViewHolder(view) {
    private  val binding =ItemListActivitiesBinding.bind(view)

    fun renderListRisk(currentList: Actividad , holder: ViewHolderActivityListAdapter) {
        binding.apply {
            nameactivities.text=currentList.actividad
            nameFunsion.text=currentList.funcion
            nameUser.text=currentList.nameUser
            nameProject.text=currentList.nameProject
            dateCreated.text=currentList.dateCreated
            time.text=currentList.timeFinish
            status.text=currentList.status

        }
    }
}