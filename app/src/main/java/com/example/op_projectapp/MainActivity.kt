package com.example.op_projectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.op_projectapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navController = binding.frgNav.getFragment<NavHostFragment>().navController
        setupActionBarWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)
        setContentView(binding.root)

    }

    override fun onSupportNavigateUp(): Boolean {
        // 네비게이션컨드롤러를 다시 가져와서 뒤로가기 동작 처리
        val navController = binding.frgNav.getFragment<NavHostFragment>().navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}