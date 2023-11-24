package com.example.aegis.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aegis.R
import com.example.aegis.adapter.InsuranceAdapter
import com.example.aegis.data.InsuranceItem
import com.example.aegis.databinding.FragmentInsuranceBinding

class InsuranceFragment : Fragment() {
    private lateinit var binding : FragmentInsuranceBinding
    private lateinit var insuranceAdapter: InsuranceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInsuranceBinding.inflate(layoutInflater)

        val insuranceItems = listOf(
            InsuranceItem("Family Policy", R.drawable.family_policy),
            InsuranceItem("House Insurance", R.drawable.house_insurance),
            InsuranceItem("General Insurance", R.drawable.general_insurance),
            InsuranceItem("Umbrella Policy", R.drawable.umbrella_policy),
            InsuranceItem("Short Term Policy", R.drawable.short_term_policy),
            InsuranceItem("Disability Policy", R.drawable.disability_policy),
            InsuranceItem("Long-term Policy", R.drawable.long_term_policy),
            InsuranceItem("Health Insurance", R.drawable.health_insurance),
            InsuranceItem("Savings & Profits", R.drawable.savings_profits),
            InsuranceItem("Long-term Benefits", R.drawable.long_term_benefits),
        )

        insuranceAdapter = InsuranceAdapter(requireContext(), insuranceItems)
        binding.recyclerView.adapter = insuranceAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }
}