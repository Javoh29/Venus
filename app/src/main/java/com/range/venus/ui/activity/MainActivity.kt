package com.range.venus.ui.activity

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.range.venus.App.Companion.isOnStart
import com.range.venus.R
import com.range.venus.ui.base.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun initView() {
        if (isOnStart){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()){
            finish()
        }
    }
}