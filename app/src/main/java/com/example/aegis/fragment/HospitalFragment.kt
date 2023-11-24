package com.example.aegis.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aegis.R
import com.example.aegis.adapter.HospitalAdapter
import com.example.aegis.data.HospitalItem
import com.example.aegis.databinding.FragmentHospitalBinding


class HospitalFragment : Fragment() {
    private lateinit var binding: FragmentHospitalBinding
    private lateinit var hospitalAdapter: HospitalAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHospitalBinding.inflate(layoutInflater)
        val hospitalItems = listOf(
            HospitalItem("Prince Court", "39, Jalan Kia Peng, 50450 Kuala Lumpur,\n" +
                    "Wilayah Persekutuan", R.drawable.price_court),
            HospitalItem("Gleneagles Hospital Kuala Lumpur", "286 50450 Kuala Lumpur, Malaysia", R.drawable.ghkl_hospital),
            HospitalItem("Pantai Hospital Kuala Lumpur", "8 Jalan Bukit Pantai, 59100 Kuala Lumpur, Malaysia", R.drawable.phkl_hospital),
            HospitalItem("KPJ Ampang Puteri Hospital", "", R.drawable.kap_hospital),
            HospitalItem(" ParkCity Medical Center", "2, Jalan Intisari, Desa Parkcity, 52200 Kuala Lumpur, Wilayah Persekutuan" , R.drawable.pmc_hospital),
            HospitalItem("Ara Damansara Medical Centre", "Lot 2, Jalan Lapangan Terbang Subang, Seksyen U2 40150 Shah Alam, Selangor, Malaysia", R.drawable.admc_hospital),
            HospitalItem("Thomson Hospital", "11, Jln Teknologi, PJU 5, Kota Damansara 47810 Petaling Jaya, Selangor, Malaysia", R.drawable.thomson_hospital)
        )

        // Set text to editTextLocation after the layout is fully inflated
        binding.editTextLocation.setText("Jalan Technologi, Kota Damansara")

        hospitalAdapter = HospitalAdapter(requireContext(), hospitalItems)
        binding.recyclerView.adapter = hospitalAdapter

        return binding.root
    }

}