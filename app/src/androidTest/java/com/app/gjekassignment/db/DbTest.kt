package com.app.gjekassignment.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.gjekassignment.AppDatabase
import org.junit.After
import org.junit.Before

abstract class DbTest {

   private lateinit var _appDb: AppDatabase
   val appDb get() = _appDb
   
   @Before
   fun init() {
      _appDb = Room.inMemoryDatabaseBuilder(
         ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
   }
   
   @After
   fun closeDb() {
      _appDb.close()
   }
}