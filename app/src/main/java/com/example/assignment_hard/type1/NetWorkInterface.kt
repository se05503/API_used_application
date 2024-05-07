package com.example.assignment_hard.type1

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NetWorkInterface {
    @GET("v2/search/image")
    suspend fun getImage(@QueryMap param: HashMap<String,String>):Image
}