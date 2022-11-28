package com.win.gestionderiesgos.domain.repository.registerFuncions

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.utils.Constants
import retrofit2.Response

class RegisterFuncionRepository {
    private val getFuncions by lazy { GetFusionProvider() }
    private val getIdProject by lazy { ProjectListProvider() }
    val noExistSnapshot:MutableLiveData<Boolean> by lazy { MutableLiveData() }
    suspend fun RegisterFuncios(funcions: Funcions): Response<Funcions> {
        return RetrofitInstance.wingestionApi.registerFuncions(funcions)
    }

     fun getFuncions(activity: Activity): LiveData<List<Funcions>> {
        val mutableLiveData =MutableLiveData<List<Funcions>>()
         getFuncions.getFuncions().addListenerForSingleValueEvent(object : ValueEventListener{
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
                         Constants.setValueSharedPreferences(activity,"idKeyFusion",ds.key.toString())
                         listFuncions.add(listas )
                     }
                     mutableLiveData.value =listFuncions
                 }else{
                     noExistSnapshot.value =true
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }

         })
        return mutableLiveData
    }


    fun getIdProjectSelected(nameProject:String):MutableLiveData<String>{
        val mutableLiveData =MutableLiveData<String>()
        getIdProject.getIdProjectSelected().orderByChild("name").equalTo(nameProject)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (ds in snapshot.children){
                            val idKeyProject:String =ds.key.toString()
                            mutableLiveData.value =idKeyProject
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        return mutableLiveData
    }

    fun getFusionOnLyAdmin(activity: Activity): LiveData<List<String>> {
        val mutableLiveData =MutableLiveData<List<String>>()
        getFuncions.getFuncions().addListenerForSingleValueEvent(object : ValueEventListener{
            val listFuncions = mutableListOf<String>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val name =ds.child("name").value.toString()
                        val nameProject =ds.child("nameProject").value.toString()
                        Constants.setValueSharedPreferences(activity,"idKeyFusion",ds.key.toString())
                        listFuncions.add(name)
                    }
                    mutableLiveData.value =listFuncions
                }else{
                    noExistSnapshot.value =true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

    fun getIdFusionSelected(name:String): MutableLiveData<String> {
        val mutableLiveData=MutableLiveData<String>()
        getFuncions.getFusionByIdProject().orderByChild("name").equalTo(name).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        mutableLiveData.value =ds.key.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }


}