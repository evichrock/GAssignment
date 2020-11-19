package com.app.gjekassignment.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.gjekassignment.data.Name
import com.app.gjekassignment.utils.TestUtils
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : DbTest() {
   
   @Test
   fun getWithoutInsert() {
      val users = appDb.userDao().getLikedUsers()
      assertThat(users, notNullValue())
      assertThat(users.size, `is`(0))
   }
   
   @Test
   fun insertAndGet() {
      val user = TestUtils.createUser(1, Name("Catarina", "Devon"), "devon@gmail.com")
      appDb.userDao().insertLikedUsers(user)
   
      var users = appDb.userDao().getLikedUsers()
      assertThat(users, notNullValue())
      assertThat(users, hasItem(user))
      
      val user2 = TestUtils.createUser(2, Name("Charlotte", "Katakuri"), "katakuri@gmail.com")
      appDb.userDao().insertLikedUsers(user2)
      users = appDb.userDao().getLikedUsers()
   
      assertThat(users, hasItem(user2))
      assertThat(users.size, `is`(2))
      assertThat(users[0].id, not(users[1].id))
   }
}