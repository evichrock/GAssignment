package com.app.gjekassignment.ui.liked_users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gjekassignment.R
import com.app.gjekassignment.data.User
import com.app.gjekassignment.databinding.LayoutLikedUserItemBinding
import com.bumptech.glide.Glide

class LikedUsersAdapter : RecyclerView.Adapter<LikedUsersAdapter.ViewHolder>() {
   
   private val users = arrayListOf<User>()
   
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_liked_user_item, parent, false))
   }
   
   override fun getItemCount() = users.size
   
   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.binding.bind(users[position])
   }
   
   fun setUsers(users: List<User>) {
      this.users.clear()
      this.users.addAll(users)
      notifyDataSetChanged()
   }
   
   class ViewHolder(view: View, val binding: LayoutLikedUserItemBinding
         = LayoutLikedUserItemBinding.bind(view)) : RecyclerView.ViewHolder(view)
   
   private fun LayoutLikedUserItemBinding.bind(user: User) {
      Glide.with(ivAvatar.context)
         .load(user.picture)
         .into(ivAvatar)
      tvName.text = tvName.context.getString(R.string.name_display_placeholders,
         user.name.first.capitalize(), user.name.last.capitalize())
   }
}