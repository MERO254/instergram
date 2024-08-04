package com.example.instargram

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(savedInstanceState == null){
            var bundle = Bundle()
            var fragment = HomeFragment()

            fragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment()).commit()
        }

        var bottomnav:BottomNavigationView = findViewById(R.id.bottomnav)

        bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, seachFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.reals -> {
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, RealsFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.account ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, AcountFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

    }
}