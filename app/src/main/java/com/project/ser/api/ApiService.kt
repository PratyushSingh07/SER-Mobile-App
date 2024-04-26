package com.project.ser.api

import com.google.gson.GsonBuilder
import com.project.ser.model.EmotionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload_audio")
    fun uploadAudio(@Part file: MultipartBody.Part): Call<EmotionResponse>
}

object RetrofitClient {

    var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.134.64:5000/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}