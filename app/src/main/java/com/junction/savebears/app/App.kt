package com.junction.savebears.app

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.room.Room
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.junction.savebears.BuildConfig
import com.junction.savebears.local.room.LocalDataBase
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.io.File

class App : Application() {

    lateinit var roomDataBase: LocalDataBase

    override fun onCreate() {
        roomDataBase =
            Room.databaseBuilder(applicationContext, LocalDataBase::class.java, LOCAL_DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()

        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)

            Log.i("App", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("App", "Could not initialize Amplify: error")
        }
        //uploadFile()

    }

    companion object {
        private const val LOCAL_DB_NAME = "SaveBearsLocalData"



    }

}