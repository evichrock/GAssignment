package com.app.gjekassignment.di

import androidx.lifecycle.ViewModel
import com.app.gjekassignment.ui.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UsersModule {
   
   @ActivityScope
   @Binds
   @IntoMap
   @ViewModelKey(UsersViewModel::class)
   abstract fun bindUsersViewModel(usersViewModel: UsersViewModel): ViewModel
}