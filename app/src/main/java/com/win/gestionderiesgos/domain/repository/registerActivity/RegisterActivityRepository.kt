package com.win.gestionderiesgos.domain.repository.registerActivity

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.GetDetailsFusionActivityProvider
import com.win.gestionderiesgos.domain.model.Actividad
import retrofit2.Response

class RegisterActivityRepository {
    private val getDetailsFusionActivity by lazy { GetDetailsFusionActivityProvider() }
    suspend fun createActivity(actividad: Actividad):Response<Actividad>{
        return RetrofitInstance.wingestionApi.createActivity(actividad)
    }

    fun getDetailsActivity(fusion:String):LiveData<List<Actividad>>{
        val mutableLiveDataList=MutableLiveData<List<Actividad>>()
        getDetailsFusionActivity.getDetailsFusionActivity()?.orderByChild("funcion")?.equalTo(fusion)?.addListenerForSingleValueEvent(object :ValueEventListener{
            val listActivity = mutableListOf<Actividad>()
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                  for (ds in snapshot.children){
                      val name =ds.child("actividad").value.toString()
                      val fusion =ds.child("funcion").value.toString()
                      val idUser =ds.child("idUser").value.toString()
                      val dateCreated =ds.child("dateCreated").value.toString()
                      val quantityPercent =ds.child("QuantityPercent").value.toString()
                      val timeFinish =ds.child("timeFinish").value.toString()
                      val addList=Actividad(fusion,name,idUser,dateCreated,quantityPercent,timeFinish.toDouble(),ds.key.toString())
                      listActivity.add(addList)
                  }
                  mutableLiveDataList.value =listActivity
              }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveDataList
    }

    /*fun updateTimer(timeFinish:String){
        getDetailsFusionActivity.updateTimer(timeFinish)!!.addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("inciado",it.toString())
            }
        }
    }*/
}