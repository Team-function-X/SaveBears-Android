package com.junction.savebears

import android.os.Bundle
import com.junction.savebears.app.App
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

    }
}