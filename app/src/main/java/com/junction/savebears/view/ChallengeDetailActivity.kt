package com.junction.savebears.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.junction.savebears.R
import com.junction.savebears.databinding.ActivityChallengeDetailBinding
import com.junction.savebears.databinding.ActivityChallengeListBinding
import com.junction.savebears.local.room.Challenge

/**
 * Challenge 리스트의 아이템을 클릭했을 때, 상세보기 페이지로 이동함
 */
class ChallengeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        binding = ActivityChallengeDetailBinding.inflate(layoutInflater)

        val item = intent.getSerializableExtra(EXTRA_ITEM) as Challenge

        Glide.with(this)
            .load(Uri.parse(item.imageStrUri))
            .into(binding.challengeDetailImageView)

        if (item.comment.isBlank()){
            binding.challengeDetailCommentTextView.text = getString(R.string.not_registered_comment)
        }else{
            binding.challengeDetailCommentTextView.text = item.comment
        }

    }

    companion object {
        const val EXTRA_ITEM = "EXTRA_ITEM"
    }
}