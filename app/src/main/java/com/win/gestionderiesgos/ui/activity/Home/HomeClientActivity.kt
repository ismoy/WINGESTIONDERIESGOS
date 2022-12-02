package com.win.gestionderiesgos.ui.activity.Home

import android.content.Intent
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
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.databinding.ActivityHomeBinding
import com.win.gestionderiesgos.databinding.ActivityHomeClientBinding
import com.win.gestionderiesgos.ui.activity.MainActivity

class HomeClientActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeClientBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var mAuthProvider: AuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuthProvider = AuthProvider()
        //BottomNavigationView
        navController = findNavController(R.id.container_fragment_client)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.singOut ->singOut()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun singOut() {
        mAuthProvider.logout()
        startActivity(Intent(this,MainActivity::class.java)).apply {  }
    }
}