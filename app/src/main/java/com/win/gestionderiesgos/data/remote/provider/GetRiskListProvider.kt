package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.HashMap

class GetRiskListProvider {
   private var mDatabase: DatabaseReference= FirebaseDatabase.getInstance().reference.child("Riesgo")
    private var mDatabaseRiskUser: DatabaseReference= FirebaseDatabase.getInstance().reference.child("RiesgoUsuario")
     fun getRisk(): DatabaseReference {
        return mDatabase
    }
    fun getRiskByUser():DatabaseReference {
        return mDatabaseRiskUser
    }

    fun updateTimer(idKeyActivity:String,timeRisk: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["timeRisk"] = timeRisk
        return idKeyActivity.let { mDatabaseRiskUser.child(it).updateChildren(map) }
    }

    fun updateStatus(idKeyActivity:String,status: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status"] = status
        return idKeyActivity.let { mDatabaseRiskUser.child(it).updateChildren(map) }
    }
}