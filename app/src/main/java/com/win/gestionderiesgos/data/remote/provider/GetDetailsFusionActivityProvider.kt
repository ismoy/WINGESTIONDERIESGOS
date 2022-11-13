package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class GetDetailsFusionActivityProvider {
    var mDatabase: DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Admin").child("Proyectos").child("Fusion").child("Actividad")
    fun getDetailsFusionActivity():DatabaseReference?{
        return mDatabase
    }

    fun updateTimer(idKeyActivity:String,timerFinish: String?): Task<Void?>? {
        val map: MutableMap<String?, Any?> = HashMap()
        map["timeFinish"] = timerFinish
        return idKeyActivity.let { mDatabase?.child(it)?.updateChildren(map) }
    }

    fun updateQuantityPercent(idKeyActivity:String,quantityPercent: String?): Task<Void?>? {
        val map: MutableMap<String?, Any?> = HashMap()
        map["QuantityPercent"] = quantityPercent
        return idKeyActivity.let { mDatabase?.child(it)?.updateChildren(map) }
    }
}