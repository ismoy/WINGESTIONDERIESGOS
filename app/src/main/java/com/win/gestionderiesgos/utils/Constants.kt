package com.win.gestionderiesgos.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.view.WindowManager
import java.util.*

object Constants {
    const val BASE_URL ="https://wingestionderiesgos-62b7d-default-rtdb.firebaseio.com/"
    val CURRENTTIME =Calendar.getInstance().time
    const val BASE_URL_FCM ="https://fcm.googleapis.com/"
    const val SERVER_KEY ="AAAABNSu0xE:APA91bFf6HpHuNqSnECY7Po5cCV7Ihvw9EoJ3ESyx3OzoZVqSjTsipQmeN3zGnNivAlK-BqAm_qXIVOCEm_ZpTmuDS8PQAErZ-qxZGxjBv4kLxhUd_WVcxNgkfZenAzTOOScHQ0qoUzN"
    const val CONTENT_TYPE ="application/json"
    const val TITLENOTIFICATION ="NUEVA VENTA"
    val TOLERANCE = 0.9
    val WITDH = 1
    val HEIGHT = 2
    const val URL_GOOGLE_SHEET ="https://script.google.com/macros/s/AKfycbyLbcv5-FAR3XNHirK0UEQibD7htws_uxCks7pYz5ln8B-5COi8xp_cfKI70FlfNvlOeg/"
    const val ACTION ="create"
    const val ACTION_UPDATE_INTERNA ="updateplantainterna"
    const val ACTION_UPDATE_EXTERNA ="updateplantaexterna"
    const val ACTION_UPDATE_OBRA_CIVIL ="updateobracivil"
    const val ACTION_UPDATE_FUSIONES ="updatefusiones"

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

    fun screenDisplay(
        window: WindowManager ,
        orientation: Int = WITDH ,
        tolerance: Double = TOLERANCE
    ): Int {
        val point = Point()
        window.defaultDisplay.getSize(point)

        val size = (if (orientation == HEIGHT) point.y else point.x)
        return ((size * tolerance).toInt())
    }
}