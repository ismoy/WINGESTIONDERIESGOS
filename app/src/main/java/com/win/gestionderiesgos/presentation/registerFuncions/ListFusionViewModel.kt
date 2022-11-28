package com.win.gestionderiesgos.presentation.registerFuncions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.repository.registerFuncions.ListFusionRepository
import com.win.gestionderiesgos.domain.repository.registerFuncions.RegisterFuncionRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ListFusionViewModel : ViewModel() {
    private val repository: ListFusionRepository = ListFusionRepository()
    val responseGetFusionsByIdProject = MutableLiveData<List<Funcions>>()
    var noExistFusionForProject = MutableLiveData<Boolean>()
    var responseGetFusionFinished=MutableLiveData<Float>()

    init {
        noExistFusionForProject = repository.noExistFusionForProject
    }

    fun getFusionsByIdProject(uidProject: String): LiveData<List<Funcions>> {
        viewModelScope.launch {
            repository.getFusions(uidProject).observeForever {
                responseGetFusionsByIdProject.value = it
            }

        }
        return responseGetFusionsByIdProject
    }

    fun getFusionFinished(){
        viewModelScope.launch {
            repository.getFusionFinished().observeForever {
                responseGetFusionFinished.value =it
            }
        }
    }
}