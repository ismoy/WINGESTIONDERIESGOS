package com.win.gestionderiesgos.domain.model

data class Risk(
    val fusionName: String ,
    val risk: String ,
    val idUser: String ,
    val dateCreated: String ,
    val idKeyActivity: String ,
    val idKeyFusion: String ,
    val timeRisk: String ,
    val status: String,
    val idKeyProject: String ,
    val projectName: String
)
data class RiskByUser(
    val fusionName: String ,
    val riesgoPredetermido: String ,
    val riesgoNoIndificado: String ,
    val idUser: String ,
    val dateCreated: String ,
    val idKeyActivity: String ,
    val idKeyFusion: String ,
    val timeRisk: String ,
    val status: String,
    val idKeyProject: String ,
    val projectName: String,
    val nameUser:String,
    val idUserNameFusion:String
)