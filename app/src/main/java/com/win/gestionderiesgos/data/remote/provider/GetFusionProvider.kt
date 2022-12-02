package com.win.gestionderiesgos.data.remote.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.win.gestionderiesgos.domain.model.Funcions
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

    fun updateStatusIdKeyProject(idKey:String,status_idKeyProject: String?): Task<Void?> {
        val map: MutableMap<String?, Any?> = HashMap()
        map["status_idKeyProject"] = status_idKeyProject
        return idKey.let { mDatabase.child(it).updateChildren(map) }
    }

    fun getFusionListData():MutableLiveData<List<String>> {
        val mutableLiveData =MutableLiveData<List<String>>()
         getFuncions().orderByChild("QuantityPercent").addListenerForSingleValueEvent(object :ValueEventListener{
             val listFusions = mutableListOf<String>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val quantityPercent=ds.child("QuantityPercent").value.toString()
                        listFusions.add(quantityPercent)
                    }
                    mutableLiveData.value=listFusions
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mutableLiveData
    }
}