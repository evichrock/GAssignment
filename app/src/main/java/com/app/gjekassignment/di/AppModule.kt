package com.app.gjekassignment.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.app.gjekassignment.AppDatabase
import com.app.gjekassignment.data.UserDao
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
object AppModule {
   
   @ApplicationScope
   @Provides
   fun provideAppDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(context.applicationContext,
         AppDatabase::class.java, "gjek-db").build()
   }
   
   @ApplicationScope
   @Provides
   fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
   
   @ApplicationScope
   @Provides
   fun provideGson() = Gson()
}