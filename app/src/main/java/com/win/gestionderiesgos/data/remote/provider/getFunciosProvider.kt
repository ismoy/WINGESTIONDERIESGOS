package com.win.gestionderiesgos.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

 class getFunciosProvider {
    var mDatabase:DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Admin").child("Funcions")

     fun getFuncions(): DatabaseReference? {
        return mDatabase
    }
}