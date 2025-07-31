package com.ubb.fmi.orar

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform