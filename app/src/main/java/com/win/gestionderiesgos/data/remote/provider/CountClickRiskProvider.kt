package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.win.gestionderiesgos.domain.model.CountClickRisk

class CountClickRiskProvider {
    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("CountClickRisk")

    fun createSnapshot(countClickRisk: CountClickRisk): Task<Void> {
       return mDatabase.setValue(countClickRisk)
    }
    fun getCountClickRisk(): DatabaseReference {
        return mDatabase
    }
}