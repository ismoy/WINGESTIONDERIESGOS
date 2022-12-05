package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderRiskAdapter
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderRiskByUserAdapter
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.RiskByUser

class ListRiskByUserAdapter:RecyclerView.Adapter<ViewHolderRiskByUserAdapter>() {
    private var riskList = emptyList<RiskByUser>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderRiskByUserAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_risk_users,parent,false)
        return ViewHolderRiskByUserAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderRiskByUserAdapter , position: Int) {
        val currentList = riskList[position]
        holder.renderListRisk(currentList,holder)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = riskList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<RiskByUser>) {
        this.riskList = list
        notifyDataSetChanged()
    }
}