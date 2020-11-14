package com.range.venus.ui.activity

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.range.venus.R
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.ui.base.BaseActivity
import com.range.venus.utils.ContextWrap
import org.kodein.di.generic.instance
import java.util.*

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private val unitProvider: UnitProvider by instance()

    override fun initView() {
        if (unitProvider.getUserID().isEmpty()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun attachBaseContext(newBase: Context) {
        val locale = if (PreferenceManager.getDefaultSharedPreferences(newBase).getBoolean("LANGUAGE", true)) {
            Locale("uz", "UZ")
        } else {
            Locale("ru", "RU")
        }
        val context: Context = ContextWrap.wrap(newBase, locale)
        super.attachBaseContext(context)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()){
            finish()
        }
    }
}