package com.ubb.fmi.orar.logging

import platform.Foundation.NSLog

class IosLogger: Logger {
    override fun log(tag: String, message: String) {
        NSLog("$tag: $message")
    }
}
