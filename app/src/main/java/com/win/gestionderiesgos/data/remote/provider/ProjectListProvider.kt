package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.HashMap

class ProjectListProvider {
   private var mDatabase: DatabaseReference= FirebaseDatabase.getInstance().reference.child("Admin").child("Proyectos")

     fun getProject(): DatabaseReference {
        return mDatabase
    }
    fun getIdProjectSelected():DatabaseReference {
        return mDatabase
    }
    fun updateStatusProject(idKey:String,status: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status"] = status
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateQuantityPercent(idKey:String,quantityPercent: String?): Task<Void?>? {
        val map: MutableMap<String?, Any?> = HashMap()
        map["QuantityPercent"] = quantityPercent
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }

    fun updateIdKeyProject(idKey:String,idKeyProject: String?): Task<Void?>? {
        val map: MutableMap<String?, Any?> = HashMap()
        map["idKeyProject"] = idKeyProject
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }
}