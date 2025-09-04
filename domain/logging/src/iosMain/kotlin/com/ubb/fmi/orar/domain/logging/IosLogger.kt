package com.ubb.fmi.orar.domain.logging

import Logger
import platform.Foundation.NSLog

/**
 * Ios specific [Logger] implementation
 */
class IosLogger : Logger {

    /**
     * Method for debug scope logging
     */
    override fun d(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    /**
     * Method for error scope logging
     */
    override fun e(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    /**
     * Method for informative scope logging
     */
    override fun i(tag: String, message: String) {
        NSLog("$tag: $message")
    }
}
