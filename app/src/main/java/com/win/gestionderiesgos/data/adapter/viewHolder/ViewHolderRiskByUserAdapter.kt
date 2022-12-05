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
import com.win.gestionderiesgos.databinding.ItemShowRiskUsersBinding
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.RiskByUser
import com.win.gestionderiesgos.services.TimerService
import kotlin.math.roundToInt

class ViewHolderRiskByUserAdapter(view: View):RecyclerView.ViewHolder(view) {
    private  val binding =ItemShowRiskUsersBinding.bind(view)

    fun renderListRisk(currentList: RiskByUser , holder: ViewHolderRiskByUserAdapter) {
        binding.apply {
            nameuser.text=currentList.nameUser
            nameFunsion.text=currentList.fusionName
            nameRiesgoPredeterminado.text=currentList.riesgoPredetermido
            nameRiesgoNoIdentificado.text=currentList.riesgoNoIndificado
            nameTiempoRiesgo.text=currentList.timeRisk
            nameFechaCreado.text=currentList.dateCreated

        }
    }
}