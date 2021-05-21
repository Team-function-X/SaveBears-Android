package com.junction.savebears.app

import android.app.Application
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.junction.savebears.local.room.LocalDataBase
import timber.log.Timber

class App : Application() {

    lateinit var roomDataBase : LocalDataBase

    override fun onCreate() {
        roomDataBase = Room.databaseBuilder(applicationContext, LocalDataBase::class.java, LOCAL_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }

    companion object {
        private const val LOCAL_DB_NAME = "SaveBearsLocalData"
    }
}