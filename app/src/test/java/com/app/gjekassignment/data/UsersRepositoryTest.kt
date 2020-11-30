package com.app.gjekassignment.data

import com.app.gjekassignment.utils.RxImmediateSchedulerRule
import com.app.gjekassignment.api.PeopleRandomService
import com.app.gjekassignment.api.RandomPeopleResponse
import com.app.gjekassignment.utils.TestUtils
import com.app.gjekassignment.utils.loadJson
import com.app.gjekassignment.utils.mock
import com.app.gjekassignment.utils.toTypeOf
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import retrofit2.HttpException
import retrofit2.Response

@RunWith(JUnit4::class)
class UsersRepositoryTest {
   @Rule
   @JvmField var testSchedulerRule =
      RxImmediateSchedulerRule()
   
   private val peopleRandomService: PeopleRandomService = mock()
   private val userDao: UserDao = mock()
   private val usersRepository = UsersRepository(peopleRandomService, userDao)
   
   @Test
   fun getUsers() {
      val sampleResponse = loadJson("random-people-1.json").toTypeOf<RandomPeopleResponse>()
      doReturn(Single.just(sampleResponse)).`when`(peopleRandomService).getRandomPeople()
      
      val testObserverUsers = usersRepository.getUsers().test()
      
      verify(peopleRandomService, only()).getRandomPeople()
      testObserverUsers.assertResult(Result.Success(sampleResponse.results.map { it.user }))
   }
   
   @Test
   fun getUsers_whenNetworkFailed() {
      val response = Response.error<RandomPeopleResponse>(404, "".toResponseBody("application/json".toMediaTypeOrNull()))
      doReturn(Single.error<RandomPeopleResponse>(HttpException(response))).`when`(peopleRandomService).getRandomPeople()
      
      val testObserverUsers = usersRepository.getUsers().test()
      
      verify(peopleRandomService, only()).getRandomPeople()
      testObserverUsers.assertResult(Result.Failure(Error.NotFound))
   }
   
   @Test
   fun addLikedUsers() {
      val user = TestUtils.createUser()
      doReturn(listOf(0L)).`when`(userDao).insertLikedUsers(user)
      
      val testObserverUsers = usersRepository.addLikedUsers(user).test()
      
      verify(userDao, only()).insertLikedUsers(user)
      testObserverUsers.assertComplete()
   }
   
   @Test
   fun getLikedUsers() {
      val users = listOf(TestUtils.createUser())
      doReturn(users).`when`(userDao).getLikedUsers()
   
      val testObserverUsers = usersRepository.getLikedUsers().test()
   
      verify(userDao, only()).getLikedUsers()
      testObserverUsers.assertResult(Result.Success(users))
   }
}