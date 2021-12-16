package com.example.skgcode_teamb.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.databinding.ActivityHomePaBinding
import com.example.skgcode_teamb.fragments.*
import com.example.skgcode_teamb.storage.SessionManager
import com.google.android.material.navigation.NavigationView


class HomePa : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    private var _binding: ActivityHomePaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityHomePaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.contentMain.toolbar)

        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.contentMain.toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navMenu.setNavigationItemSelectedListener(this)

        setToolbarTitle("Home")
        changeFragment(Home())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val sessionManager = SessionManager(this)
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        when(item.itemId){
            R.id.nav_home -> {
                setToolbarTitle("Home")
                changeFragment(Home())
            }

            R.id.nav_UserProfile -> {
                setToolbarTitle("User Profile")
                changeFragment(UserProfile())
            }

            R.id.nav_Prescriptions -> {
                setToolbarTitle("Prescriptions")
                changeFragment(Prescripti())
            }

            R.id.nav_Diagnoses -> {
                setToolbarTitle("Diagnoses")
                changeFragment(Diagnoses())
            }

            R.id.nav_Appointments -> {
                setToolbarTitle("Appointments")
                changeFragment(Appointments())
            }

            R.id.nav_Schedule_Appointments -> {
                setToolbarTitle("Schedule Appointments")
                changeFragment(SceduleApp())
            }

            R.id.nav_LogOut -> {
                //setToolbarTitle("Log Out")
                //changeFragment(Logout())

                    // Clear session details
                    sessionManager.clearSession()

                    // After clear session details, redirect user to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)

                    // Closing all the Activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    // Add new flag to start new Activity
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    // Staring Login Activity
                    startActivity(intent)
                    finish()

            }

        }


        return true
    }

    fun setToolbarTitle(Title:String){
        supportActionBar?.title = title
    }

    fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container,frag).commit()
    }
}