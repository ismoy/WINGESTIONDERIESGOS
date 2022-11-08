package com.win.gestionderiesgos.presentation.registerActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.repository.registerActivity.RegisterActivityRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterActivityViewModel:ViewModel() {
    private val repository:RegisterActivityRepository= RegisterActivityRepository()
    val responseActivity :MutableLiveData<Response<Actividad>> by lazy { MutableLiveData() }

    fun createActivity(actividad: Actividad){
        viewModelScope.launch {
            val response =repository.createActivity(actividad)
            responseActivity.value =response
        }
    }
}