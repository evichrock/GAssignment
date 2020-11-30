package com.app.gjekassignment.di

import com.app.gjekassignment.ui.liked_users.LikedUsersActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [LikedUsersModule::class])
interface LikedUsersComponent {
   
   @Subcomponent.Factory
   interface Factory {
      fun create(): LikedUsersComponent
   }

   fun inject(activity: LikedUsersActivity)
}