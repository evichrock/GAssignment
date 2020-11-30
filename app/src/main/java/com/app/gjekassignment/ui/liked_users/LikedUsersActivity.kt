package com.app.gjekassignment.ui.liked_users

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gjekassignment.GjekApp
import com.app.gjekassignment.databinding.ActivityLikedUsersBinding
import javax.inject.Inject

class LikedUsersActivity : AppCompatActivity() {
   
   @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
   private val viewModel by viewModels<LikedUsersViewModel> { viewModelFactory }
   
   private lateinit var binding: ActivityLikedUsersBinding
   private val adapter by lazy { LikedUsersAdapter() }
   
   override fun onCreate(savedInstanceState: Bundle?) {
      (applicationContext as GjekApp).appComponent
         .likedUsersComponent().create().inject(this)
      
      super.onCreate(savedInstanceState)
      binding = ActivityLikedUsersBinding.inflate(layoutInflater)
      setContentView(binding.root)
   
      binding.rvLikedUsers.setHasFixedSize(true)
      binding.rvLikedUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      binding.rvLikedUsers.adapter = adapter
      
      viewModel.getUsersLiveData().observe(this, Observer {
         adapter.setUsers(it)
      })
      viewModel.getShowLoadingLiveData().observe(this, Observer {})
      viewModel.init()
   }
}