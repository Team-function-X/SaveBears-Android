package com.junction.savebears.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junction.savebears.databinding.EcoChallengeListItemBinding
import com.junction.savebears.local.room.Challenge

class ChallengeListAdapter(
    val itemClick: (Challenge) -> Unit,
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
                itemClick(item)
            }

        }
    }

    fun addItem(items: List<Challenge>) { // 레퍼런스 타입이므로 분리
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}