package com.navercorp.android.languagetranslate

import android.app.Application
import androidx.room.Room
import com.navercorp.android.languagetranslate.datasource.AppDatabase

lateinit var GlobalDatabase: AppDatabase

class TranslateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        ).build()

    }

}