package com.win.gestionderiesgos.data.remote.api

import com.win.gestionderiesgos.domain.model.Actividad
import com.win.gestionderiesgos.domain.model.Funcions
import com.win.gestionderiesgos.domain.model.Risk
import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response
import retrofit2.http.*

/** * Created by ISMOY BELIZAIRE on 23/10/2022. */
interface WingestionApi {

    @PATCH("Clients/{uidUser}.json")
    suspend fun registerClient(@Path("uidUser") uidUser:String , @Body param: Users): Response<Users>

    @GET("Clients/{uidUser}.json")
    suspend fun getOnlyUser(@Path("uidUser") uidUser:String):Response<Users>

    @POST("Admin/Funcions.json")
    suspend fun registerFuncions(@Body funcions: Funcions): Response<Funcions>

    @POST("Admin/Actividad.json")
    suspend fun createActivity(@Body actividad: Actividad):Response<Actividad>
    @POST("Admin/Riesgo.json")
    suspend fun createRisk(@Body risk: Risk):Response<Risk>
}