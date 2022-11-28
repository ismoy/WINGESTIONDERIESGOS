package com.win.gestionderiesgos.domain.repository.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.domain.model.Project

class HomeRepository {
    private val getProject by lazy { ProjectListProvider() }
    val noExistProject:MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val noExistProjectForUser:MutableLiveData<Boolean> by lazy { MutableLiveData() }
    fun getProject(idUser:String): LiveData<List<Project>> {

        val mutableLiveData = MutableLiveData<List<Project>>()
        getProject.getProject().orderByChild("idUserAsign").equalTo(idUser).addListenerForSingleValueEvent(object : ValueEventListener {
            val listProject = mutableListOf<Project>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val idProject=ds.child("idProject").value.toString()
                        val name =ds.child("name").value.toString()
                        val initialDate =ds.child("initialDate").value.toString()
                        val endDate =ds.child("endDate").value.toString()
                        val idUserAsign =ds.child("idUserAsign").value.toString()
                        val idAdmin =ds.child("idAdmin").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val quantityPercent =ds.child("QuantityPercent").value.toString()
                        val status=ds.child("status").value.toString()
                        val listas =Project(idProject,name,initialDate, endDate, idUserAsign, idAdmin, dateCreated,quantityPercent,ds.key.toString(), status)
                        listProject.add(listas)
                    }
                    mutableLiveData.value =listProject
                }else{
                   noExistProjectForUser.value=true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

     fun getAllProject(): LiveData<List<Project>> {

        val mutableLiveData = MutableLiveData<List<Project>>()
        getProject.getProject().addValueEventListener(object : ValueEventListener {
            val listProject = mutableListOf<Project>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val idProject=ds.child("idProject").value.toString()
                        val name =ds.child("name").value.toString()
                        val initialDate =ds.child("initialDate").value.toString()
                        val endDate =ds.child("endDate").value.toString()
                        val idUserAsign =ds.child("idUserAsign").value.toString()
                        val idAdmin =ds.child("idAdmin").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val quantityPercent =ds.child("QuantityPercent").value.toString()
                        val status=ds.child("status").value.toString()
                        val listas =Project(idProject,name,initialDate, endDate, idUserAsign, idAdmin, dateCreated,quantityPercent,ds.key.toString(), status)
                        listProject.add(listas)
                    }
                    mutableLiveData.value =listProject
                }else{
                    noExistProject.value =true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }


    fun getProjectOrderByStatus(): LiveData<List<Project>> {
        val mutableLiveData = MutableLiveData<List<Project>>()
        getProject.getProject().orderByChild("status").equalTo("Finish").addListenerForSingleValueEvent(object : ValueEventListener {
            val listProject = mutableListOf<Project>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val idProject=ds.child("idProject").value.toString()
                        val name =ds.child("name").value.toString()
                        val initialDate =ds.child("initialDate").value.toString()
                        val endDate =ds.child("endDate").value.toString()
                        val idUserAsign =ds.child("idUserAsign").value.toString()
                        val idAdmin =ds.child("idAdmin").value.toString()
                        val dateCreated =ds.child("dateCreated").value.toString()
                        val quantityPercent =ds.child("QuantityPercent").value.toString()
                        val status=ds.child("status").value.toString()
                        val listas =Project(idProject,name,initialDate, endDate, idUserAsign, idAdmin, dateCreated,quantityPercent,ds.key.toString(), status)
                        listProject.add(listas)
                    }
                    mutableLiveData.value =listProject
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }
}