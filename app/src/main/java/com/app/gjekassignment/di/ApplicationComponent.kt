package com.app.gjekassignment.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelFactoryModule::class])
interface ApplicationComponent {
   
   @Component.Factory
   interface Factory {
      fun create(@BindsInstance applicationContext: Context): ApplicationComponent
   }
   
   fun usersComponent(): UsersComponent.Factory
   fun likedUsersComponent(): LikedUsersComponent.Factory
}