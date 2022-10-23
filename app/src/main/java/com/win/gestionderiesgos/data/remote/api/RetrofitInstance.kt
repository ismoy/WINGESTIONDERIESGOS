package com.win.gestionderiesgos.data.remote.api

import com.win.gestionderiesgos.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** * Created by ISMOY BELIZAIRE on 23/10/2022. */
object RetrofitInstance {
 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(BASE_URL)
   .addConverterFactory(GsonConverterFactory.create())
   .build()
 }
 val wingestionApi:WingestionApi by lazy {
  retrofit.create(WingestionApi::class.java)
 }
}