package com.app.gjekassignment.data

import com.app.gjekassignment.OpenForTesting
import com.app.gjekassignment.api.PeopleRandomService
import com.app.gjekassignment.di.ApplicationScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.toCompletable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Callable
import javax.inject.Inject

@OpenForTesting
@ApplicationScope
class UsersRepository @Inject constructor(private val peopleRandomService: PeopleRandomService,
                                          private val userDao: UserDao) {
   
   private val apiErrorHandler by lazy { ApiErrorHandler() }
   
   fun getUsers(): Single<Result<List<User>>> {
      return peopleRandomService.getRandomPeople()
         .map { it.results.map { userResponse -> userResponse.user } }
         .toResult(apiErrorHandler)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
   }
   
   fun addLikedUsers(vararg users: User): Completable {
      return Callable { userDao.insertLikedUsers(*users) }.toCompletable()
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