package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderDetailsFusionsAdapter
import com.win.gestionderiesgos.domain.model.Actividad

class DetailsFusionActivityAdapter:RecyclerView.Adapter<ViewHolderDetailsFusionsAdapter>() {
    private var projectList = emptyList<Actividad>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderDetailsFusionsAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_activity,parent,false)
        return ViewHolderDetailsFusionsAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderDetailsFusionsAdapter , position: Int) {
       val currentList = projectList[position]
        holder.renderDetailsFusionActivity(currentList,holder)
        holder.setIsRecyclable(false)

    }

    override fun getItemCount() =projectList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Actividad>) {
        this.projectList = list
        notifyDataSetChanged()
    }
}