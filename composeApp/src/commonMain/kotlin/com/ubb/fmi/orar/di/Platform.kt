package com.ubb.fmi.orar.di

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform