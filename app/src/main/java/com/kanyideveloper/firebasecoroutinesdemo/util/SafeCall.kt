package com.kanyideveloper.firebasecoroutinesdemo.util

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Unknown Error Occurred")
    }
}