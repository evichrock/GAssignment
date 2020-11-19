package com.app.gjekassignment.data

import android.util.Log
import com.app.gjekassignment.OpenForTesting
import com.app.gjekassignment.api.PeopleRandomService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

@OpenForTesting
class UsersRepository(private val peopleRandomService: PeopleRandomService,
                      private val userDao: UserDao) {
   
   private val apiErrorHandler by lazy { ApiErrorHandler() }
   
   fun getUsers(): Single<Result<List<User>>> {
      return peopleRandomService.getRandomPeople()
         .flattenAsObservable { it.results.map { userResponse -> userResponse.user } }
         .toList()
         .toResult(apiErrorHandler)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
   }
   
   fun addLikedUsers(vararg users: User): Single<Result<List<Long>>> {
      return Single.fromCallable { userDao.insertLikedUsers(*users) }
         .toResult()
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
   }
   
   fun getLikedUsers(): Single<Result<List<User>>> {
      return Single.fromCallable { userDao.getLikedUsers() }
         .toResult()
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
   }
}