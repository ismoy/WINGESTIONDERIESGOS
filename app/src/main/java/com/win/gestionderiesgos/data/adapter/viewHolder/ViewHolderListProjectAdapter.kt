package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.databinding.ItemProyectListBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListProjectAdapter(view: View):RecyclerView.ViewHolder(view) {
    private val binding =ItemProyectListBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun renderListProject(currentList: Project , onClickListener:(Project) -> Unit) {
       binding.apply {
           nameProject.text =currentList.name
           percente.text ="${currentList.percentQuantity}%"
       }
        itemView.setOnClickListener {
            onClickListener(currentList)
        }
    }
}