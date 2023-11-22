package com.example.aegis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.aegis.databinding.ActivityMainBinding  // Update with your actual package name
import com.example.aegis.fragment.HospitalFragment
import com.example.aegis.fragment.HomeFragment
import com.example.aegis.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the initial fragment to HomeFragment
        setFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            val selectedFragment = when (it.itemId) {
                R.id.hospital -> HospitalFragment()
                R.id.profile -> ProfileFragment()
                else -> HomeFragment()
            }
            setFragment(selectedFragment)
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
