package com.app.gjekassignment.data

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

sealed class Error {
   
   object Network : Error()
   
   object NotFound : Error()
   
   object AccessDenied : Error()
   
   object ServiceUnavailable : Error()
   
   object Unknown : Error()
}


interface ErrorHandler {
   
   fun getError(throwable: Throwable): Error
}

open class DefaultErrorHandler : ErrorHandler {
   
   override fun getError(throwable: Throwable): Error = Error.Unknown
}

class ApiErrorHandler : DefaultErrorHandler() {
   
   override fun getError(throwable: Throwable): Error {
      return when(throwable) {
         is HttpException -> {
            when(throwable.code()) {
               HttpURLConnection.HTTP_BAD_REQUEST -> Error.Network
               HttpURLConnection.HTTP_NOT_FOUND -> Error.NotFound
               HttpURLConnection.HTTP_FORBIDDEN -> Error.AccessDenied
               HttpURLConnection.HTTP_UNAVAILABLE -> Error.ServiceUnavailable
               else -> super.getError(throwable)
            }
         }
         else -> super.getError(throwable)
      }
   }
}