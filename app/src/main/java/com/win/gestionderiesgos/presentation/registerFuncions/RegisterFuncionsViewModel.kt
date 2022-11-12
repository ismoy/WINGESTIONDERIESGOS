package com.win.gestionderiesgos.presentation.registerFuncions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.FusionList
import com.win.gestionderiesgos.domain.repository.registerFuncions.RegisterFuncionRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterFuncionsViewModel:ViewModel() {
    private val repository:RegisterFuncionRepository = RegisterFuncionRepository()
    val responseFuncions :MutableLiveData<Response<Funcions>> by lazy { MutableLiveData() }
    val responseFusionList :MutableLiveData<List<FusionList>> by lazy { MutableLiveData() }
    val responsegetFuncions = MutableLiveData<List<Funcions>>()
    fun registerFuncions(funcions: Funcions){
        viewModelScope.launch {
          val response=  repository.RegisterFuncios(funcions)
            responseFuncions.value =response
        }
    }

    fun getFuncions():LiveData<List<Funcions>>{
        viewModelScope.launch {
       repository.getFuncions().observeForever {
           responsegetFuncions.value =it
       }

        }
        return responsegetFuncions
    }

    fun getFusionsList(): MutableLiveData<List<FusionList>> {
        viewModelScope.launch {
            repository.getFusionList().observeForever {
                responseFusionList.value =it
            }

        }
        return responseFusionList
    }
}