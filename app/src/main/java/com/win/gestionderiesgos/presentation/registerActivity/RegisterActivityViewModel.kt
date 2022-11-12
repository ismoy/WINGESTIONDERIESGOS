package com.win.gestionderiesgos.presentation.registerActivity

import androidx.lifecycle.LiveData
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
    val responseDetailsActivity:MutableLiveData<List<Actividad>> by lazy { MutableLiveData() }
    val responseTimer :MutableLiveData<String> by lazy { MutableLiveData() }

    fun createActivity(actividad: Actividad){
        viewModelScope.launch {
            val response =repository.createActivity(actividad)
            responseActivity.value =response
        }
    }

    fun getDetailsFusionActivity(fusion:String):LiveData<List<Actividad>>{
        viewModelScope.launch {
            repository.getDetailsActivity(fusion).observeForever {
                responseDetailsActivity.value=it
            }
        }
        return responseDetailsActivity
    }

    /*fun updateTimer(timeFinish:String){
        viewModelScope.launch {
            val response =repository.updateTimer(timeFinish)
            responseTimer.value = response.toString()
        }
    }*/
}