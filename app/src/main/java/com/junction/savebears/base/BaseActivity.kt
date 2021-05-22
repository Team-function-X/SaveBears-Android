package com.junction.savebears.base

import androidx.appcompat.app.AppCompatActivity
import com.junction.savebears.app.App
import com.junction.savebears.remote.retrofit.ApiModule

abstract class BaseActivity : AppCompatActivity() {

    protected val roomDatabase
        get() = (application as App).roomDataBase

    protected val saveBearsApi
        get() = ApiModule.saveBearsApi()
}