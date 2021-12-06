package com.example.skgcode_teamb.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://skgcode8-teamb-backend.herokuapp.com/"

    val getRetrofitInstance: ApiInterface
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                // We add converter factory to convert JSON object to Java object
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

}