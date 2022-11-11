package com.win.gestionderiesgos.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*

object Constants {
    const val BASE_URL ="https://wingestionderiesgos-62b7d-default-rtdb.firebaseio.com/"
    val CURRENTTIME =Calendar.getInstance().time
    var ROLE =""

    fun getValueSharedPreferences(activity: Activity , value: String): String {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(value, "").toString()
    }

    fun setValueSharedPreferences(activity: Activity , key: String , value: String){
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }
}