package com.range.venus.ui.activity

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.db.entities.UserModel
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.ui.base.BaseActivity
import org.kodein.di.generic.instance

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    val apiService: ApiService by instance()
    val venusDao: VenusDao by instance()
    val unitProvider: UnitProvider by instance()
    private var userModel: UserModel? = null

    override fun initView() {
        if (unitProvider.getUserID().isEmpty()){
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