package com.app.gjekassignment.di

import com.app.gjekassignment.ui.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [UsersModule::class])
interface UsersComponent {
   
   @Subcomponent.Factory
   interface Factory {
      fun create(): UsersComponent
   }

   fun inject(activity: MainActivity)
}