package com.win.gestionderiesgos.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.win.gestionderiesgos.R

class ShowDialog(context: Context){
   private  val dialog: Dialog= Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        if (dialog.window!=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}