package com.example.orarubb_fmi

import android.content.Context
import com.example.orarubb_fmi.di.KoinContext

object OrarUBBFMI {
    fun init(context: Context) {
        KoinContext.startKoin(context)
    }
}
