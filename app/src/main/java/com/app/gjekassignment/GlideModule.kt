package com.app.gjekassignment

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class GlideModule : AppGlideModule() {
   
   override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
      val loggingInterceptor = HttpLoggingInterceptor().apply {
         level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
         else
            HttpLoggingInterceptor.Level.NONE
      }
      val client = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS)
         .connectTimeout(10, TimeUnit.SECONDS)
         .addInterceptor(loggingInterceptor)
         .build()
      
      registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
   }
}