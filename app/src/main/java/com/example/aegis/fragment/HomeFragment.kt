package com.example.aegis.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aegis.R
import com.example.aegis.adapter.ServiceAdapter
import com.example.aegis.data.ServiceItem
import com.example.aegis.databinding.FragmentHomeBinding



@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var serviceAdapter: ServiceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val serviceItems = listOf(
            ServiceItem("Medical History", R.drawable.medical_history),
            ServiceItem("Insurance Plans", R.drawable.insurance_plans),
            ServiceItem("Privacy Advisor", R.drawable.privacy_advisor),
            ServiceItem("Nearest Medical Facility", R.drawable.medical_facility),
            ServiceItem("Track Medical Claims", R.drawable.medical_claims),
            ServiceItem("About Us", R.drawable.about_us),
            ServiceItem("Locate Our Branch", R.drawable.our_branch),
            ServiceItem("Live Chat", R.drawable.live_chat),
            // Add more items as needed
        )

        serviceAdapter = ServiceAdapter(requireContext(), serviceItems)
        binding.recyclerView.adapter = serviceAdapter

        serviceAdapter.setOnClickListener(object : ServiceAdapter.OnClickListener {
            override fun onClick(position: Int, serviceItem: ServiceItem) {
                // Handle item click
                val fragment: Fragment = when (serviceItem.name) {
                    "Nearest Medical Facility" -> HospitalFragment()
                    "Insurance Plans" -> InsuranceFragment()
                    else -> {
                        // Handle other cases or set to a default fragment if needed
                        HomeFragment() // Replace with a default fragment or handle other cases
                    }
                }
                navigateToFragment(fragment)
            }
        })

        return binding.root
    }
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null) // Add to back stack for back navigation
            .commit()
    }
}

