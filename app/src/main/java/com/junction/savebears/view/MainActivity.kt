package com.junction.savebears.view

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding4.view.clicks
import com.junction.savebears.R
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.databinding.ActivityMainBinding
import com.junction.savebears.remote.model.GlacierResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@FlowPreview
class MainActivity : BaseActivity() {

    private var isFirstTurnOn = false
    private lateinit var binding: ActivityMainBinding
    private val uiState = MutableLiveData<UiState<GlacierResponse>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uiState.value = UiState.loading() // 초기 로딩
        setOnListeners()
    }

    private fun setOnListeners() {
        binding.ecoChallengeButton
            .clicks()
            .debounce(DEBOUNCED_TIME, TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ moveActivity() }, Timber::e)
    }

    private fun moveActivity() {
        startActivity(Intent(this, ChallengeListActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        // TODO 빙하 데이터 fetching
        //  1. 앱 처음 진입시에는 기존 빙하 데이터 가져오기
        //  2. 처음이 아닐 시 onStart를 타는 경우 서버에 반영된 결과를 바탕으로 Refresh
        //  3. API를 나눌지 하나로 통합해서 하나의 값으로 분기할지 정하기 (예를 들면 빈문자열 일때는 기존 값을 보내준다던가)
        //  4. 각각 에러, 로딩, 반영시 어떻게 UI 반영할지 정하기(기획 필요)
        //  5. 특히 성공 시에 곰 입모양, 눈물 등 이미지를 어떻게 표현 할지 논의 필요

        if (isFirstTurnOn) {
            getGlacierData()
        } else {
            refreshGlacierData()
        }
    }

    private fun getGlacierData() {
        lifecycleScope.launch(Dispatchers.IO) {
            flow<GlacierResponse> {
                saveBearsApi.getGlacierChange()
                    .catch {
                        isFirstTurnOn = false
                        uiState.postValue(UiState.error(it.message ?: getString(R.string.unknown_error)))
                    }
                    .collect {
                        isFirstTurnOn = true
                        uiState.postValue(UiState.success(it))
                    }
            }
        }
    }

    private fun refreshGlacierData() {
        lifecycleScope.launch(Dispatchers.IO) {

            saveBearsApi.getGlacierChange()
                .catch { uiState.postValue(UiState.error(it.message ?: getString(R.string.unknown_error))) }
                .collect { uiState.postValue(UiState.success(it)) }
        }

//            glacierDataLiveData = liveData(Dispatchers.IO) {
//            saveBearsApi.getGlacierChange().asCallbackFlow().catch { e ->
//                // 에러 스트림 처리
//                Timber.d(e)
//            }.collect {
//                // 최종적으로 데이터 스트림 받고 LiveData 로 Emit 함
//                this.emit(it)
//            }
    }

    private fun showChangesGlacierHeight(response: GlacierResponse?) {
        response?.let {
            // TODO 빙하 관련 UI 업데이트 로직
        }
    }

    override fun observeUiResult() {
        uiState.observe(this) {
            when (it.status) {
                Status.SUCCESS -> { // 성공했을 때
                    binding.loadingView.progress.isVisible = false
                    showChangesGlacierHeight(it.data)
                }
                Status.LOADING -> { // 로딩중일 때
                    binding.loadingView.progress.isVisible = true
                }
                Status.ERROR -> { // 실패했을 때
                    binding.loadingView.progress.isVisible = true
                    Timber.e(it.message)
                }
                Status.EMPTY -> { // 데이터가 비었을 때
                    binding.loadingView.progress.isVisible = false
                }
            }
        }
    }

    companion object {
        private const val DEBOUNCED_TIME = 250L
    }
}