package com.win.gestionderiesgos.ui.activity.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.win.gestionderiesgos.R
import com.win.gestionderiesgos.databinding.ActivityHomeBinding
import com.win.gestionderiesgos.databinding.ActivityHomeClientBinding

class HomeClientActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityHomeClientBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //BottomNavigationView
        navController = findNavController(R.id.container_fragment_client)
        bottomNavigationView = binding.bottomNavigationView
        setupBottomNavigation()
        //DrawerLayout
        drawerLayout = binding.drawerLayout
        //NavigationUpButton
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


        NavigationUI.setupWithNavController(binding.navViewClient, navController)

        val navigationview: NavigationView = binding.navViewClient
        navigationview.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    private fun setupBottomNavigation() {
        bottomNavigationView.setupWithNavController(navController)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}