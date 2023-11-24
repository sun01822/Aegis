package com.example.aegis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aegis.data.HospitalItem
import com.example.aegis.databinding.HospitalLayoutBinding

class HospitalAdapter(
    private val context: Context,
    private val hospitalItems: List<HospitalItem>
) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    inner class HospitalViewHolder(private val binding: HospitalLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hospitalItem: HospitalItem) {
            binding.textViewHospitalName.text = hospitalItem.name
            binding.textViewAddress.text = hospitalItem.address

            // Load image using Glide
            Glide.with(context)
                .load(hospitalItem.imageResId)
                .into(binding.imageViewHospital)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val binding =
            HospitalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        holder.bind(hospitalItems[position])
    }

    override fun getItemCount(): Int {
        return hospitalItems.size
    }
}