package com.win.gestionderiesgos.data.remote.api

import com.win.gestionderiesgos.domain.model.*
import com.win.gestionderiesgos.utils.Constants.CONTENT_TYPE
import com.win.gestionderiesgos.utils.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/** * Created by ISMOY BELIZAIRE on 23/10/2022. */
interface WingestionApi {

    @PATCH("Clients/{uidUser}.json")
    suspend fun registerClient(@Path("uidUser") uidUser:String , @Body param: Users): Response<Users>
    @PATCH("Admin/{uidUser}.json")
    suspend fun registerAdmin(@Path("uidUser") uidUser:String , @Body param: Users): Response<Users>

    @GET("Clients/{uidUser}.json")
    suspend fun getOnlyUser(@Path("uidUser") uidUser:String):Response<Users>

    @POST("Fusion.json")
    suspend fun registerFuncions(@Body funcions:Funcions): Response<Funcions>

    @POST("Actividad.json")
    suspend fun createActivity(@Body actividad: Actividad):Response<Actividad>

    @POST("Riesgo.json")
    suspend fun createRisk(@Body risk: Risk):Response<Risk>

    @POST("RiesgoUsuario.json")
    suspend fun createRiskByUser(@Body risk: Risk):Response<Risk>

    @POST("Admin/Proyectos.json")
    suspend fun createProject(@Body project: Project):Response<Project>

    @POST("OnClickRisk.json")
    suspend fun onClickRisk(@Body onclickRisk: OnclickRisk):Response<OnclickRisk>
    @Headers(
        "Content-Type:$CONTENT_TYPE",
        "Authorization:key=$SERVER_KEY")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ):Response<ResponseBody>

    @GET("exec")
    suspend fun sendDataInGoogleSheet(
        @Query("action")action:String,
        @Query("id_Project") id_Project: String,
        @Query("project") project: String,
        @Query("startDate") startDate: String,
        @Query("endDate")endDate:String,
        @Query("priority") priority: String,
        @Query("nCaja") nCaja: String,
        @Query("node") node: String,
        @Query("contrata")contrata:String,
        @Query("percentPlantaExterna")percentPlantaExterna:String,
        @Query("percentPlantaInterna") percentPlantaInterna: String,
        @Query("percentObracivil") percentObracivil: String,
        @Query("percentFusiones") percentFusiones: String,
        @Query("user")user:String,
        @Query("time") time: String,
        @Query("clickRiskPlantaExterna") clickRiskPlantaExterna: String,
        @Query("clickRiskPlantaInterna") clickRiskPlantaInterna: String,
        @Query("clickRiskObracivil")clickRiskObracivil:String,
        @Query("clickRiskFusiones")clickRiskFusiones:String,
        @Query("risk") risk: String,
        @Query("fecha_de_revision") fecha_de_revision: String,
        @Query("observaciones") observaciones: String,
    )


    @GET("exec")
    suspend fun updatePlantaExternaInGoogleSheet(
        @Query("action")action:String,
        @Query("id_Project") id_Project: String,
        @Query("percentPlantaExterna")percentPlantaExterna:String
    )

    @GET("exec")
    suspend fun updatePlantaInternaInGoogleSheet(
        @Query("action")action:String,
        @Query("id_Project") id_Project: String,
        @Query("percentPlantaInterna")percentPlantaInterna:String
    )

    @GET("exec")
    suspend fun updateObraCivilInGoogleSheet(
        @Query("action")action:String,
        @Query("id_Project") id_Project: String,
        @Query("percentObracivil")percentObracivil:String
    )

    @GET("exec")
    suspend fun updateFusionesInGoogleSheet(
        @Query("action")action:String,
        @Query("id_Project") id_Project: String,
        @Query("percentFusiones")percentFusiones:String
    )
}