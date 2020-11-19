package com.app.gjekassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.gjekassignment.utils.RxImmediateSchedulerRule
import com.app.gjekassignment.data.Result
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UsersRepository
import com.app.gjekassignment.ui.liked_users.LikedUsersViewModel
import com.app.gjekassignment.utils.TestUtils
import com.app.gjekassignment.utils.mock
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class LikedUsersViewModelTest {
   
   @Rule
   @JvmField var testSchedulerRule =
      RxImmediateSchedulerRule()
   
   @Rule
   @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()
   
   private val usersRepository: UsersRepository = mock()
   private val likedUsersViewModel = LikedUsersViewModel(usersRepository)
   
   @Test
   fun testNull() {
      verify(usersRepository, never()).getLikedUsers()
   }
   
   @Test
   fun observeShouldNotReceiveAnyChanged() {
      val usersObserver = mock<Observer<List<User>>>()
      likedUsersViewModel.getUsersLiveData().observeForever(usersObserver)
      verify(usersObserver, never()).onChanged(any())
   
      val showLoadingObserver = mock<Observer<Boolean>>()
      likedUsersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      verify(showLoadingObserver, never()).onChanged(any())
   }
   
   @Test
   fun getLikedUsers() {
      val testScheduler = TestScheduler()
      
      val users = listOf(TestUtils.createUser())
      doReturn(Single.just(Result.Success(users)).delay(100, TimeUnit.MILLISECONDS, testScheduler))
                     .`when`(usersRepository).getLikedUsers()
      val usersObserver = mock<Observer<List<User>>>()
      likedUsersViewModel.getUsersLiveData().observeForever(usersObserver)
      val showLoadingObserver = mock<Observer<Boolean>>()
      likedUsersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      
      likedUsersViewModel.init()
   
      testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)
      verify(showLoadingObserver, times(1)).onChanged(true)
      
      testScheduler.advanceTimeBy(99, TimeUnit.MILLISECONDS)
      verify(showLoadingObserver, times(1)).onChanged(false)
      
      verify(usersRepository, only()).getLikedUsers()
      verify(usersObserver, only()).onChanged(users)
   }
}