package com.win.gestionderiesgos.domain.repository.register

import com.win.gestionderiesgos.data.remote.api.RetrofitInstance
import com.win.gestionderiesgos.domain.model.Users
import retrofit2.Response

class RegisterRepository {
    suspend fun register(uidUser: String?, users: Users): Response<Users> {
        return RetrofitInstance.wingestionApi.registerClient(uidUser!!, users)
    }
}