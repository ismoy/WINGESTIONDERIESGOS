package com.win.gestionderiesgos.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProjectListProvider {
    var mDatabase: DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Admin").child("Proyectos")
    suspend fun getProject(): DatabaseReference? {
        return mDatabase
    }
}