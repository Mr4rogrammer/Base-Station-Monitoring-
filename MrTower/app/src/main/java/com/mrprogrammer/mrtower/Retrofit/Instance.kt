package com.mrprogrammer.mrtower.Retrofit

import com.mrprogrammer.mrtower.Realm.NgrokRealmModel
import com.mrprogrammer.mrtower.Utils.RealmManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Instance {

    companion object{
        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl()!!.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun getBaseUrl(): NgrokRealmModel? {
            val realm = RealmManager.getInstance()
            return RealmManager.getInstance().copyFromRealm(realm.where(NgrokRealmModel::class.java).findFirst())
        }
    }
}