package com.example.aegis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aegis.data.InsuranceItem
import com.example.aegis.databinding.InsuranceLayoutBinding

class InsuranceAdapter(
    private val context: Context,
    private val insuranceItems: List<InsuranceItem>
) : RecyclerView.Adapter<InsuranceAdapter.InsuranceViewHolder>() {

    inner class InsuranceViewHolder(private val binding: InsuranceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(insuranceItem: InsuranceItem) {
            binding.titleTextView.text = insuranceItem.name
            Glide.with(context).load(insuranceItem.imageResId).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsuranceViewHolder {
        val binding = InsuranceLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InsuranceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InsuranceViewHolder, position: Int) {
        holder.bind(insuranceItems[position])
    }

    override fun getItemCount(): Int {
        return insuranceItems.size
    }
}
