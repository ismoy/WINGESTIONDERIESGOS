package com.win.gestionderiesgos.domain.model

data class Funcions(val name:String,val idUser:String,val dateCreated:String,val QuantityPercent:String) {
    override fun toString(): String {
        return name
    }
}

