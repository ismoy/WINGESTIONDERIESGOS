package com.win.gestionderiesgos.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.win.gestionderiesgos.channel.ChannelIdNotification.CHANNEL_ID
import com.win.gestionderiesgos.channel.ChannelIdNotification.CHANNEL_NAME
import com.win.gestionderiesgos.R
import kotlin.random.Random

/** * Created by ISMOY BELIZAIRE on 12/11/2022. */
class FirebaseService: FirebaseMessagingService() {
 override fun onNewToken(token: String) {
  super.onNewToken(token)
 }
 override fun onMessageReceived(message: RemoteMessage) {
  super.onMessageReceived(message)
  val intent = Intent()
  val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
  val notificationID = Random.nextInt()
  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
   createNotificationChannel(notificationManager)
  }
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE)
  val notification =NotificationCompat.Builder(this,CHANNEL_ID)
   .setContentTitle(message.data["title"])
   .setContentText(message.data["message"])
   .setSmallIcon(R.drawable.ic_launcher_background)
   .setAutoCancel(true)
   .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
   .setStyle(NotificationCompat.BigTextStyle().bigText(message.data["message"]).setBigContentTitle(message.data["title"]))
   .setContentIntent(pendingIntent)
   .build()
  notificationManager.notify(notificationID,notification)
 }
 @RequiresApi(Build.VERSION_CODES.O)
 private fun createNotificationChannel(notificationManager: NotificationManager){
  val notificatiochannel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
   enableLights(true)
   enableVibration(true)
   lightColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
   lockscreenVisibility = Notification.VISIBILITY_PRIVATE
  }
  notificationManager.createNotificationChannel(notificatiochannel)
 }
}