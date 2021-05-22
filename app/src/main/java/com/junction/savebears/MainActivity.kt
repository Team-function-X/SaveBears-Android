package com.junction.savebears

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityMainBinding
import com.junction.savebears.remote.extension.asCallbackFlow
import com.junction.savebears.remote.model.GlacierData
import com.junction.savebears.remote.retrofit.ApiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val saveBearsApi
        get() = ApiModule.saveBearsApi()

    lateinit var glacierDataLiveData: LiveData<GlacierData>

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        /**
         * 빙하 변화량 API 호출 (데이터 리프레쉬)
         */
        refreshGlacierData()

        /**
         * Glacier API 호출 Response 를 Observing (변화 시점에 View 변경하면 됨)
         */
        glacierDataLiveData.observe(this, Observer {
            // ImageView 적용 등
        })

        /**
         * 에코 챌린지 페이지로 이동할 수 있는 버튼
         */
        binding.ecoChallengeButton.setOnClickListener {
            startActivity(Intent(this, ChallengeListActivity::class.java))
        }

    }

    @ExperimentalCoroutinesApi
    private fun refreshGlacierData() {
        glacierDataLiveData = liveData(Dispatchers.IO) {
            saveBearsApi.getGlacierChange().asCallbackFlow().catch { e ->
                // 에러 스트림 처리
                Timber.d(e)
            }.collect {
                // 최종적으로 데이터 스트림 받고 LiveData 로 Emit 함
                this.emit(it)
            }
        }
    }
}