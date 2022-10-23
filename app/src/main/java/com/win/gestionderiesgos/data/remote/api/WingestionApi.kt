package com.win.gestionderiesgos.data.remote.api

import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

/** * Created by ISMOY BELIZAIRE on 23/10/2022. */
interface WingestionApi {

    @PATCH("Clients/{uidUser}.json")
    suspend fun registerClient(@Path("uidUser") uidUser:String , @Body param: Users): Response<Users>

}