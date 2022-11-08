package com.win.gestionderiesgos.domain.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.domain.model.Project

class HomeRepository {
    private val getProject by lazy { ProjectListProvider() }

    suspend fun getProject(): LiveData<List<Project>> {

        val mutableLiveData = MutableLiveData<List<Project>>()
        getProject.getProject()?.addValueEventListener(object : ValueEventListener {
            val listProject = mutableListOf<Project>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val name =ds.child("name").value.toString()
                        val percentQuantity =ds.child("percentQuantity").value.toString()
                        val listas =Project(name,percentQuantity.toInt())
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