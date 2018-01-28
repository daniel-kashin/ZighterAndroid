package com.zighter.zighterandroid.util

class Optional<T> private constructor(private val value: T?) {

    val isPresent: Boolean
        get() = value != null

    fun get(): T? {
        return value
    }

    companion object {
        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }
    }
}