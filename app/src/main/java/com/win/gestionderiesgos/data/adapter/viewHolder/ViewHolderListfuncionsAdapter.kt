package com.win.gestionderiesgos.data.adapter.viewHolder

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.databinding.ItemListFuncionsBinding
import com.win.gestionderiesgos.domain.model.Funcions

class ViewHolderListfuncionsAdapter(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListFuncionsBinding.bind(view)
    private var progr = 0
    private lateinit var getFusionProvider: GetFusionProvider

    @SuppressLint("SetTextI18n")
    fun renderListfuncions(
        currentList: Funcions ,
        onListener: (Funcions) -> Unit
    ) {
        getFusionProvider = GetFusionProvider()
        val quantityPercent = currentList.QuantityPercent.toInt()
        updateProgressBar(quantityPercent)
        if (progr <= 90) {
            progr += quantityPercent
            updateProgressBar(quantityPercent)
        }

        binding.apply {
            btntplanta.text = currentList.name
            tvpercent.text = "$quantityPercent%"
        }
        onListener(currentList)
        if (currentList.status == "") {
            itemView.isEnabled = false
            binding.apply {
                btntplanta.isEnabled = false
                progress.visibility =View.GONE
                tvpercent.visibility=View.GONE
            }
        } else {
            itemView.setOnClickListener {
                Navigation.findNavController(
                    binding.btntplanta.context as Activity ,
                    R.id.container_fragment_client
                ).navigate(
                    R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "idKeyFusion" ,
                    currentList.idKeyFusion
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "NameFusion" ,
                    currentList.name
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "status" ,
                    currentList.status
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "idProject" ,
                    currentList.idProject
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "nameProject" ,
                    currentList.nameProject
                )
            }
            binding.btntplanta.setOnClickListener {
                Navigation.findNavController(
                    binding.btntplanta.context as Activity ,
                    R.id.container_fragment_client
                ).navigate(
                    R.id.action_listFuscionsProjectFragment_to_detailsFusionsActivityFragment
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "NameFusion" ,
                    currentList.name
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "idKeyFusion" ,
                    currentList.idKeyFusion
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "status" ,
                    currentList.status
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "idProject" ,
                    currentList.idProject
                )

                (binding.btntplanta.context as Activity).intent.putExtra(
                    "QuantityPercentFusion" ,
                    currentList.QuantityPercent
                )
                (binding.btntplanta.context as Activity).intent.putExtra(
                    "nameProject" ,
                    currentList.nameProject
                )
            }
        }

        getFusionProvider.getFusionListData().observeForever {
        }

    }

    private fun updateProgressBar(quantityPercent: Int) {
        binding.apply {
            if (quantityPercent == 0) progress.progress = 0 else progress.progress = progr
        }

    }


}