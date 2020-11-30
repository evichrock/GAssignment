package com.app.gjekassignment

import android.app.Application
import com.app.gjekassignment.di.ApplicationComponent
import com.app.gjekassignment.di.DaggerApplicationComponent
import com.google.gson.Gson

class GjekApp : Application() {
   
   val appComponent: ApplicationComponent by lazy { DaggerApplicationComponent.factory().create(applicationContext) }

   companion object {
      val gson by lazy { Gson() }
   }
}