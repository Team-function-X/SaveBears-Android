package com.junction.savebears.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.junction.savebears.BuildConfig
import com.junction.savebears.R
import com.junction.savebears.adapter.ChallengeListAdapter
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.component.ext.drawableToByteArray
import com.junction.savebears.component.ext.toastLong
import com.junction.savebears.databinding.ActivityChallengeListBinding
import com.junction.savebears.local.room.Challenge
import com.junction.savebears.view.ChallengeDetailActivity.Companion.EXTRA_ITEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.*


@FlowPreview
class ChallengeListActivity : BaseActivity() {

    private lateinit var binding: ActivityChallengeListBinding
    private val dao get() = roomDatabase.challengeDao()
    private val uiState = MutableLiveData<UiState<List<Challenge>?>>()
    private val adapter = ChallengeListAdapter {
        val intent = Intent(this, ChallengeDetailActivity::class.java)
        intent.putExtra(EXTRA_ITEM, it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeListBinding.inflate(layoutInflater)
        toastLong(R.string.app_name)
        setRecyclerViewAdapter()
        showAllChallenges()
    }

    private fun showAllChallenges() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (BuildConfig.DEBUG) {
                val dummySignatureImage = drawableToByteArray(R.drawable.ic_water_bottle)
                val dummy = Challenge(
                    missionCompleteDate = Date(),
                    imageSignature = dummySignatureImage,
                    imageStrUri = "이미지 Uri",
                    comment = "코멘트"
                )
                val dummyDatas = mutableListOf<Challenge>()
                (1..20).forEachIndexed { i, _ -> dummyDatas.add(dummy.copy(id = i)) }
                dao.insert(dummyDatas)
            }
            getAllChallenges()
        }
    }

    private fun getAllChallenges() {
        lifecycleScope.launch(Dispatchers.IO) {
            dao.getAllChallenges() // 날짜 상관없이 모든 챌린지들을 List 형태로 담아 가져옴
                .flatMapConcat { list ->
                    if (list.isEmpty()) { // 리스트가 없으면 빈 리스트 반환
                        return@flatMapConcat flow<List<Challenge>> {
                            uiState.postValue(UiState.empty(null)) // 데이터 비었다고 알려주기
                            listOf<Challenge>()
                        }
                    } else {
                        flow { dao.getAllChallenges() } // 리스트가 있으면 해당 리스트 반환
                    }
                }
                .flowOn(Dispatchers.IO) // flow 스트림을 IO 쓰레드에서 동작
                .catch { uiState.postValue(UiState.error(it.toString(), null)) } // 에러 캐치
                .collect { uiState.postValue(UiState.success(it)) } // 데이터 구독

        }
    }

    private fun setRecyclerViewAdapter() {
        binding.rvChallenges.apply {
            adapter = this@ChallengeListActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    override fun observeUiResult() {
        uiState.observe(this) {
            when (it.status) {
                Status.SUCCESS -> { // 성공했을 때
                    binding.loadingView.progress.isVisible = false
                    adapter.addItem(it.data ?: listOf())
                }
                Status.LOADING -> { // 로딩중일 때
                    binding.loadingView.progress.isVisible = true
                }
                Status.ERROR -> { // 실패했을 때
                    binding.loadingView.progress.isVisible = false
                    Timber.e(it.message)
                }
                Status.EMPTY -> { // 데이터가 비었을 때
                    binding.loadingView.progress.isVisible = false
                }
            }
        }
    }
}