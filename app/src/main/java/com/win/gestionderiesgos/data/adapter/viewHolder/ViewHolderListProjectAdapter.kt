package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.databinding.ItemProyectListBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListProjectAdapter(view: View):RecyclerView.ViewHolder(view) {
    private val binding =ItemProyectListBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun renderListProject(currentList: Project , onClickListener:(Project) -> Unit) {
       binding.apply {
           nameProject.text =currentList.name
           percente.text ="${currentList.QuantityPercent}%"
       }
        if (currentList.QuantityPercent==0){
            itemView.isEnabled =false
            binding.nameProject.isEnabled=false
            binding.percente.isEnabled=false
        }else{
            itemView.setOnClickListener {
                onClickListener(currentList)
                Navigation.findNavController(binding.nameProject.context as Activity , R.id.container_fragment_client).navigate(R.id.action_homeClientFragment_to_listFuscionsProjectFragment)
            }
        }

    }
}