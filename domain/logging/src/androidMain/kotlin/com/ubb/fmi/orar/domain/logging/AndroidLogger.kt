package com.ubb.fmi.orar.domain.logging

import Logger
import android.util.Log

class AndroidLogger : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}
