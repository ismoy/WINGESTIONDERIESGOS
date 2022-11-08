package com.win.gestionderiesgos.data.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.win.gestionderiesgos.databinding.ItemListFuncionsBinding
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Project

class ViewHolderListfuncionsAdapter(view: View): RecyclerView.ViewHolder(view){
    private val binding =ItemListFuncionsBinding.bind(view)
    private var progr = 0
    fun renderListfuncions(currentList: Project) {
        updateProgressBar()
        binding.apply {
            btntplanta.text =currentList.name
        }
        if (progr <= 90){
            progr +=currentList.percentQuantity
            updateProgressBar()
        }
    }

    private fun updateProgressBar(){
        binding.progress.progress=progr
    }
}