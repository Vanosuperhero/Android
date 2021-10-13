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


interface MyApiService{
    @GET("{filter}/{page}?json=true")
    fun getProperties(@Path("filter") filter: String, @Path("page") page: Int):
            Call<MyResult>
//    fun getRandom(@Path("filter",) filter: String, @Path("page") page: String):
//            Call<MyProperty>
}

interface MyApiRandomService{
    @GET("random?json=true")
    fun getProperties():
            Call<MyProperty>
}

object MyApi{
    val retrofitService: MyApiService by lazy {
        retrofit.create(MyApiService::class.java)
    }
}

object MyApiRandom{
    val retrofitService: MyApiRandomService by lazy {
        retrofit.create(MyApiRandomService::class.java)
    }
}