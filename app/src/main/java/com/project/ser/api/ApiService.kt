package com.project.ser.api

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
    private const val BASE_URL = "http://192.168.39.64:5000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}