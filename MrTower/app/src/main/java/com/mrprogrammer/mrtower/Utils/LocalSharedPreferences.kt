package com.mrprogrammer.mrtower.Utils

import android.content.Context
import androidx.core.content.edit
import com.mrprogrammer.mrtower.Utils.Const

class LocalSharedPreferences {

    companion object{
        fun saveAppTourState(context:Context,state:Boolean){
            val sharedPref = context.getSharedPreferences(Const.appTourSP, Context.MODE_PRIVATE)
            sharedPref.edit {
                putBoolean("state", state)
                apply()
            }
        }

        fun getAppTourState(context: Context):Boolean{
            val sharedPref = context.getSharedPreferences(Const.appTourSP, Context.MODE_PRIVATE)
            return sharedPref.getBoolean("state", false)
        }

        fun saveUserLocally(context: Context, name: String?, email: String?, imageUrl: String?) {
            val sharedPreferences = context.getSharedPreferences(Const.LocalDbName, Context.MODE_PRIVATE)
            val localDb = sharedPreferences.edit()
            localDb.putString(Const.LocalDbNameValueName, name)
            localDb.putString(Const.LocalDbNameValueEmail, email)
            localDb.putString(Const.LocalDbNameValueImage, imageUrl)
            localDb.apply()
        }

        fun getLocalSavedUser(context: Context): List<String?>? {
            val LocalUserData = ArrayList<String?>()
            val sharedPreferences =
                context.getSharedPreferences(Const.LocalDbName, Context.MODE_PRIVATE)
            val name = sharedPreferences.getString(Const.LocalDbNameValueName, "")
            val email = sharedPreferences.getString(Const.LocalDbNameValueEmail, "")
            val imageUrl = sharedPreferences.getString(Const.LocalDbNameValueImage, "")
            LocalUserData.add(name)
            LocalUserData.add(email)
            LocalUserData.add(imageUrl)
            return LocalUserData
        }
    }
}