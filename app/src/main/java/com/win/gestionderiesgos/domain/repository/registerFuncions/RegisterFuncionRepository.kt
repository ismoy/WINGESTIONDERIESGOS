package com.win.gestionderiesgos.domain.repository.registerFuncions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.getFunciosProvider
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.FusionList
import retrofit2.Response

class RegisterFuncionRepository {
    private val getFuncions by lazy { getFunciosProvider() }
    suspend fun RegisterFuncios(funcions: Funcions): Response<Funcions> {
        return RetrofitInstance.wingestionApi.registerFuncions(funcions)
    }

     fun getFuncions(): LiveData<List<Funcions>> {
        val mutableLiveData =MutableLiveData<List<Funcions>>()
        getFuncions.getFuncions()?.addListenerForSingleValueEvent(object : ValueEventListener{
            val listFuncions = mutableListOf<Funcions>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val name =ds.child("name").value.toString()
                        val idUser =ds.child("idUser").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val QuantityPercent=ds.child("QuantityPercent").value.toString()
                        val listas =Funcions(name,idUser, dateCreated,QuantityPercent)
                        listFuncions.add(listas)
                    }
                    mutableLiveData.value =listFuncions
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }


    fun getFusionList(): LiveData<List<FusionList>> {
        val mutableLiveDataList =MutableLiveData<List<FusionList>>()
        getFuncions.getFuncions()?.addListenerForSingleValueEvent(object : ValueEventListener{
            val listFuncions = mutableListOf<FusionList>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val name =ds.child("name").value.toString()
                        val idUser =ds.child("idUser").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val QuantityPercent=ds.child("QuantityPercent").value.toString()
                        val listas =FusionList(name,idUser, dateCreated,QuantityPercent)
                        if (listas.name.isNotEmpty())
                        listFuncions.add(listas)
                    }
                    mutableLiveDataList.value =listFuncions
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveDataList
    }
}