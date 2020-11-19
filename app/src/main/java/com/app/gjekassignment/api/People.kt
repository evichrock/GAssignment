package com.app.gjekassignment.api

import com.app.gjekassignment.data.User

data class RandomPeopleResponse(val results: List<UserResponse>)

data class UserResponse(val user: User)
