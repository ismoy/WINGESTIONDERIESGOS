package com.win.gestionderiesgos.domain.repository.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.remote.api.RetrofitInstanceFCM
import com.win.gestionderiesgos.data.remote.provider.GetDetailsFusionActivityProvider
import com.win.gestionderiesgos.domain.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response

class NotificationRepository {

    private val getDetailsFusionActivity by lazy { GetDetailsFusionActivityProvider() }

    suspend fun sendNotification(notification: PushNotification): Response<ResponseBody> {
        return RetrofitInstanceFCM.notificationAPI.postNotification(notification)
    }

}