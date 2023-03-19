package com.mrprogrammer.mrtower.Utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mrprogrammer.mrtower.Utils.Const

class LocalFirebase {
    companion object{
        fun getDatabaseReferences() : DatabaseReference{
            return FirebaseDatabase.getInstance().getReference("Tower")
        }
    }
}