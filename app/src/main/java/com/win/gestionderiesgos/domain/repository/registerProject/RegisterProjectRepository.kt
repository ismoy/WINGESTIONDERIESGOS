package com.win.gestionderiesgos.domain.repository.registerProject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.ListOnlyUsersClient
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response

class RegisterProjectRepository {
    private val getListUser by lazy { ListOnlyUsersClient() }
    suspend fun createProject(project:Project): Response<Project> {
       return RetrofitInstance.wingestionApi.createProject(project)
    }

    suspend fun getListUsers():LiveData<List<Users>> {
            val mutableLiveData = MutableLiveData<List<Users>>()
            getListUser.getListUser()?.addListenerForSingleValueEvent(object : ValueEventListener {
                val listUsers= mutableListOf<Users>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (ds in snapshot.children){
                            val email =ds.child("email").value.toString()
                            val completeName =ds.child("completeName").value.toString()
                            val userName =ds.child("userName").value.toString()
                            val password =ds.child("password").value.toString()
                            val role =ds.child("role").value.toString()
                            val idUser =ds.child("idUser").value.toString()
                            val status =ds.child("status").value.toString()
                            val listas =Users(idUser,email,completeName,userName,password,role,status.toInt())
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
}