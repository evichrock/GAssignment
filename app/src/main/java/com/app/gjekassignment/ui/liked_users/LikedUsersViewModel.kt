package com.app.gjekassignment.ui.liked_users

import androidx.lifecycle.*
import com.app.gjekassignment.data.Result
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UsersRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class LikedUsersViewModel(private val usersRepository: UsersRepository) : ViewModel() {
   
   private val compositeDisposable = CompositeDisposable()
   private var firstLoaded = false
   
   private var usersLiveData = MutableLiveData<List<User>>()
   private var showLoadingLiveData = MutableLiveData<Boolean>()
   
   fun getUsersLiveData(): LiveData<List<User>> = usersLiveData
   fun getShowLoadingLiveData(): LiveData<Boolean> = showLoadingLiveData
   
   fun init() {
      if (firstLoaded) return
   
      compositeDisposable.add(usersRepository.getLikedUsers()
         .doOnSubscribe { showLoadingLiveData.value = true }
         .doOnEvent { _, _ -> showLoadingLiveData.value = false }
         .subscribe({ result ->
            when (result) {
               is Result.Success -> usersLiveData.value = result.data
               is Result.Failure -> result.error
            }
         }, {}))
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