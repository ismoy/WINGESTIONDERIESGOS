package com.win.gestionderiesgos.data.adapter.viewHolder

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.databinding.ItemListFuncionsBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.FusionList
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListfuncionsAdapter(view: View): RecyclerView.ViewHolder(view){
    private val binding =ItemListFuncionsBinding.bind(view)
    private var progr = 0
    fun renderListfuncions(currentList: FusionList) {

        updateProgressBar()

        if (currentList.name.contains("null")){
            binding.relativecontainer.visibility=View.GONE
        }else{
            val quantityPercent =currentList.QuantityPercent.toInt()
            if (progr <= 90){
                progr +=quantityPercent
                updateProgressBar()
            }
        }
        binding.apply {
            btntplanta.text =currentList.name
        }
        if (currentList.QuantityPercent=="0"){
            itemView.isEnabled =false
            binding.btntplanta.isEnabled =false
        }else{
            itemView.setOnClickListener {
                Navigation.findNavController(binding.btntplanta.context as Activity , R.id.container_fragment_client).navigate(
                    R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment)
                (binding.btntplanta.context as Activity).intent.putExtra("NameFusion",currentList.name)
                (binding.btntplanta.context as Activity).intent.putExtra("QuantityPercentFusion",currentList.QuantityPercent)
            }
            binding.btntplanta.setOnClickListener {
                    Navigation.findNavController(binding.btntplanta.context as Activity , R.id.container_fragment_client).navigate(
                        R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment)
                    (binding.btntplanta.context as Activity).intent.putExtra("NameFusion",currentList.name)
                    (binding.btntplanta.context as Activity).intent.putExtra("QuantityPercentFusion",currentList.QuantityPercent)
            }
        }

    }

    private fun updateProgressBar(){
        binding.progress.progress=progr
    }

}