package com.junction.savebears

import android.net.Uri
import android.os.Bundle
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityChallengeListBinding
import com.junction.savebears.remote.retrofit.ApiModule

class ChallengeListActivity : BaseActivity() {

    private lateinit var binding: ActivityChallengeListBinding
    private val saveBearsApi
        get() = ApiModule.saveBearsApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeListBinding.inflate(layoutInflater)


        // TODO with api
        saveBearsApi
    }

}