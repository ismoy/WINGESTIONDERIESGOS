package com.win.gestionderiesgos.presentation.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.win.gestionderiesgos.domain.model.Users
import com.win.gestionderiesgos.domain.repository.register.RegisterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel:ViewModel() {
    private val registerRepository:RegisterRepository = RegisterRepository()
    val responseRegister: MutableLiveData<Response<Users>> = MutableLiveData()
    val responseRegisterAdmin: MutableLiveData<Response<Users>> = MutableLiveData()

    fun register(uidUser:String,users: Users){
        viewModelScope.launch {
           val response = registerRepository.register(uidUser, users)
            responseRegister.value =response
        }
    }

    fun registerAdmin(uidUser:String,users: Users){
        viewModelScope.launch {
            val response = registerRepository.registerAdmin(uidUser, users)
            responseRegisterAdmin.value =response
        }
    }
}