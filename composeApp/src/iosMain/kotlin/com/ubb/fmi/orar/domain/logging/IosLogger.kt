package com.ubb.fmi.orar.domain.logging

import com.ubb.fmi.orar.domain.logging.Logger
import platform.Foundation.NSLog

class IosLogger: Logger {
    override fun d(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    override fun e(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    override fun i(tag: String, message: String) {
        NSLog("$tag: $message")
    }
}
