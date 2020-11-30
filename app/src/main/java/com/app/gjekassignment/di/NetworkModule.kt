package com.app.gjekassignment.di

import com.app.gjekassignment.BuildConfig
import com.app.gjekassignment.api.PeopleRandomService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {
   
   @Provides
   fun provideOkHttpClient(): OkHttpClient {
      val loggingInterceptor = HttpLoggingInterceptor().apply {
         level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
         else
            HttpLoggingInterceptor.Level.NONE
      }
      return OkHttpClient.Builder()
         .addInterceptor(loggingInterceptor)
         .build()
   }
   
   @Provides
   fun provideGsonConverterFactory(): GsonConverterFactory {
      return GsonConverterFactory.create(GsonBuilder()
         .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
         .create())
   }
   
   @ApplicationScope
   @Provides
   fun providePeopleRandomService(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): PeopleRandomService {
      return Retrofit.Builder().client(okHttpClient)
         .addConverterFactory(gsonConverterFactory)
         .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
         .baseUrl("https://randomuser.me/")
         .build()
         .create(PeopleRandomService::class.java)
   }
}