package com.win.gestionderiesgos.presentation.registerRisk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.repository.registerFuncions.RegisterFuncionRepository
import com.win.gestionderiesgos.domain.repository.registerRisk.RegisterRiskRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterRiskViewModel:ViewModel() {
    private val repository:RegisterRiskRepository = RegisterRiskRepository()
    val responseRisk :MutableLiveData<Response<Risk>> by lazy { MutableLiveData() }
    val responseRiskByUser = MutableLiveData<Response<Risk>>()

    fun registerRisk(risk: Risk){
        viewModelScope.launch {
          val response=  repository.createRisk(risk)
            responseRisk.value =response
        }
    }
    fun registerRiskUser(risk: Risk){
        viewModelScope.launch {
            val response = repository.createRiskUser(risk)
            responseRiskByUser.value =response
        }
    }
}