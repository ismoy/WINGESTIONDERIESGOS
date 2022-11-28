package com.win.gestionderiesgos.domain.repository.registerFuncions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.domain.model.Funcions

class ListFusionRepository {
    private val getFusionByIdProject by lazy { GetFusionProvider() }
     var noExistFusionForProject=MutableLiveData<Boolean>()


     fun getFusions(uidProject:String): LiveData<List<Funcions>> {
        val mutableLiveData =MutableLiveData<List<Funcions>>()
         getFusionByIdProject.getFuncions().orderByChild("idProject").equalTo(uidProject).addListenerForSingleValueEvent(object : ValueEventListener{
             val listFuncions = mutableListOf<Funcions>()
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()){
                     for (ds in snapshot.children){
                         val name =ds.child("name").value.toString()
                         val idUser =ds.child("idUser").value.toString()
                         val dateCreated =ds.child("dateCreated").value.toString()
                         val QuantityPercent=ds.child("QuantityPercent").value.toString()
                         val idProject =ds.child("idProject").value.toString()
                         val nameProject =ds.child("nameProject").value.toString()
                         val status=ds.child("status").value.toString()
                         val listas =Funcions(name,idUser, dateCreated,QuantityPercent,idProject,nameProject,ds.key.toString(),status)
                         listFuncions.add(listas)
                     }
                     mutableLiveData.value =listFuncions
                 }else{
                     noExistFusionForProject.value =true
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }

         })
        return mutableLiveData
    }

    fun getFusionFinished():MutableLiveData<Float>{
        val mutableLiveData =MutableLiveData<Float>()
        getFusionByIdProject.getFuncions().orderByChild("status").equalTo("Finish").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    mutableLiveData.value =snapshot.childrenCount.toFloat()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

}