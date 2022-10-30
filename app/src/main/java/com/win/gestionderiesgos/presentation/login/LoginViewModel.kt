package com.win.gestionderiesgos.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.domain.repository.login.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel:ViewModel() {
    private val repository:LoginRepository = LoginRepository()
    val responseLoginRepository: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    val responseUsers:MutableLiveData<Response<Users>> = MutableLiveData()


    @SuppressLint("NullSafeMutableLiveData")
    fun login(email:String,password:String){
        viewModelScope.launch {
            val response = repository.login(email, password)
            responseLoginRepository.value = response
        }
    }

    fun getOnlyUser(uidUser:String){
        viewModelScope.launch {
            val response = repository.getOnlyUser(uidUser)
            responseUsers.value = response
        }
    }
}