package com.junction.savebears.base

import androidx.appcompat.app.AppCompatActivity
import com.junction.savebears.app.App

open class BaseActivity : AppCompatActivity() {
    private val roomDatabase
        get() = (application as App).roomDataBase
}