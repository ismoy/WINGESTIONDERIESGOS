package com.win.gestionderiesgos.data.adapter.viewHolder

import android.app.Activity
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.databinding.ItemListFuncionsBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListfuncionsAdapter(view: View): RecyclerView.ViewHolder(view){
    private val binding =ItemListFuncionsBinding.bind(view)
    private var progr = 0
    fun renderListfuncions(currentList: Funcions,onListener:(Funcions) -> Unit) {
       updateProgressBar()
            val quantityPercent =currentList.QuantityPercent.toInt()
            if (progr <= 90){
                progr +=quantityPercent
                updateProgressBar()
            }
        binding.apply {
            btntplanta.text =currentList.name
        }
        onListener(currentList)
        if (currentList.status==""){
            itemView.isEnabled=false
            binding.apply {
                btntplanta.isEnabled=false
                progress.visibility=View.GONE
            }
        }else{
            itemView.setOnClickListener {
                Navigation.findNavController(binding.btntplanta.context as Activity , R.id.container_fragment_client).navigate(
                    R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment)
                (binding.btntplanta.context as Activity).intent.putExtra("idKeyFusion",currentList.idKeyFusion)
                (binding.btntplanta.context as Activity).intent.putExtra("NameFusion",currentList.name)
                (binding.btntplanta.context as Activity).intent.putExtra("status",currentList.status)
                (binding.btntplanta.context as Activity).intent.putExtra("idProject",currentList.idProject)
            }
            binding.btntplanta.setOnClickListener {
                Navigation.findNavController(binding.btntplanta.context as Activity , R.id.container_fragment_client).navigate(
                    R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment)
                (binding.btntplanta.context as Activity).intent.putExtra("NameFusion",currentList.name)
                (binding.btntplanta.context as Activity).intent.putExtra("idKeyFusion",currentList.idKeyFusion)
                (binding.btntplanta.context as Activity).intent.putExtra("status",currentList.status)
                (binding.btntplanta.context as Activity).intent.putExtra("idProject",currentList.idProject)

                (binding.btntplanta.context as Activity).intent.putExtra("QuantityPercentFusion",currentList.QuantityPercent)
            }
        }


    }

    private fun updateProgressBar(){
        binding.progress.progress=progr
    }


}