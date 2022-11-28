package com.win.gestionderiesgos.presentation.registerActivity

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.OnclickRisk
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.repository.registerActivity.RegisterActivityRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterActivityViewModel:ViewModel() {
    private val repository:RegisterActivityRepository= RegisterActivityRepository()
    val responseActivity :MutableLiveData<Response<Actividad>> by lazy { MutableLiveData() }
    val responseDetailsActivity:MutableLiveData<List<Actividad>> by lazy { MutableLiveData() }
    val responseGetActivitiesFinished :MutableLiveData<Float> by lazy { MutableLiveData() }
    val responseRisk:MutableLiveData<List<String>> by lazy { MutableLiveData() }
    val responseRiskByIdUser:MutableLiveData<List<Risk>> by lazy { MutableLiveData() }
    val responseClickRisk :MutableLiveData<Response<OnclickRisk>> by lazy { MutableLiveData() }
    val getResponseOnclick:MutableLiveData<List<OnclickRisk>> by lazy { MutableLiveData() }
    fun createActivity(actividad: Actividad){
        viewModelScope.launch {
            val response =repository.createActivity(actividad)
            responseActivity.value =response
        }
    }

    fun onClickRisk(onclickRisk: OnclickRisk){
        viewModelScope.launch {
            val response =repository.onClickRisk(onclickRisk)
            responseClickRisk.value =response
        }
    }
    fun getOnClickRisk(clickRisk:String){
        viewModelScope.launch {
        repository.getClicksRisk(clickRisk).observeForever {
            getResponseOnclick.value=it
        }
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

    fun getActivitiesFinished(status_idKeyFusion:String){
        viewModelScope.launch {
            repository.getActivitiesFinished(status_idKeyFusion).observeForever {
                responseGetActivitiesFinished.value =it
            }
        }
    }

    fun getAllRisk(){
        viewModelScope.launch {
            repository.getAllRisk().observeForever {
                responseRisk.value =it
            }
        }
    }

    fun getAllRiskByIdUser(idUser:String){
        viewModelScope.launch {
            repository.getAllRiskByIdUser(idUser).observeForever {
                responseRiskByIdUser.value =it
            }
        }
    }
}