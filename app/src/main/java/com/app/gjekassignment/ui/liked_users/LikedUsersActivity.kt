package com.app.gjekassignment.ui.liked_users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gjekassignment.GjekApp
import com.app.gjekassignment.databinding.ActivityLikedUsersBinding

class LikedUsersActivity : AppCompatActivity() {
   
   private lateinit var binding: ActivityLikedUsersBinding
   private lateinit var viewModel: LikedUsersViewModel
   
   private val adapter by lazy { LikedUsersAdapter() }
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityLikedUsersBinding.inflate(layoutInflater)
      setContentView(binding.root)
   
      binding.rvLikedUsers.setHasFixedSize(true)
      binding.rvLikedUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      binding.rvLikedUsers.adapter = adapter
      
      viewModel = ViewModelProvider(this, LikedUsersViewModel.Factory((application as GjekApp).usersRepository)).get()
      viewModel.getUsersLiveData().observe(this, Observer {
         adapter.setUsers(it)
      })
      viewModel.getShowLoadingLiveData().observe(this, Observer {})
      viewModel.init()
   }
}