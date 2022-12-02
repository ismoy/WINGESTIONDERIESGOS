package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class GetDetailsFusionActivityProvider {
    private var mDatabase: DatabaseReference= FirebaseDatabase.getInstance().reference.child("Actividad")
    fun getDetailsFusionActivity():DatabaseReference{
        return mDatabase
    }

    fun updateTimer(idKeyActivity:String,timerFinish: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["timeFinish"] = timerFinish
        return idKeyActivity.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateStatus(idKeyActivity:String,status: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status"] = status
        return idKeyActivity.let { mDatabase.child(it).updateChildren(map) }
    }
    fun updateStatusIdKeyFusion(idKeyActivity:String,status_idKeyFusion: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status_idKeyFusion"] = status_idKeyFusion
        return idKeyActivity.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateStatusNameUser(idKeyActivity:String,nameUser: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["nameUser"] = nameUser
        return idKeyActivity.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateStatusNameProject(idKeyActivity:String,nameProject: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["nameProject"] = nameProject
        return idKeyActivity.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateQuantityPercent(idKey:String,quantityPercent: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["QuantityPercent"] = quantityPercent
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }
    fun getDetailsQuantityActivitiesFinished():DatabaseReference{
        return mDatabase
    }
}