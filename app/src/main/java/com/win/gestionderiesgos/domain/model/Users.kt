package com.win.gestionderiesgos.domain.model

data class Users(val idUser:String,val email:String,val completeName:String,val userName:String,val password:String,val role:Int){
    override fun toString(): String {
        return "$idUser"
    }
}

