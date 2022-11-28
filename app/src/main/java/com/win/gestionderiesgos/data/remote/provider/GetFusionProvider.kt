package com.win.gestionderiesgos.data.remote.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.HashMap

class GetFusionProvider {
    var mDatabase:DatabaseReference= FirebaseDatabase.getInstance().reference.child("Fusion")

     fun getFuncions(): DatabaseReference {
        return mDatabase
    }

     fun getFusionByIdProject(): DatabaseReference {
         return mDatabase
     }

     fun updateQuantityPercent(idKey:String,quantityPercent: String?): Task<Void?>? {
         val map: MutableMap<String?, Any?> = HashMap()
         map["QuantityPercent"] = quantityPercent
         return idKey.let { mDatabase.child(it).updateChildren(map) }
     }
    fun updateIdKeyFusion(idKey:String,status: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status"] = status
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }

}