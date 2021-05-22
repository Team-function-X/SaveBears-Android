package com.junction.savebears.view

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.remote.model.GlacierData
import timber.log.Timber

class RegisterChallengeActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val uiState = MutableLiveData<UiState<GlacierData>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterChallengeBinding.inflate(layoutInflater)
    }

    private fun saveChallengeImage() {
        // TODO 사진을 특정 naming의 파일로 저장하기
        //  형식 : 앱이름 + yyyy_MM_dd_hhmmss 추천
    }

    private fun uploadChallengeImage(imageUri: Uri) {
        // TODO 저장소에서 uri로 get 하기
        //  1. contentProvider에 cursor로 접근해 사진 정보를 가져오기
        //  2. 사진 백엔드에서 요구하는 형식에 맞게 변환 후 Request
        //   - 성공 -> 해당 점수 반영(기획 필요)
        //   - 실패 -> 다시 시도 로직(기획 필요)
        //   - 통신 중 -> 기획 필요

        getChallengeUris()
        saveBearsApi
    }

    private fun getChallengeUris() {

    }

    override fun observeUiResult() {
        uiState.observe(this) {
            when (it.status) {
                Status.SUCCESS -> { // 성공했을 때
                }
                Status.LOADING -> { // 로딩중일 때
                }
                Status.ERROR -> { // 실패했을 때
                    Timber.e(it.message)
                }
                Status.EMPTY -> { // 데이터가 비었을 때
                }
            }
        }
    }
}