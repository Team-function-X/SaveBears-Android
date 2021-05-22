package com.junction.savebears.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.jakewharton.rxbinding4.view.clicks
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.ext.openActivity
import com.junction.savebears.component.ext.toastLong
import com.junction.savebears.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@FlowPreview
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    enum class Glacier(val value: Int) { Safe(1), Normal(2), Dangerous(3) }
    enum class Result { Success, Fail }

    private val challengeLauncher: ActivityResultLauncher<Bundle> = // 첼린지 런처
        registerForActivityResult(ChallengeLauncherContract()) { result: Result ->
            val data =
                when (result) {
                    Result.Success -> Glacier.Safe
                    Result.Fail -> Glacier.Dangerous
                }.value

            showGlacierData(data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressShow()
        setOnListeners()
        showGlacierData(Glacier.Normal.value)
    }

    private fun setOnListeners() {
        binding.faboption1 // 빙하 용해
            .clicks()
            .doOnNext {
                Timber.d("2번")
            }
            .debounce(DEBOUNCED_TIME, TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { GlacierInfoActivity::class.java }
            .subscribe(::openActivity, Timber::e)

        binding.faboption2 // 첼린지 목록
            .clicks()
            .debounce(DEBOUNCED_TIME, TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { ChallengeListActivity::class.java }
            .subscribe({
                Timber.d("1번!!")
                openActivity(it)
            }, Timber::e)

        binding.challengeArrivedLayout // 첼린지
            .clicks()
            .debounce(DEBOUNCED_TIME, TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { RegisterChallengeActivity::class.java }
            .subscribe({ challengeLauncher.launch(bundleOf()) }, Timber::e)
    }

    private fun showGlacierData(value: Int) {
        val drawableId = resources.getIdentifier(
            String.format("ic_polar_%s", value),
            "drawable",
            packageName
        )
        val msgId = resources.getIdentifier(
            String.format("glacier_result_%s", value),
            "string",
            packageName
        )

        toastLong(msgId)
        binding.ivPolar.setImageResource(drawableId)
        progressHide()
    }

    private fun progressShow() {
        binding.loadingView.progress.isVisible = true
    }

    private fun progressHide() {
        binding.loadingView.progress.isVisible = false
    }

    inner class ChallengeLauncherContract : ActivityResultContract<Bundle, Result>() {
        override fun createIntent(context: Context, bundle: Bundle): Intent =
            Intent(context, RegisterChallengeActivity::class.java).apply {
                intent.putExtras(bundle)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Result? =
            when (resultCode) {
                Activity.RESULT_OK -> intent?.extras?.get(RESULT_KEY) as Result
                else -> null
            }
    }

    companion object {
        private const val DEBOUNCED_TIME = 200L
        const val RESULT_KEY = "Result"
    }
}