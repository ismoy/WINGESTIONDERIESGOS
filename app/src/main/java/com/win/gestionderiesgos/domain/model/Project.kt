package com.win.gestionderiesgos.domain.model

data class Project(
    val idProject:String,
    val name: String ,
    val initialDate: String ,
    val endDate: String ,
    val idUserAsign: String ,
    val idAdmin: String ,
    val dateCreated: String ,
    val QuantityPercent: String ,
    val idKeyProject: String ,
    val status: String
) {
    override fun toString(): String {
        return name
    }
}