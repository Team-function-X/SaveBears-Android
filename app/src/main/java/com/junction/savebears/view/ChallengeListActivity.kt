package com.junction.savebears.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.junction.savebears.adapter.ChallengeListAdapter
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityChallengeListBinding
import com.junction.savebears.local.room.Challenge
import com.junction.savebears.view.ChallengeDetailActivity.Companion.EXTRA_ITEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*


@FlowPreview
class ChallengeListActivity : BaseActivity() {
    private lateinit var binding: ActivityChallengeListBinding
    private val dao get() = roomDatabase.challengeDao()
    private val adapter = ChallengeListAdapter {
        val intent = Intent(this, ChallengeDetailActivity::class.java)
        intent.putExtra(EXTRA_ITEM, it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewAdapter()
        showAllChallenges()
    }

    private fun showAllChallenges() {
        lifecycleScope.launch(Dispatchers.IO) {
//            if (BuildConfig.DEBUG) {
//                val dummySignatureImage = drawableToByteArray(R.drawable.dash_border)
//                val dummy = Challenge(
//                    missionCompleteDate = Date(),
//                    imageSignature = dummySignatureImage,
//                    imageStrUri = "이미지 Uri",
//                    comment = "코멘트"
//                )
//                val dummyDatas = mutableListOf<Challenge>()
//                (1..20).forEachIndexed { i, _ -> dummyDatas.add(dummy.copy(id = i)) }
//                dao.insert(dummyDatas)
//            }
            getAllChallenges()
        }
    }

    private fun getAllChallenges() {
        lifecycleScope.launch(Dispatchers.IO) {
            dao.getAllChallenges() // 날짜 상관없이 모든 챌린지들을 List 형태로 담아 가져옴
                .flatMapConcat { list ->
                    if (list.isEmpty()) { // 리스트가 없으면 빈 리스트 반환
                        return@flatMapConcat flow<List<Challenge>> {
                            listOf<Challenge>()
                        }
                    } else {
                        flow {
                            dao.getAllChallenges()
                                .collect {
                                    adapter.addItem(it)
                                    adapter.notifyDataSetChanged()
                                }
                        }// 리스트가 있으면 해당 리스트 반환
                    }
                }
                .flowOn(Dispatchers.IO) // Flow 스트림을 IO 쓰레드에서 동작
                .catch { } // 에러 캐치
                .collect {
                    adapter.addItem(it)
                    adapter.notifyDataSetChanged()
                } // 데이터 구독
        }
    }

    private fun setRecyclerViewAdapter() {
        binding.rvChallenges.apply {
            adapter = this@ChallengeListActivity.adapter
//            addItemDecoration(
//                DividerItemDecoration(
//                    this.context,
//                    (this.layoutManager as LinearLayoutManager).orientation
//                )
//            )
        }
    }
}