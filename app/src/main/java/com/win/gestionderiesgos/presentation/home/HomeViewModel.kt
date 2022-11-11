package com.win.gestionderiesgos.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.repository.home.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
   private val repository:HomeRepository = HomeRepository()
    val responseGetProject = MutableLiveData<List<Project>>()

    fun getProject(idUser:String):LiveData<List<Project>> {
        viewModelScope.launch {
            repository.getProject(idUser).observeForever {
                responseGetProject.value =it
            }
        }
        return responseGetProject
    }
}