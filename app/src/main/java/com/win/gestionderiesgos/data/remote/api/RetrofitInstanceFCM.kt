package com.win.gestionderiesgos.data.remote.api

import com.win.gestionderiesgos.utils.Constants.BASE_URL_FCM
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceFCM {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FCM)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val notificationAPI by lazy {
        retrofit.create(WingestionApi::class.java)
    }
}