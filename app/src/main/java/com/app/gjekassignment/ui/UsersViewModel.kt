package com.app.gjekassignment.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.app.gjekassignment.OpenForTesting
import com.app.gjekassignment.data.Result
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UsersRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

@OpenForTesting
class UsersViewModel(private val usersRepository: UsersRepository) : ViewModel() {
   
   private val compositeDisposable = CompositeDisposable()
   private var firstLoaded = false
   private val users = arrayListOf<User>()
   
   private var usersLiveData = MutableLiveData<List<User>>()
   private var showLoadingLiveData = MutableLiveData<Boolean>()
   
   fun getUsersLiveData(): LiveData<List<User>> = usersLiveData
   fun getShowLoadingLiveData(): LiveData<Boolean> = showLoadingLiveData
   
   fun init() {
      if (firstLoaded) return
   
      fetchUsers()
   }
   
   private fun fetchUsers(loadCount: Int = 12, retryCount: Int = 3) {
      compositeDisposable.add(Observable.range(1, loadCount)
         .doOnSubscribe { showLoadingLiveData.value = true }
         .buffer(2) // call two apis once at a time
         .zipWith<Long, List<Int>>(Observable
            .interval(100, TimeUnit.MILLISECONDS), BiFunction { list, _ -> list }) // avoid spamming apis cause failed
         .flatMap { Observable.fromIterable(it) }
         .flatMapSingle { usersRepository.getUsers() }
         .toList()
         .doOnEvent { _, _ -> showLoadingLiveData.value = false }
         .subscribe({ results ->
            val failures = arrayListOf<Result.Failure<*>>()
         
            for (result in results)
               when (result) {
                  is Result.Success<List<User>> -> users.addAll(result.data)
                  is Result.Failure -> failures.add(result)
               }
            usersLiveData.value = users
   
            if (failures.size > 0 && retryCount > 0)
               fetchUsers(failures.size, retryCount - 1)
         }, {}))
   }
   
   fun loadMore() {
      fetchUsers()
   }
   
   fun likeUser(user: User) {
      usersRepository.addLikedUsers(user).subscribe({}, {})
   }
   
   fun skipUser(user: User) {
   }
   
   override fun onCleared() {
      compositeDisposable.clear()
   }
   
   class Factory(private val peopleRepository: UsersRepository) : ViewModelProvider.Factory {
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
         return try {
            modelClass.getConstructor(UsersRepository::class.java).newInstance(peopleRepository)
         } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
         } catch (e: SecurityException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
         }
      }
   }
}