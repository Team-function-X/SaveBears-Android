package com.junction.savebears.view

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.junction.savebears.adapter.GlacierInfoAdapter
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.ext.openActivity
import com.junction.savebears.databinding.ActivityGlacierInfoBinding

class GlacierInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityGlacierInfoBinding
    private val adapter = GlacierInfoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlacierInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewAdapter()
        showAllGlacierInfos()
        binding.btnGraph.setOnClickListener {
            openActivity(GlacierGraphActivity::class.java)
        }
    }

    private fun setRecyclerViewAdapter() {
        binding.rvGlacierInfo.apply {
            adapter = this@GlacierInfoActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun showAllGlacierInfos() {
        // 성공 시 보여주기
        adapter.addItem(listOf())
    }

}