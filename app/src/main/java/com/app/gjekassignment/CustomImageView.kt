package com.app.gjekassignment

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.translationMatrix

class CustomImageView(context: Context): ImageView(context) {
   
   constructor(context: Context, attrs: AttributeSet?) : this(context)
   
   constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)
   
   constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : this(context, attrs, defStyleAttr)
   
   override fun setSelected(selected: Boolean) {
      imageMatrix = if (selected)
         translationMatrix(0F, 0F)
      else
         translationMatrix(0F, 96F)
      
      super.setSelected(selected)
   }
}