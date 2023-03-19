package com.mrprogrammer.mrtower.Firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.mrprogrammer.mrtower.Firebase.FirebaseModel.FirebaseKeyModel
import com.mrprogrammer.mrtower.Realm.NgrokRealmModel
import com.mrprogrammer.mrtower.Utils.LocalFirebase
import com.mrprogrammer.mrtower.Utils.RealmManager

class SyncKey {
    companion object{
        fun syncKey(){
            val ref = LocalFirebase.getDatabaseReferences().child("key")
            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                        val Rdata = NgrokRealmModel("id",snapshot.value.toString())
                        saveUrlLocally(Rdata)

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val Rdata = NgrokRealmModel("id",snapshot.value.toString())
                    saveUrlLocally(Rdata)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        private fun saveUrlLocally(ngrok: NgrokRealmModel){
            val realm = RealmManager.getInstance()
            realm.executeTransaction {
                it.copyToRealmOrUpdate(ngrok)
            }
        }

    }
}