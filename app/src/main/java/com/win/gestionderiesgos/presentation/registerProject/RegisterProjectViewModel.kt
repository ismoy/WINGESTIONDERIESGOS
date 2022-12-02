package com.win.gestionderiesgos.presentation.registerProject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.data.remote.api.WingestionApi
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.domain.repository.registerProject.RegisterProjectRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterProjectViewModel : ViewModel() {
    private val repository: RegisterProjectRepository = RegisterProjectRepository()
    val myResponse: MutableLiveData<Response<Project>> by lazy { MutableLiveData() }
    val listUsers: MutableLiveData<List<Users>> by lazy { MutableLiveData() }
    val responseIdUser: MutableLiveData<String> by lazy { MutableLiveData() }
    val responseIdFusion: MutableLiveData<String> by lazy { MutableLiveData() }
    val responseIdProject: MutableLiveData<String> by lazy { MutableLiveData() }

    fun createProject(project: Project) {
        viewModelScope.launch {
            val response = repository.createProject(project)
            myResponse.value = response
        }
    }

    fun getListUser(): LiveData<List<Users>> {
        viewModelScope.launch {
            repository.getListUsers().observeForever {
                listUsers.value = it
            }
        }
        return listUsers
    }

    fun getIdUserSelected(name: String) {
        viewModelScope.launch {
            repository.getIdUserSelected(name).observeForever {
                responseIdUser.value = it
            }
        }
    }

    fun getIdFusionSelected(name: String) {
        viewModelScope.launch {
            repository.getIdFusionSelected(name).observeForever {
                responseIdFusion.value = it
            }
        }
    }

    fun sendDataInGoogleSheet(
        action: String ,
        id_Project: String ,
        project: String ,
        startDate: String ,
        endDate: String ,
        priority: String ,
        nCaja: String ,
        node: String ,
        contrata: String ,
        percentPlantaExterna: String ,
        percentPlantaInterna: String ,
        percentObracivil: String ,
        percentFusiones: String ,
        user: String ,
        time: String ,
        clickRiskPlantaExterna: String ,
        clickRiskPlantaInterna: String ,
        clickRiskObracivil: String ,
        clickRiskFusiones: String ,
        risk: String ,
        fecha_de_revision: String ,
        observaciones: String
    ) {
        viewModelScope.launch {
            repository.sendDataInGoogleSheet(
                action ,
                id_Project ,
                project ,
                startDate ,
                endDate ,
                priority ,
                nCaja ,
                node ,
                contrata ,
                percentPlantaExterna ,
                percentPlantaInterna ,
                percentObracivil ,
                percentFusiones ,
                user ,
                time ,
                clickRiskPlantaExterna ,
                clickRiskPlantaInterna ,
                clickRiskObracivil ,
                clickRiskFusiones ,
                risk ,
                fecha_de_revision ,
                observaciones
            )
        }
    }

    fun updatePlantaExternaInGoogleSheet(
        action: String ,
        id_Project: String ,
        percentPlantaExterna: String? ,
    ) {
        viewModelScope.launch {
            repository.updatePlantaExternaInGoogleSheet(
                action ,
                id_Project ,
                percentPlantaExterna!!
            )
        }
    }

    fun updatePlantaInternaInGoogleSheet(
        action: String ,
        id_Project: String ,
        percentPlantaInterna: String? ,
    ) {
        viewModelScope.launch {
            repository.updatePlantaInternaInGoogleSheet(
                action ,
                id_Project ,
                percentPlantaInterna!!
            )
        }
    }

    fun updatePlantaObraCivilInGoogleSheet(
        action: String ,
        id_Project: String ,
        percentObracivil: String? ,
    ) {
        viewModelScope.launch {
            repository.updateObraCivilInGoogleSheet(action , id_Project , percentObracivil!!)
        }
    }

    fun updateFusionesInGoogleSheet(
        action: String ,
        id_Project: String ,
        percentFusiones: String? ,
    ) {
        viewModelScope.launch {
            repository.updateFusionesInGoogleSheet(action , id_Project , percentFusiones!!)
        }
    }

    fun updateRiesgoInGoogleSheet(action: String,id_Project: String,detaillRiesgo:String){
        viewModelScope.launch {
            repository.updateRiesgoInGoogleSheet(action,id_Project,detaillRiesgo)
        }

    }

    fun updateClickRiesgoPlantaExternaInGoogleSheet(action: String,id_Project: String,click_riesgo_plantaExterna:String){
        viewModelScope.launch {
            repository.updateClickRiesgoPlantaExternaInGoogleSheet(action,id_Project,click_riesgo_plantaExterna)
        }

    }

    fun updateClickRiesgoPlantaInternaInGoogleSheet(action: String,id_Project: String,click_riesgo_plantaInterna:String){
        viewModelScope.launch {
            repository.updateClickRiesgoPlantaInternaInGoogleSheet(action,id_Project,click_riesgo_plantaInterna)
        }

    }

    fun updateClickRiesgoObraCivilInGoogleSheet(action: String,id_Project: String,click_riesgo_obra_civil:String){
        viewModelScope.launch {
            repository.updateClickRiesgoObraCivilInGoogleSheet(action,id_Project,click_riesgo_obra_civil)
        }

    }

    fun updateClickRiesgoFusionesInGoogleSheet(action: String,id_Project: String,click_riesgo_fusiones:String){
        viewModelScope.launch {
            repository.updateClickRiesgoFusionesInGoogleSheet(action,id_Project,click_riesgo_fusiones)
        }

    }

    fun getIdProject(idKeyProject: String) {
        viewModelScope.launch {
            repository.getIdProject(idKeyProject).observeForever { idProject ->
                responseIdProject.value = idProject
            }
        }
    }
}