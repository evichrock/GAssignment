package com.app.gjekassignment.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.gjekassignment.R
import com.app.gjekassignment.data.User
import com.app.gjekassignment.databinding.LayoutUserItemBinding
import com.bumptech.glide.Glide

class CardStackAdapter(context: Context): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
   
   private var users: List<User> = emptyList()
   private val transY = context.resources.getDimension(R.dimen.ic_info_height)
   
   private val unselectedMatrix: Matrix = Matrix().apply {
      setScale(0.5F, 0.5F)
      postTranslate(0F, -transY)
   }
   private val selectedMatrix =  Matrix().apply {
      setScale(0.5F, 0.5F)
      postTranslate(0F, 0F)
   }
   
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_user_item, parent, false))
   }
   
   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(users[position])
   }
   
   override fun getItemCount() = users.size
   
   fun getUsers() = users
   
   fun getUserAtPos(pos: Int) = users.elementAtOrNull(pos)
   
   fun setUsers(users: List<User>) {
      this.users = ArrayList(users)
   }
   
   inner class ViewHolder(view: View, private val binding: LayoutUserItemBinding =
      LayoutUserItemBinding.bind(view)) : RecyclerView.ViewHolder(view) {
   
      private var lastSelectedIcon: ImageView? = null
      private lateinit var currUser: User
      private val iconSelectedListener = View.OnClickListener { view ->
         lastSelectedIcon = applySelected(currUser, view as ImageView, lastSelectedIcon)
      }
      
      fun bind(user: User) {
         val context = binding.root.context
         currUser = user
      
         Glide.with(context)
            .load(user.picture)
            .into(binding.ivAvatar)
   
         lastSelectedIcon = applySelected(user, binding.ivName, lastSelectedIcon)
   
         binding.ivName.imageMatrix = selectedMatrix
         binding.ivDob.imageMatrix = unselectedMatrix
         binding.ivAddress.imageMatrix = unselectedMatrix
         binding.ivPhone.imageMatrix = unselectedMatrix
         binding.ivPassword.imageMatrix = unselectedMatrix
   
         binding.ivName.setOnClickListener(iconSelectedListener)
         binding.ivDob.setOnClickListener(iconSelectedListener)
         binding.ivAddress.setOnClickListener(iconSelectedListener)
         binding.ivPhone.setOnClickListener(iconSelectedListener)
         binding.ivPassword.setOnClickListener(iconSelectedListener)
      }
      
      private fun applySelected(user: User, selectedIcon: ImageView, lastSelectedIcon: ImageView?): ImageView {
         if (selectedIcon != lastSelectedIcon) {
            lastSelectedIcon?.toggleSelected()
            selectedIcon.toggleSelected()
            
            val newIndex = binding.icContainer.indexOfChild(selectedIcon)
            val context = selectedIcon.context
            when (newIndex) {
               0 -> {
                  binding.tvTitle.text = context.getString(R.string.name_title)
                  binding.tvSubTitle.text = context.getString(R.string.name_display_placeholders,
                     user.name.first.capitalize(), user.name.last.capitalize())
               }
   
               1 -> {
                  binding.tvTitle.text = context.getString(R.string.dob_title)
                  binding.tvSubTitle.text = user.dob
               }
   
               2 -> {
                  binding.tvTitle.text = context.getString(R.string.address_title)
                  binding.tvSubTitle.text = user.location.street
               }
   
               3 -> {
                  binding.tvTitle.text = context.getString(R.string.phone_title)
                  binding.tvSubTitle.text = user.phone
               }
               
               4 -> {
                  binding.tvTitle.text = context.getString(R.string.password_title)
                  binding.tvSubTitle.text = user.password
               }
            }
         }
         return selectedIcon
      }
   
      private fun ImageView.toggleSelected() {
         isSelected = !isSelected
         imageMatrix = if (isSelected) selectedMatrix else unselectedMatrix
      }
   }
}