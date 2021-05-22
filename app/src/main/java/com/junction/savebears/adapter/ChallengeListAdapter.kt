package com.junction.savebears.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.junction.savebears.R
import com.junction.savebears.component.ext.loadUri
import com.junction.savebears.component.ext.toBitmap
import com.junction.savebears.component.ext.toSimpleString
import com.junction.savebears.databinding.ChallengeListItemBinding
import com.junction.savebears.local.room.Challenge
import com.junction.savebears.view.ChallengeDetailActivity

class ChallengeListAdapter(
    val itemClick: (Challenge) -> Unit,
    ) : RecyclerView.Adapter<ChallengeListAdapter.ViewHolder>() {

    private val items = mutableListOf<Challenge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ChallengeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    inner class ViewHolder(
        private val binding: ChallengeListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Challenge) {
            binding.root.setOnClickListener {
                itemClick(item)
            }

            // Comment 가 없으면 기본 문구 넣음
            if(item.comment.isNotBlank()){
                binding.tvComment.text = item.comment
            }else{
                binding.tvComment.text = "A Proud Eco Challenge Record"
            }
            binding.tvDate.text = item.missionCompleteDate

        }
    }

    fun addItem(items: List<Challenge>) { // 레퍼런스 타입이므로 분리
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}