package com.junction.savebears

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.remote.retrofit.ApiModule

class RegisterChallengeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val saveBearsApi
        get() = ApiModule.saveBearsApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_challenge)
    }

    private fun uploadChallengeImage(image: Uri){

    }
}