package com.mrprogrammer.mrtower.Retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("location/{lat}/{lon}")
    fun getTower(@Path("lat") lat: Double, @Path("lon") lon: Double): Call<ResponseBody>
}
