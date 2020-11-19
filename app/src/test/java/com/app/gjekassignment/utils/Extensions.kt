package com.app.gjekassignment.utils

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

fun Any.loadJson(fileName: String): String {
   return javaClass.classLoader!!
      .getResourceAsStream("api-response/$fileName")
      .source().buffer().readString(Charsets.UTF_8)
}


fun MockWebServer.enqueueResponse(expectedBody: String, headers: Map<String, String> = emptyMap()) {
   val mockResponse = MockResponse()
   for ((key, value) in headers) {
      mockResponse.addHeader(key, value)
   }
   enqueue(mockResponse.setBody(expectedBody))
}

inline fun <reified T> String.toTypeOf(): T = Gson().fromJson(this, T::class.java)