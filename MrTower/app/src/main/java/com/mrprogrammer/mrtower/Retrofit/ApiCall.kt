package com.mrprogrammer.mrtower.Retrofit

import com.google.gson.Gson
import com.mrprogrammer.mrtower.Interface.CompleteHandler
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ApiCall {
    companion object{
        fun getValue(lat:Double,lon:Double,completion: CompleteHandler){
            val gson = Gson()
            val myApiInstance = Instance.getInstance().create(API::class.java)


            myApiInstance.getTower(lat,lon).enqueue(object : retrofit2.Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val myResponse = response.body()?.string()
                        completion.onSuccess(myResponse)
                    } else {
                        completion.onFailure("Error")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    completion.onFailure(t.toString())
                }
            })
        }
    }
}