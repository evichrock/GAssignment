package com.app.gjekassignment.utils

import com.app.gjekassignment.data.Location
import com.app.gjekassignment.data.Name
import com.app.gjekassignment.data.User

object TestUtils {
   
   fun createUser(id: Int = 1, name: Name = Name("Sandra", "Ann"), email: String = "sandra@gmail.com") = User(
      id = id,
      name = name,
      location = Location("238 Rolling Green Rd", ""),
      email = email,
      password = "tulips",
      dob = "12/4/1986",
      phone = "(478)-553-8700",
      cellPhone = "",
      picture = "https://randomuser.me/api/portraits/women/32.jpg"
   )
}