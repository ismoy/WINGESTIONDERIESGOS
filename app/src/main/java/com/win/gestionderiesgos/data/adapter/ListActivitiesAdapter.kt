package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderActivityListAdapter
import com.win.gestionderiesgos.domain.model.Actividad

class ListActivitiesAdapter:RecyclerView.Adapter<ViewHolderActivityListAdapter>() {
    private var activityList = emptyList<Actividad>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderActivityListAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_activities,parent,false)
        return ViewHolderActivityListAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderActivityListAdapter , position: Int) {
        val currentList = activityList[position]
        holder.renderListRisk(currentList,holder)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = activityList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Actividad>) {
        this.activityList = list
        notifyDataSetChanged()
    }
}