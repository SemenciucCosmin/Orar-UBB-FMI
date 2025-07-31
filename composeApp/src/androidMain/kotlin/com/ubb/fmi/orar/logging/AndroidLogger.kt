package com.ubb.fmi.orar.logging

import android.util.Log

class AndroidLogger: Logger {
    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }
}
