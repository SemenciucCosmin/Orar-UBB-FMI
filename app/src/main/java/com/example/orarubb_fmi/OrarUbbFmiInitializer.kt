package com.example.orarubb_fmi

import android.content.Context
import androidx.startup.Initializer

class OrarUbbFmiInitializer : Initializer<OrarUbbFmi> {
    override fun create(context: Context): OrarUbbFmi {
        OrarUbbFmi.init(context)
        return OrarUbbFmi
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
