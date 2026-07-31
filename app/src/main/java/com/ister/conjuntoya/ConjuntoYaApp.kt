package com.ister.conjuntoya

import android.app.Application

class ConjuntoYaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
