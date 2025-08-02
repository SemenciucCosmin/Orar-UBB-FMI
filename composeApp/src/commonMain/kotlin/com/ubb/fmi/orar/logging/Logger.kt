package com.ubb.fmi.orar.logging

interface Logger {
    fun d(tag: String, message: String)

    fun e(tag: String, message: String)

    fun i(tag: String, message: String)
}
