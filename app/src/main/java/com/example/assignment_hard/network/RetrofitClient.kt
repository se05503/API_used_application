package com.example.assignment_hard.network

import com.example.assignment_hard.data.Constants
import com.example.assignment_hard.data.SearchRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
객체를 매번 생성하면 high cost 이다. → 싱글톤으로 만든 이유
 */

object RetrofitClient {

    // 객체 초기화 및 생성을 했다. 동시에 retrofit에 대한 인스턴스를 생성하고 데이터를 파싱해줬다.
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // retrofit : SearchRemoteDataSource에 API 호출을 수행할 수 있게 해줌
    val searchImageRetrofit: SearchRemoteDataSource by lazy {
        retrofit.create(SearchRemoteDataSource::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        // Level를 BODY로 설정하여 네트워크 요청, 응답등 모든 내용 로그에 포함 시킴
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        // 네트워크가 20초내로 요청 되지않을 시 TimeOut
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // connect = 불러오기
            .readTimeout(20,TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }
}
/*
private val createOkHttpClient by lazy {
    OkHttpClient.Builder()
    .build()
}
 */