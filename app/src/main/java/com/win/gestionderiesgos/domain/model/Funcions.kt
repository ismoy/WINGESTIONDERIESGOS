package com.win.gestionderiesgos.domain.model

data class Funcions(
    val name: String ,
    val idUser: String ,
    val dateCreated: String ,
    val QuantityPercent: String ,
    val idProject: String ,
    val nameProject: String ,
    val idKeyFusion: String ,
    val status: String,
    val status_idKeyProject:String
) {
    override fun toString(): String {
        return name
    }
}

