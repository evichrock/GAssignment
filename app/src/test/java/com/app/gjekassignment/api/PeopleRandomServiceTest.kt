package com.app.gjekassignment.api

import com.app.gjekassignment.utils.enqueueResponse
import com.app.gjekassignment.utils.loadJson
import com.app.gjekassignment.utils.toTypeOf
import com.google.gson.Gson
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class PeopleRandomServiceTest {
   
   private lateinit var service: PeopleRandomService
   private lateinit var mockWebServer: MockWebServer
   private val gson: Gson = PeopleRandomService.createGsonObject()
   
   @Before
   fun initService() {
      mockWebServer = MockWebServer()
   
      service = Retrofit.Builder()
         .baseUrl(mockWebServer.url("/"))
         .addConverterFactory(GsonConverterFactory.create(gson))
         .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
         .build()
         .create(PeopleRandomService::class.java)
   }
   
   @After
   fun stopService() {
      mockWebServer.shutdown()
   }
   
   @Test
   fun getUsers() {
      val sampleBody = loadJson("random-people-1.json")
      mockWebServer.enqueueResponse(sampleBody)
      
      val expectedResponse = sampleBody.toTypeOf<RandomPeopleResponse>()
      
      val testObserverRandomPeopleResponse = service.getRandomPeople().test()
      testObserverRandomPeopleResponse.await()
      testObserverRandomPeopleResponse.assertResult(expectedResponse)
      
      val randomPeopleResponse = testObserverRandomPeopleResponse.values()[0]
      assertFalse(randomPeopleResponse.results.isNullOrEmpty())
      assertThat(randomPeopleResponse.results[0], notNullValue())

      val user = randomPeopleResponse.results[0].user
      assertThat(user, notNullValue())
      assertThat(user.name, notNullValue())
      assertFalse(user.name.first.isNullOrEmpty())
      assertFalse(user.name.last.isNullOrEmpty())
      
      assertThat(user.location, notNullValue())
      assertFalse(user.location.street.isNullOrEmpty())
      assertFalse(user.location.city.isNullOrEmpty())
      
      assertThat(user.email, notNullValue())
      assertThat(user.password, notNullValue())
      assertThat(user.dob, notNullValue())
      assertThat(user.picture, notNullValue())
      assertThat(user.password, notNullValue())
   }
}