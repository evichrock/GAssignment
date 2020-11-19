package com.app.gjekassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.gjekassignment.utils.RxImmediateSchedulerRule
import com.app.gjekassignment.data.Error
import com.app.gjekassignment.data.Result
import com.app.gjekassignment.data.User
import com.app.gjekassignment.data.UsersRepository
import com.app.gjekassignment.utils.TestUtils
import com.app.gjekassignment.utils.mock
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class UsersViewModelTest {
   
   @Rule
   @JvmField var testSchedulerRule =
      RxImmediateSchedulerRule()
   
   @Rule
   @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()

   private val usersRepository: UsersRepository = mock()
   private val usersViewModel = UsersViewModel(usersRepository)
   
   @Test
   fun testNull() {
      verify(usersRepository, never()).getUsers()
      verify(usersRepository, never()).addLikedUsers(TestUtils.createUser())
   }
   
   @Test
   fun observeShouldNotReceiveAnyChanged() {
      val usersObserver = mock<Observer<List<User>>>()
      usersViewModel.getUsersLiveData().observeForever(usersObserver)
      verify(usersObserver, never()).onChanged(anyList())
   
      val showLoadingObserver = mock<Observer<Boolean>>()
      usersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      verify(showLoadingObserver, never()).onChanged(anyBoolean())
   }
   
   @Test
   fun fetchUsers() {
      val testScheduler = TestScheduler()
      
      val usersResult = Result.Success(listOf(TestUtils.createUser()))
      doReturn(Single.just(usersResult).delay(100, TimeUnit.MILLISECONDS, testScheduler))
                     .`when`(usersRepository).getUsers()
      val observer = mock<Observer<List<User>>>()
      usersViewModel.getUsersLiveData().observeForever(observer)
      val showLoadingObserver = mock<Observer<Boolean>>()
      usersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      
      usersViewModel.init()
   
      testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)
      verify(showLoadingObserver, times(1)).onChanged(true)
      testScheduler.advanceTimeBy(99, TimeUnit.MILLISECONDS)
      verify(showLoadingObserver, times(1)).onChanged(false)
      
      verify(usersRepository, atLeast(12)).getUsers()
      verify(observer).onChanged(anyList())
      assertThat(usersViewModel.getUsersLiveData().value!!.size, `is`(12))
   }
   
   @Test
   fun fetchUsersFailed() {
      doReturn(Single.just(Result.Failure<List<User>>(Error.Network))).`when`(usersRepository).getUsers()
      val usersObserver = mock<Observer<List<User>>>()
      usersViewModel.getUsersLiveData().observeForever(usersObserver)
      val showLoadingObserver = mock<Observer<Boolean>>()
      usersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      
      usersViewModel.init()
   
      verify(usersObserver, atLeastOnce()).onChanged(anyList())
      verify(showLoadingObserver, atLeastOnce()).onChanged(true)
      verify(showLoadingObserver, atLeastOnce()).onChanged(false)
      verify(usersRepository, atLeast(12)).getUsers()
      assertThat(usersViewModel.getUsersLiveData().value!!.size, `is`(0))
   }
   
   @Test
   fun likeUser() {
      val user = TestUtils.createUser()
      doReturn(Single.just(Result.Success(listOf(1L)))).`when`(usersRepository).addLikedUsers(user)
      val showLoadingObserver = mock<Observer<Boolean>>()
      usersViewModel.getShowLoadingLiveData().observeForever(showLoadingObserver)
      
      usersViewModel.likeUser(user)
   
      verify(showLoadingObserver, never()).onChanged(anyBoolean())
      verify(usersRepository, only()).addLikedUsers(user)
   }
}