package com.app.gjekassignment.utils

import com.app.gjekassignment.GjekApp

fun <T> T.toJson(): String = GjekApp.gson.toJson(this)

inline fun <reified T> String.toTypeOf(): T = GjekApp.gson.fromJson(this, T::class.java)