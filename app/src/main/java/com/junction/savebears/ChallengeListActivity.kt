package com.junction.savebears

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityChallengeListBinding

class ChallengeListActivity : BaseActivity() {

    private lateinit var binding: ActivityChallengeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeListBinding.inflate(layoutInflater)

    }
}