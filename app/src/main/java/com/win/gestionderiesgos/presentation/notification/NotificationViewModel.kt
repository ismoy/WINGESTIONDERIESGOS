package com.win.gestionderiesgos.presentation.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.PushNotification
import com.win.gestionderiesgos.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class NotificationViewModel:ViewModel() {
    private val repository: NotificationRepository = NotificationRepository()
    val responseNotification:MutableLiveData<Response<ResponseBody>> by lazy { MutableLiveData() }


    fun sendNotification(notification: PushNotification){
        viewModelScope.launch {
            val response =repository.sendNotification(notification)
            responseNotification.value =response
        }
    }

}