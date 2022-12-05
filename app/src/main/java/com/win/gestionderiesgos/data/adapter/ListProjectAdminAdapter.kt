package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderListProjectAdminAdapter
import com.win.gestionderiesgos.domain.model.Project

class ListProjectAdminAdapter:RecyclerView.Adapter<ViewHolderListProjectAdminAdapter>() {
    private var projectList = emptyList<Project>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderListProjectAdminAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_proyect_list,parent,false)
        return ViewHolderListProjectAdminAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderListProjectAdminAdapter , position: Int) {
       val currentList = projectList[position]
        holder.renderListProject(currentList)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() =projectList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Project>) {
        this.projectList = list
        notifyDataSetChanged()
    }
}