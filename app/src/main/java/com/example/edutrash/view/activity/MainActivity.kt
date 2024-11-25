package com.example.edutrash.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.example.edutrash.view.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_category -> loadFragment(CategoryFragment())
                R.id.nav_scan_trash -> loadFragment(ScanTrashFragment())
                R.id.nav_redemption -> loadFragment(RedemptionFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }

        // Default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
