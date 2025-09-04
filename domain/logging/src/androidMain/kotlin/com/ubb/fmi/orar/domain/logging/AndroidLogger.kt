package com.ubb.fmi.orar.domain.logging

import Logger
import android.util.Log

/**
 * Android specific [Logger] implementation
 */
class AndroidLogger : Logger {

    /**
     * Method for debug scope logging
     */
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    /**
     * Method for error scope logging
     */
    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    /**
     * Method for informative scope logging
     */
    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}
