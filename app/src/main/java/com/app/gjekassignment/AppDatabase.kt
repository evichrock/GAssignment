package com.app.gjekassignment

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
   
   abstract fun userDao(): UserDao
}