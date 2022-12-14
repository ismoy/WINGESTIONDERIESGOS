package com.win.gestionderiesgos.ui.activity.Home

import android.content.Intent
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
import com.win.gestionderiesgos.ui.activity.MainActivity

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding:ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController:NavController
    private lateinit var mAuthProvider: AuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuthProvider= AuthProvider()
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
                R.id.fragment_register_funcion -> navController.navigate(R.id.action_homeFragment_to_registerFuncionFragment)
                R.id.fragment_register_activity->navController.navigate(R.id.action_homeFragment_to_registerActivityFragment)
                R.id.fragment_register_risk->navController.navigate(R.id.action_homeFragment_to_registerRiskFragment)
                R.id.fragment_register_project->navController.navigate(R.id.action_homeFragment_to_registerProjectFragment)
                R.id.fragment_show_risk->navController.navigate(R.id.action_homeFragment_to_showListRiskOnlyAdminFragment)
                R.id.fragment_show_activities->navController.navigate(R.id.action_homeFragment_to_listActivitiesFragment)
                R.id.singOut->singOut()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun singOut() {
        mAuthProvider.logout()
        startActivity(Intent(this,MainActivity::class.java)).apply {  }
    }
}