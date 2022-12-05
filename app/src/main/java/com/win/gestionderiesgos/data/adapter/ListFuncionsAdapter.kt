package com.win.gestionderiesgos.data.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.adapter.viewHolder.ViewHolderListfuncionsAdapter
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.presentation.registerFuncions.ListFusionViewModel

class ListFuncionsAdapter(private val onListener:(Funcions) -> Unit) :RecyclerView.Adapter<ViewHolderListfuncionsAdapter>()  {
    private var listfuncions = emptyList<Funcions>()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderListfuncionsAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_funcions,parent,false)
        return ViewHolderListfuncionsAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderListfuncionsAdapter , position: Int) {
        val currentList = listfuncions[position]
        holder.renderListfuncions(currentList,onListener)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() =listfuncions.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Funcions>) {
        this.listfuncions = list
        notifyDataSetChanged()
    }
}