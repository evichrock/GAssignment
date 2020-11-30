package com.app.gjekassignment.di

import androidx.lifecycle.ViewModel
import com.app.gjekassignment.ui.liked_users.LikedUsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LikedUsersModule {
   
   @ActivityScope
   @Binds
   @IntoMap
   @ViewModelKey(LikedUsersViewModel::class)
   abstract fun bindUsersViewModel(likedUsersViewModel: LikedUsersViewModel): ViewModel
}