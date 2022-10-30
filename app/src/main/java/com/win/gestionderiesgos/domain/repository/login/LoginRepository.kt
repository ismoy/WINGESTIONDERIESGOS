package com.win.gestionderiesgos.domain.repository.login

import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response

class LoginRepository {
    var fAuth: FirebaseAuth? = null
    var application = Application()
    private val authProvider = AuthProvider()
    init {
        fAuth = FirebaseAuth.getInstance()
        fAuth?.currentUser
        this.application =application
    }
    suspend fun login(email:String,password:String): Task<AuthResult> {
        return authProvider.login(email, password)
    }
    suspend fun getOnlyUser(uidUser:String): Response<Users> {
        return RetrofitInstance.wingestionApi.getOnlyUser(uidUser)
    }
}