package com.win.gestionderiesgos.ui.activity.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.ui.activity.Home.HomeActivity
import com.win.gestionderiesgos.ui.activity.Home.HomeClientActivity
import com.win.gestionderiesgos.ui.activity.MainActivity
import com.win.gestionderiesgos.utils.Constants

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    var mAuthProvider:AuthProvider?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthProvider = AuthProvider()
        checkUserExist()
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    }

    private fun checkUserExist() {
        if (mAuthProvider?.existSession() ==true) startTimeHome() else startTime()
    }

    private fun startTime() {
        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent= Intent(applicationContext,MainActivity::class.java).apply {  }
                startActivity(intent)
                finish()
            }

        }.start()
    }

    private fun startTimeHome() {
        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                if (Constants.getValueSharedPreferences(this@SplashScreenActivity,"roleAdmin") == "Administrador"){
                    val intent =Intent(applicationContext,HomeActivity::class.java).apply {  }
                    startActivity(intent)
                }else if (Constants.getValueSharedPreferences(this@SplashScreenActivity,"roleClient") == "Client"){
                    val intent =Intent(applicationContext,HomeClientActivity::class.java).apply {  }
                    startActivity(intent)
                }
                else{
                    startActivity(Intent(applicationContext,MainActivity::class.java)).apply {  }
                }
            }

        }.start()
    }
}