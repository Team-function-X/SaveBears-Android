package com.junction.savebears.view

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.junction.savebears.adapter.ChallengeListAdapter
import com.junction.savebears.adapter.ChallengeSelectionListener
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.databinding.ActivityChallengeListBinding
import com.junction.savebears.local.room.Challenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
class ChallengeListActivity : BaseActivity(), ChallengeSelectionListener {

    private lateinit var binding: ActivityChallengeListBinding
    private val dao get() = roomDatabase.challengeDao()
    private val adapter = ChallengeListAdapter(this)
    private val uiState = MutableLiveData<UiState<List<Challenge>?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeListBinding.inflate(layoutInflater)
        setRecyclerViewAdapter()
        showAllChallenges()
    }

    @FlowPreview
    private fun showAllChallenges() {
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

    override fun onChallengeClick(item: Challenge) {
        // TODO 리스트 아이템 클릭 이벤트
    }
}