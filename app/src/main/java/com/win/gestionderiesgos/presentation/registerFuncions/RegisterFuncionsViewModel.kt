package com.win.gestionderiesgos.presentation.registerFuncions

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.repository.registerFuncions.RegisterFuncionRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterFuncionsViewModel:ViewModel() {
    private val repository:RegisterFuncionRepository = RegisterFuncionRepository()
    val responseFuncions :MutableLiveData<Response<Funcions>> by lazy { MutableLiveData() }
    val responsegetFuncions = MutableLiveData<List<Funcions>>()
    var noExistSnapshot =MutableLiveData<Boolean>()
    val responseIdProjectSelected =MutableLiveData<String>()
    val responseFusionsOnlyAdmin = MutableLiveData<List<String>>()
    val responseIdFusionSelected =MutableLiveData<String>()

init {
    noExistSnapshot =repository.noExistSnapshot
}

    fun registerFuncions(funcions: Funcions){
        viewModelScope.launch {
          val response=  repository.RegisterFuncios(funcions)
            responseFuncions.value =response
        }
    }

    fun getFuncions(activity: Activity):LiveData<List<Funcions>>{
        viewModelScope.launch {
       repository.getFuncions(activity).observeForever {
           responsegetFuncions.value =it
       }

        }
        return responsegetFuncions
    }

    fun getIdProjectSelected(nameProject:String){
        viewModelScope.launch {
            repository.getIdProjectSelected(nameProject).observeForever {
                responseIdProjectSelected.value =it
            }
        }
    }

    fun getFusionOnlyAdmin(activity: Activity):LiveData<List<String>>{
        viewModelScope.launch {
            repository.getFusionOnLyAdmin(activity).observeForever {
                responseFusionsOnlyAdmin.value =it
            }

        }
        return responseFusionsOnlyAdmin
    }

    fun getIdFusionSelected(name:String){
        viewModelScope.launch {
            repository.getIdFusionSelected(name).observeForever {
                responseIdFusionSelected.value =it
            }
        }
    }

}