package com.junction.savebears.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.junction.savebears.R
import com.junction.savebears.component.ext.loadUri
import com.junction.savebears.component.ext.toBitmap
import com.junction.savebears.component.ext.toSimpleString
import com.junction.savebears.databinding.EcoChallengeListItemBinding
import com.junction.savebears.local.room.Challenge

class ChallengeListAdapter(
    private val clickListener: ChallengeSelectionListener
) : RecyclerView.Adapter<ChallengeListAdapter.ViewHolder>() {

    private val items = mutableListOf<Challenge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            EcoChallengeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    inner class ViewHolder(
        private val binding: EcoChallengeListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Challenge) {
            binding.root.setOnClickListener {
                clickListener.onChallengeClick(item)
            }

            // 시그니쳐
            if (item.imageSignature.toBitmap() == null) {
                binding.ivSignature.setImageBitmap(item.imageSignature.toBitmap())
            } else {
                binding.ivSignature.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_launcher_foreground))
            }

            // 실사
            binding.ivMain.loadUri(item.imageStrUri.toUri()) {
                placeholder(R.mipmap.ic_launcher)
                centerCrop()
            }

            // 날짜
            binding.tvDate.text = item.missionCompleteDate.toSimpleString()

            // 코멘트
            binding.tvComment.text = item.comment
        }
    }

    fun addItem(items: List<Challenge>) { // 레퍼런스 타입이므로 분리
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

interface ChallengeSelectionListener {
    fun onChallengeClick(item: Challenge)
}