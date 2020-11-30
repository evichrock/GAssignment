package com.app.gjekassignment.ui.liked_users

import androidx.lifecycle.*
import com.app.gjekassignment.data.Result
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UsersRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import javax.inject.Inject

class LikedUsersViewModel @Inject constructor(private val usersRepository: UsersRepository) : ViewModel() {
   
   private val compositeDisposable = CompositeDisposable()
   private var firstLoaded = false
   private var usersLiveData = MutableLiveData<List<User>>()
   private var showLoadingLiveData = MutableLiveData<Boolean>()
   
   fun getUsersLiveData(): LiveData<List<User>> = usersLiveData
   fun getShowLoadingLiveData(): LiveData<Boolean> = showLoadingLiveData
   
   fun init() {
      if (firstLoaded) return
   
      firstLoaded = true
   
      compositeDisposable += usersRepository.getLikedUsers()
         .doOnSubscribe { showLoadingLiveData.value = true }
         .doOnEvent { _, _ -> showLoadingLiveData.value = false }.subscribe({ result ->
            when (result) {
               is Result.Success -> usersLiveData.value = result.data
               is Result.Failure -> result.error
            }
         }, {})
   }
   
   override fun onCleared() {
      compositeDisposable.clear()
   }
}