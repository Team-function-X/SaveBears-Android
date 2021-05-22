package com.junction.savebears.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junction.savebears.databinding.EcoChallengeListItemBinding
import com.junction.savebears.local.room.ChallengeItem

class ChallengeListAdapter(
    val itemClick: (ChallengeItem) -> Unit
) : RecyclerView.Adapter<ChallengeListAdapter.ViewHolder>() {
    private var items: List<ChallengeItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            EcoChallengeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(
        private val binding: EcoChallengeListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChallengeItem) {


            binding.root.setOnClickListener {
                itemClick(item)
            }
        }
    }

    fun setItem(items: ArrayList<ChallengeItem>) {
        this.items = items
        notifyDataSetChanged()
    }


}