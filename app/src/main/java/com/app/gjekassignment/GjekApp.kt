package com.app.gjekassignment

import android.app.Application
import androidx.room.Room
import com.app.gjekassignment.api.PeopleRandomService
import com.app.gjekassignment.data.UsersRepository
import com.google.gson.Gson

class GjekApp : Application() {
   
   private val appDatabase by lazy {
      Room.databaseBuilder(applicationContext, AppDatabase::class.java, "gjek-db").build()
   }
   
   val usersRepository by lazy {
      UsersRepository(PeopleRandomService.INSTANCE, appDatabase.userDao())
   }
   
   companion object {
      val gson by lazy { Gson() }
   }
}