package com.win.gestionderiesgos.presentation.registerProject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.domain.repository.registerProject.RegisterProjectRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterProjectViewModel:ViewModel() {
    private val repository:RegisterProjectRepository = RegisterProjectRepository()
    val myResponse : MutableLiveData<Response<Project>> by lazy { MutableLiveData() }
    val listUsers:MutableLiveData<List<Users>> by lazy { MutableLiveData() }

    fun createProject(project: Project){
        viewModelScope.launch {
            val response =repository.createProject(project)
            myResponse.value =response
        }
    }
    fun getListUser():LiveData<List<Users>>{
        viewModelScope.launch {
            repository.getListUsers().observeForever {
                listUsers.value =it
            }
        }
        return listUsers
    }
}