package com.example.orarubb_fmi

import android.content.Context
import androidx.startup.Initializer

class OrarUBBFMIInitializer : Initializer<OrarUBBFMI> {
    override fun create(context: Context): OrarUBBFMI {
        OrarUBBFMI.init(context)
        return OrarUBBFMI
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
