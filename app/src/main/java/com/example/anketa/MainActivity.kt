package com.example.anketa

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anketa.data.Role
import com.example.anketa.databinding.ActivityMainBinding
import com.example.anketa.screen.NavBarCallbacks
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), NavBarCallbacks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        setupNavBottomDestinations(navController)
        checkLoginStatus()
        navView.setupWithNavController(navController)
    }


    private fun checkLoginStatus() {
        if (!prefs.isProfileDataSet) {
            binding.navView.visibility = View.GONE
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_login)
        }
    }

    private fun setupNavBottomDestinations(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_main -> {
                    showNavBar()
                }
                R.id.navigation_profile_employer -> {
                    if (prefs.role == Role.Employee) {
                        navController.navigate(R.id.navigation_edit_profile_employee)
                    }
                }
                R.id.navigation_edit_profile_employee -> {
                    binding.navView.menu.findItem(R.id.navigation_profile_employer).isChecked = true
                }
                R.id.navigation_list -> {
                    binding.navView.menu.findItem(R.id.navigation_main).isChecked = true
                }
            }
        }
    }

    override fun showNavBar() {
        binding.navView.visibility = View.VISIBLE
    }

    override fun hideNavBar() {
        binding.navView.visibility = View.GONE
    }
}