package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.databinding.ItemProyectListBinding
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.utils.Constants

class ViewHolderListProjectAdapter(view: View):RecyclerView.ViewHolder(view) {
    private val binding =ItemProyectListBinding.bind(view)
    private lateinit var getFusionProvider: GetFusionProvider
    private lateinit var projectListProvider: ProjectListProvider
    @SuppressLint("SetTextI18n")
    fun renderListProject(currentList: Project , onClickListener:(Project) -> Unit) {
        getFusionProvider= GetFusionProvider()
        projectListProvider = ProjectListProvider()
        val parseInt =currentList.QuantityPercent.toFloat()
       binding.apply {
           nameProject.text =currentList.name
           percente.text="${parseInt.toInt()}%"

       }
        if (currentList.status==""){
            itemView.isEnabled =false
            binding.apply {
                nameProject.isEnabled=false
                percente.isEnabled=false
                percente.text = "0%"
            }
        }else{
            binding.percente.text="${parseInt.toInt()}%"
            itemView.setOnClickListener {
                onClickListener(currentList)
                Navigation.findNavController(binding.nameProject.context as Activity , R.id.container_fragment_client).navigate(R.id.action_homeClientFragment_to_listFuscionsProjectFragment)
            }
        }
        if (parseInt.toInt()==100){
            projectListProvider.updateStatusProject(currentList.idKeyProject,"Finish")
        }
    }
}