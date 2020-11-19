package com.app.gjekassignment.data

import io.reactivex.rxjava3.core.Single

sealed class Result<T> {

   data class Success<T>(val data: T) : Result<T>()
   
   data class Failure<T>(val error: Error) : Result<T>()
}


fun <T> Single<T>.toResult(errorHandler: ErrorHandler = ApiErrorHandler()): Single<Result<T>> {
   return map<Result<T>> { Result.Success(it) }.onErrorReturn { Result.Failure(errorHandler.getError(it)) }
}