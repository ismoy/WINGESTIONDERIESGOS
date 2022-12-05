package com.win.gestionderiesgos.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class ListOnlyUsersClient {
    var mDatabase: DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Clients")
     fun getListUser():DatabaseReference? {
        return mDatabase
    }

}