package com.win.gestionderiesgos.domain.repository.registerActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.CountClickRiskProvider
import com.win.gestionderiesgos.data.remote.provider.GetDetailsFusionActivityProvider
import com.win.gestionderiesgos.data.remote.provider.GetRiskListProvider
import com.win.gestionderiesgos.data.remote.provider.OnClickRiskProvider
import com.win.gestionderiesgos.domain.model.*
import retrofit2.Response

class RegisterActivityRepository {
    private val getDetailsFusionActivity by lazy { GetDetailsFusionActivityProvider() }
    private val getRisk by lazy { GetRiskListProvider() }
    private val onClickRiskProvider by lazy { OnClickRiskProvider() }
    private val countClickRiskProvider by lazy { CountClickRiskProvider() }


    suspend fun createActivity(actividad: Actividad):Response<Actividad>{
        return RetrofitInstance.wingestionApi.createActivity(actividad)
    }

    suspend fun createSnapshotRisk(uidFusion:String,countClickRisk: CountClickRisk):Response<CountClickRisk>{
        return RetrofitInstance.wingestionApi.createSnapshotRisk(uidFusion,countClickRisk)
    }


    suspend fun onClickRisk(onclickRisk: OnclickRisk):Response<OnclickRisk>{
        return RetrofitInstance.wingestionApi.onClickRisk(onclickRisk)
    }

    fun getClicksRisk(clicksRisk:String):LiveData<List<OnclickRisk>>{
        val mutableLiveData=MutableLiveData<List<OnclickRisk>>()
        onClickRiskProvider.getClicks().orderByChild("nameFusion").equalTo(clicksRisk).addListenerForSingleValueEvent(object :ValueEventListener{
            val listClicks = mutableListOf<OnclickRisk>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val quantity =ds.child("quantityClick").value.toString()
                       val nameFusion =ds.child("nameFusion").value.toString()
                        val addList=OnclickRisk(quantity.toInt(),nameFusion)
                        listClicks.add(addList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }
    fun getDetailsActivity(idKeyfusion:String):LiveData<List<Actividad>>{
        val mutableLiveDataList=MutableLiveData<List<Actividad>>()
        getDetailsFusionActivity.getDetailsFusionActivity().orderByChild("idKeyFusion").equalTo(idKeyfusion).addListenerForSingleValueEvent(object :ValueEventListener{
            val listActivity = mutableListOf<Actividad>()
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                  for (ds in snapshot.children){
                      val name =ds.child("actividad").value.toString()
                      val fusions =ds.child("funcion").value.toString()
                      val idUser =ds.child("idUser").value.toString()
                      val dateCreated =ds.child("dateCreated").value.toString()
                      val quantityPercent =ds.child("QuantityPercent").value.toString()
                      val timeFinish =ds.child("timeFinish").value.toString()
                      val idKeyFusion =ds.child("idKeyFusion").value.toString()
                      val status = ds.child("status").value.toString()
                      val status_idKeyFusion =ds.child("status_idKeyFusion").value.toString()
                      val idKeyProject =ds.child("idKeyProject").value.toString()
                      val nameUser =ds.child("nameUser").value.toString()
                      val nameProject =ds.child("nameProject").value.toString()
                      val addList=Actividad(fusions,name,idUser,dateCreated,quantityPercent,timeFinish,ds.key.toString(),idKeyFusion,status,status_idKeyFusion,idKeyProject,nameUser, nameProject)
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

  fun getActivitiesFinished(status_idKeyFusion:String):MutableLiveData<Float>{
      val mutableLiveData =MutableLiveData<Float>()
          getDetailsFusionActivity.getDetailsQuantityActivitiesFinished().orderByChild("status_idKeyFusion").equalTo(status_idKeyFusion).addListenerForSingleValueEvent(object :ValueEventListener{
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
    fun getAllRisk(): LiveData<List<String>> {
        val mutableLiveData = MutableLiveData<List<String>>()
        getRisk.getRisk().addValueEventListener(object : ValueEventListener {
            val listRisk = mutableListOf<String>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val name =ds.child("risk").value.toString()
                        listRisk.add(name)
                    }
                    mutableLiveData.value =listRisk
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

    fun getAllRiskByIdUser(idUserNameFusion:String): LiveData<List<RiskByUser>> {
        val mutableLiveData = MutableLiveData<List<RiskByUser>>()
        getRisk.getRiskByUser().orderByChild("idUserNameFusion").equalTo(idUserNameFusion).addListenerForSingleValueEvent(object : ValueEventListener {
            val listRisk = mutableListOf<RiskByUser>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val fusion =ds.child("fusionName").value.toString()
                        val name =ds.child("riesgoNoIndificado").value.toString()
                        val riskPre =ds.child("riesgoPredetermido").value.toString()
                        val userId =ds.child("idUser").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val idKeyFusion =ds.child("idKeyFusion").value.toString()
                        val timeRisk =ds.child("timeRisk").value.toString()
                        val status =ds.child("status").value.toString()
                        val idKeyProject=ds.child("idKeyProject").value.toString()
                        val projectName =ds.child("projectName").value.toString()
                        val nameUser =ds.child("nameUser").value.toString()
                        val iduserNameFusion=ds.child("idUserNameFusion").value.toString()
                        val risk= RiskByUser(fusion,riskPre,name,userId,dateCreated,ds.key.toString(),idKeyFusion,timeRisk,status,idKeyProject,projectName,nameUser,iduserNameFusion )
                        listRisk.add(risk)
                    }
                    mutableLiveData.value =listRisk
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mutableLiveData
    }
    fun getAllRiskUserOnlyAdmin(): LiveData<List<RiskByUser>> {
        val mutableLiveData = MutableLiveData<List<RiskByUser>>()
        getRisk.getRiskByUser().addValueEventListener(object : ValueEventListener {
            val listRisk = mutableListOf<RiskByUser>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val fusion =ds.child("fusionName").value.toString()
                        val name =ds.child("riesgoNoIndificado").value.toString()
                        val riskPre =ds.child("riesgoPredetermido").value.toString()
                        val userId =ds.child("idUser").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val idKeyActivity =ds.child("idKeyActivity").value.toString()
                        val idKeyFusion =ds.child("idKeyFusion").value.toString()
                        val timeRisk =ds.child("timeRisk").value.toString()
                        val status =ds.child("status").value.toString()
                        val idKeyProject=ds.child("idKeyProject").value.toString()
                        val projectName =ds.child("projectName").value.toString()
                        val nameUser =ds.child("nameUser").value.toString()
                        val iduserNameFusion=ds.child("idUserNameFusion").value.toString()
                        val risk= RiskByUser(fusion,riskPre,name,userId,dateCreated,idKeyActivity,idKeyFusion,timeRisk,status,idKeyProject,projectName,nameUser,iduserNameFusion )
                        listRisk.add(risk)
                    }
                    mutableLiveData.value =listRisk
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mutableLiveData
    }

    fun getCountClickProvider(nameFusion:String):LiveData<String>{
        val mutableLiveData = MutableLiveData<String>()
        countClickRiskProvider.getCountClickRisk().orderByKey().equalTo(nameFusion).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val quantity =ds.child("quantityCount").value.toString()
                        mutableLiveData.value=quantity
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }
    fun getTimeRealiseByActivities(idKeyProject:String): MutableLiveData<List<String>> {
        val mutableLiveData =MutableLiveData<List<String>>()
        getDetailsFusionActivity.getDetailsFusionActivity().orderByChild("idKeyProject").equalTo(idKeyProject).addListenerForSingleValueEvent(object :ValueEventListener{
           val listTimes = mutableListOf<String>()
            override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                for (ds in snapshot.children){
                    val timeFinish =ds.child("timeFinish").value.toString()
                    listTimes.add(timeFinish)
                }
                mutableLiveData.value=listTimes
            }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }
     fun getAllActivities():LiveData<List<Actividad>>{
         val mutableLiveDataList=MutableLiveData<List<Actividad>>()
         getDetailsFusionActivity.getDetailsFusionActivity().addListenerForSingleValueEvent(object :ValueEventListener{
             val listActivity = mutableListOf<Actividad>()
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()){
                     for (ds in snapshot.children){
                         val name =ds.child("actividad").value.toString()
                         val fusions =ds.child("funcion").value.toString()
                         val idUser =ds.child("idUser").value.toString()
                         val dateCreated =ds.child("dateCreated").value.toString()
                         val quantityPercent =ds.child("QuantityPercent").value.toString()
                         val timeFinish =ds.child("timeFinish").value.toString()
                         val idKeyFusion =ds.child("idKeyFusion").value.toString()
                         val status = ds.child("status").value.toString()
                         val status_idKeyFusion =ds.child("status_idKeyFusion").value.toString()
                         val idKeyProject =ds.child("idKeyProject").value.toString()
                         val nameUser =ds.child("nameUser").value.toString()
                         val nameProject =ds.child("nameProject").value.toString()
                         val addList=Actividad(fusions,name,idUser,dateCreated,quantityPercent,timeFinish,ds.key.toString(),idKeyFusion,status,status_idKeyFusion,idKeyProject,nameUser, nameProject)
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
}