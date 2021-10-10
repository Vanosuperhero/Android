package com.example.test

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://developerslife.ru/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//enum class MyApiFilter(val value: String) {SHOW_R("rent"),SHOW_BUY("buy"),SHOW_ALL("all") }

interface MyApiService{
    @GET("latest/0?json=true")
//    {page}
//    ,@Path("page") type: Int
    //@Path("id") type: String
    fun getProperties():
            Call<MyResult>
}

object MyApi{
    val retrofitService: MyApiService by lazy {
        retrofit.create(MyApiService::class.java)
    }
}