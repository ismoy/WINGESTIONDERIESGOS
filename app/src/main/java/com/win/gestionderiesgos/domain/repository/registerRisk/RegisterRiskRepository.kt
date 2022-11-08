package com.win.gestionderiesgos.domain.repository.registerRisk

import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Risk
import retrofit2.Response

class RegisterRiskRepository {
    suspend fun createRisk(risk: Risk):Response<Risk>{
        return RetrofitInstance.wingestionApi.createRisk(risk)
    }
}