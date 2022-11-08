package com.win.gestionderiesgos.ui.activity.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.ActivityHomeBinding
import com.win.gestionderiesgos.presentation.login.LoginViewModel
import com.win.gestionderiesgos.utils.Constants.ROLE

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding:ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //BottomNavigationView
        navController = findNavController(R.id.container_fragment)
        bottomNavigationView = binding.bottomNavigationView
        setupBottomNavigation()
        //DrawerLayout
        drawerLayout = binding.drawerLayout
        //NavigationUpButton
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        val navigationview: NavigationView = binding.navView
        navigationview.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    private fun setupBottomNavigation() {
        bottomNavigationView.setupWithNavController(navController)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_registerFuncionFragment -> navController.navigate(R.id.action_homeFragment_to_registerFuncionFragment)
                R.id.nav_registerActivityFragment->navController.navigate(R.id.action_homeFragment_to_registerActivityFragment)
            }
            drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }
}