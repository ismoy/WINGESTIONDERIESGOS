package com.win.gestionderiesgos.domain.repository.registerActivity

import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.domain.model.Actividad
import retrofit2.Response

class RegisterActivityRepository {
    suspend fun createActivity(actividad: Actividad):Response<Actividad>{
        return RetrofitInstance.wingestionApi.createActivity(actividad)
    }
}