package com.example.kotlinproject

import android.content.Intent
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kotlinproject.activity.LoginActivity
import com.example.kotlinproject.fragment.AkunFragment
import com.example.kotlinproject.fragment.FavoriteFragment
import com.example.kotlinproject.fragment.HomeFragment
import com.example.kotlinproject.fragment.KernajangFragment
import com.example.kotlinproject.helper.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val fragmentHome:Fragment =HomeFragment()
    val fragmentFavorite:Fragment =FavoriteFragment()
    val fragmentAkun:Fragment =AkunFragment()
    val fragmentKernajang:Fragment =KernajangFragment()
    val fm :FragmentManager =supportFragmentManager
    var active :Fragment =fragmentHome

    private lateinit var menu:Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s= SharedPref(this)
        setUpBottomNav()
    }

    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentFavorite).hide(fragmentFavorite).commit()
        fm.beginTransaction().add(R.id.container, fragmentKernajang).hide(fragmentKernajang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    callFargment(0, fragmentHome)
                }

                R.id.navigation_favorite -> {
                    callFargment(1, fragmentFavorite)
                }

                R.id.navigation_kernajing -> {
                    callFargment(2, fragmentKernajang)
                }

                R.id.navigation_akun -> {
                    if (s.getStatusLogin()){
                        callFargment(3, fragmentAkun)
                    }else{
                        startActivity(Intent(this,LoginActivity::class.java))
                    }

                }
            }

            false
        }
    }

    fun callFargment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}