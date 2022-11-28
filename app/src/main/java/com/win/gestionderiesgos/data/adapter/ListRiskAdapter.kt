package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderRiskAdapter
import com.win.gestionderiesgos.domain.model.Risk

class ListRiskAdapter:RecyclerView.Adapter<ViewHolderRiskAdapter>() {
    private var riskList = emptyList<Risk>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderRiskAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_risk_list,parent,false)
        return ViewHolderRiskAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderRiskAdapter , position: Int) {
        val currentList = riskList[position]
        holder.renderListRisk(currentList,holder)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = riskList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Risk>) {
        this.riskList = list
        notifyDataSetChanged()
    }
}