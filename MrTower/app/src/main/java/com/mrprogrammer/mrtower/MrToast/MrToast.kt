package com.mrprogrammer.shop.MrToast

import android.app.Activity
import android.widget.Toast
import com.mrprogrammer.mrtower.R

class MrToast{
    companion object{
        fun success(activity: Activity, message: String) {
            HeadToast(activity, message, Toast.LENGTH_SHORT, R.drawable.checked,R.color.success_left,R.color.success)
        }



        fun warning(activity: Activity, message: String) {
            HeadToast(activity, message, Toast.LENGTH_SHORT,R.drawable.alert,R.color.warning_left,R.color.warning)
        }



        fun information(activity: Activity, message: String) {
            HeadToast(activity, message, Toast.LENGTH_SHORT, R.drawable.information,R.color.information_left,R.color.information)
        }


        fun error(activity: Activity, message: String) {
            HeadToast(activity, message, Toast.LENGTH_SHORT, R.drawable.error,R.color.error_left,R.color.error)
        }

    }
}