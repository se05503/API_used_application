package com.example.assignment_hard.type1

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {
    private const val IMAGE_BASE_URL = "https://dapi.kakao.com/"

    private fun createOkHttpClient():OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        // BuildConfig는 import가 잘 안되어서 일단 if else 문은 넣지 않음

        return OkHttpClient.Builder()
            .connectTimeout(20,TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .writeTimeout(20,TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val imageRetrofit = Retrofit.Builder()
        .baseUrl(IMAGE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    val imageNetwork : NetWorkInterface = imageRetrofit.create(NetWorkInterface::class.java)
}