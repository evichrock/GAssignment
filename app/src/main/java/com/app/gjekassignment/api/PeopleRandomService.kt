package com.app.gjekassignment.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface PeopleRandomService {
    
    @GET("api/0.4/?randomapi")
    fun getRandomPeople(): Single<RandomPeopleResponse>
}