package com.mrprogrammer.mrtower.Realm

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mrprogrammer.mrtower.Utils.RealmManager
import io.realm.RealmList

class SaveLocation {
    companion object{
        fun saveLocationData(json:String) {
            val gson = Gson()
            val towerListType = object : TypeToken<List<Tower>>() {}.type
            val towerList: List<Tower> = gson.fromJson(json, towerListType)
            val towerRealmList = RealmList<Tower>()
            towerRealmList.addAll(towerList)

            var realm = RealmManager.getInstance()
            realm.executeTransaction {
                realm.insertOrUpdate(towerRealmList)
            }
        }
    }
}