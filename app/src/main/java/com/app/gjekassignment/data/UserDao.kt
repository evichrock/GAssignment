package com.app.gjekassignment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {
   
   @Query("SELECT * FROM users")
   fun getLikedUsers(): List<User>
   
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertLikedUsers(vararg users: User): List<Long>
}