package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class OnClickRiskProvider {
    var mDatabase:DatabaseReference=FirebaseDatabase.getInstance().reference.child("OnClickRisk")
    fun getClicks():DatabaseReference {
        return mDatabase
    }

    fun updateClicks(idKey:String,quantityClick: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["quantityClick"] = quantityClick
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }
}