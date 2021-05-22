package com.junction.savebears.remote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junction.savebears.databinding.RvChallengeItemBinding
import com.junction.savebears.local.room.Challenge

class ChallengeAdapter : RecyclerView.Adapter<ChallengeAdapter.ChallengeHolder>() {

    private val items = mutableListOf<Challenge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeHolder =
        ChallengeHolder(
            RvChallengeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ChallengeHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    fun addItem(items: List<Challenge>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ChallengeHolder(private val binding: RvChallengeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Challenge) {
            // TODO with binding
            binding.ivChallenge
            binding.tvComment
        }
    }
}