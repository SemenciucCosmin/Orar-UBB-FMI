package com.ubb.fmi.orar.domain.logging

import android.util.Log
import com.ubb.fmi.orar.domain.logging.Logger

class AndroidLogger: Logger {
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
