package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.databinding.ItemProyectListBinding
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListProjectAdminAdapter(view: View):RecyclerView.ViewHolder(view) {
    private val binding =ItemProyectListBinding.bind(view)
    private lateinit var getFusionProvider: GetFusionProvider
    @SuppressLint("SetTextI18n")
    fun renderListProject(currentList: Project) {
        getFusionProvider= GetFusionProvider()
       binding.apply {
           nameProject.text =currentList.name
       }
        if (currentList.QuantityPercent=="0") {
            itemView.isEnabled = false
            binding.apply {
                nameProject.isEnabled = false
                percente.isEnabled = false
                percente.text = "0%"
            }
        }else{
            binding.apply {
                percente.text="${currentList.QuantityPercent}%"
                itemView.isEnabled =true
            }
        }
    }


}