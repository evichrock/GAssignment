package com.app.gjekassignment.api

import com.app.gjekassignment.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface PeopleRandomService {
    
    @GET("api/0.4/?randomapi")
    fun getRandomPeople(): Single<RandomPeopleResponse>

    companion object {
        val INSTANCE: PeopleRandomService by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
    
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    
            val baseUrl = "https://randomuser.me/"
            
            Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(createGsonObject()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(PeopleRandomService::class.java)
        }
    
        fun createGsonObject() = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}