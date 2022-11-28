package com.win.gestionderiesgos.domain.repository.registerProject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.api.RetrofitInstanceGoogleSheet
import com.win.gestionderiesgos.data.remote.provider.ListOnlyUsersClient
import com.win.gestionderiesgos.data.remote.provider.GetFusionProvider
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response

class RegisterProjectRepository {
    private val getListUser by lazy { ListOnlyUsersClient() }
    private val getListFusion by lazy { GetFusionProvider() }
    private val getProjectList by lazy { ProjectListProvider() }

    suspend fun createProject(project:Project): Response<Project> {
       return RetrofitInstance.wingestionApi.createProject(project)
    }

     fun getListUsers():LiveData<List<Users>> {
            val mutableLiveData = MutableLiveData<List<Users>>()
        getListUser.getListUser()?.orderByChild("role")?.equalTo("Client")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                val listUsers= mutableListOf<Users>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (ds in snapshot.children){
                            val email =ds.child("email").value.toString()
                            val completeName =ds.child("completeName").value.toString()
                            val userName =ds.child("userName").value.toString()
                            val password =ds.child("password").value.toString()
                            val idUser =ds.child("idUser").value.toString()
                            val listas =Users(idUser,email,completeName,userName,password)
                            listUsers.add(listas)
                        }
                        mutableLiveData.value =listUsers
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            return mutableLiveData
    }

    fun getIdUserSelected(completeName:String): MutableLiveData<String> {
        val mutableLiveData=MutableLiveData<String>()
        getListUser.getListUser()?.orderByChild("completeName")?.equalTo(completeName)?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val idUser =ds.child("idUser").value.toString()
                        mutableLiveData.value =idUser
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

    fun getIdFusionSelected(name:String): MutableLiveData<String> {
        val mutableLiveData=MutableLiveData<String>()
        getListFusion.getFuncions().orderByChild("name").equalTo(name).addValueEventListener(object :ValueEventListener{
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

   suspend fun sendDataInGoogleSheet(action:String,id_Project:String,project:String,startDate:String,endDate:String,
                              priority:String,nCaja:String,node:String,contrata:String,percentPlantaExterna:String,
                              percentPlantaInterna:String,percentObracivil:String,percentFusiones:String,user:String,
                              time:String,clickRiskPlantaExterna:String,clickRiskPlantaInterna:String,clickRiskObracivil:String,
                              clickRiskFusiones:String,risk:String,fecha_de_revision:String,observaciones:String): String {

       return RetrofitInstanceGoogleSheet.wingestionApi.sendDataInGoogleSheet(action, id_Project, project, startDate, endDate, priority, nCaja, node, contrata, percentPlantaExterna, percentPlantaInterna, percentObracivil, percentFusiones, user, time, clickRiskPlantaExterna, clickRiskPlantaInterna, clickRiskObracivil, clickRiskFusiones, risk, fecha_de_revision, observaciones).toString()
    }

    suspend fun updatePlantaExternaInGoogleSheet(action:String,id_Project:String,percentPlantaExterna:String) {
        return RetrofitInstanceGoogleSheet.wingestionApi.updatePlantaExternaInGoogleSheet(action, id_Project,percentPlantaExterna)
    }
    suspend fun updatePlantaInternaInGoogleSheet(action:String,id_Project:String,percentPlantaInterna:String) {
        return RetrofitInstanceGoogleSheet.wingestionApi.updatePlantaInternaInGoogleSheet(action, id_Project,percentPlantaInterna)
    }

    suspend fun updateObraCivilInGoogleSheet(action:String,id_Project:String,percentObraCivil:String) {
        return RetrofitInstanceGoogleSheet.wingestionApi.updateObraCivilInGoogleSheet(action, id_Project,percentObraCivil)
    }

    suspend fun updateFusionesInGoogleSheet(action:String,id_Project:String,percentFusiones:String) {
        return RetrofitInstanceGoogleSheet.wingestionApi.updateFusionesInGoogleSheet(action, id_Project,percentFusiones)
    }

    fun getIdProject(idKeyProject:String):MutableLiveData<String>{
        val mutableLiveData =MutableLiveData<String>()
        getProjectList.getProject().orderByKey().equalTo(idKeyProject).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val idProject =ds.child("idProject").value.toString()
                        mutableLiveData.value =idProject
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mutableLiveData
    }
}