package com.junction.savebears.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.junction.savebears.databinding.RvGlacierInfoBinding
import com.junction.savebears.model.Glacier

class GlacierInfoAdapter : RecyclerView.Adapter<GlacierInfoAdapter.ViewHolder>() {

    private val items = mutableListOf<Glacier>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RvGlacierInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    fun addItem(items: List<Glacier>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: RvGlacierInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Glacier) {
            binding.root.setOnClickListener {

            }
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.ivGlacier)

            binding.tvPeriod.text = item.date
        }
    }
}