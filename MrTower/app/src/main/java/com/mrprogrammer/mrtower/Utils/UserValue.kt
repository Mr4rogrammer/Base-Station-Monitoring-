package com.mrprogrammer.shop.Utils

import android.content.Context
import com.mrprogrammer.mrtower.Utils.LocalSharedPreferences

class UserValue {
    companion object{
        fun getUserName(context: Context):String{
           return LocalSharedPreferences.getLocalSavedUser(context)?.get(0).toString()
        }

        fun getUserEmail(context: Context):String{
            return LocalSharedPreferences.getLocalSavedUser(context)?.get(1).toString()
        }

        fun getUserImageUrl(context: Context):String{
            return LocalSharedPreferences.getLocalSavedUser(context)?.get(2).toString()
        }
    }
}