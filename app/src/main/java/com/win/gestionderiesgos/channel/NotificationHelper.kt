package com.win.gestionderiesgos.channel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.channel.ChannelIdNotification.CHANNEL_ID
import com.win.gestionderiesgos.channel.ChannelIdNotification.CHANNEL_NAME

/** * Created by ISMOY BELIZAIRE on 12/11/2022. */
class NotificationHelper(base: Context?) : ContextWrapper(base){
    private var manager :NotificationManager?=null
    init {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel()
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel() {
     val notificatiochannel = NotificationChannel(
         CHANNEL_ID,
         CHANNEL_NAME,
         NotificationManager.IMPORTANCE_HIGH

         )
        notificatiochannel.enableLights(true)
        notificatiochannel.enableVibration(true)
        notificatiochannel.lightColor =ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        notificatiochannel.lockscreenVisibility =Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(notificatiochannel)
    }

    fun getManager(): NotificationManager {
      if (manager==null){
          manager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager

      }
        return manager as NotificationManager
    }
    fun getNotification(title:String,body:String):NotificationCompat.Builder{
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setColor(Color.GRAY)
            .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title))

    }
}
