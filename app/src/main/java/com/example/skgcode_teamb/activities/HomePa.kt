package com.example.skgcode_teamb.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_pa.*
import kotlinx.android.synthetic.main.containt_main.*

class HomePa : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pa)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,drawer,toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigation_menu.setNavigationItemSelectedListener(this)

        setToolbarTitle("Home")
        changeFragment(Home())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawer(GravityCompat.START)

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
                setToolbarTitle("Log Out")
                changeFragment(Logout())
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