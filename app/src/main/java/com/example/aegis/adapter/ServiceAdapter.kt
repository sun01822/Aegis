package com.example.aegis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aegis.data.ServiceItem
import com.example.aegis.databinding.ServicesLayoutBinding

class ServiceAdapter(
    private val context: Context,
    private val serviceItems: List<ServiceItem>
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private var mOnClickListener: OnClickListener? = null

    inner class ServiceViewHolder(private val binding: ServicesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mOnClickListener?.onClick(position, serviceItems[position])
                }
            }
        }

        fun bind(serviceItem: ServiceItem) {
            binding.titleTextView.text = serviceItem.name
            Glide.with(context).load(serviceItem.imageResId).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding =
            ServicesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(serviceItems[position])
    }

    override fun getItemCount(): Int {
        return serviceItems.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.mOnClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, serviceItem: ServiceItem)
    }
}
