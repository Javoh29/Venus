package com.range.venus.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.databinding.ActivityLoginBinding
import com.range.venus.ui.base.BaseActivity
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    val apiService: ApiService by instance()
    val venusDao: VenusDao by instance()
    val unitProvider: UnitProvider by instance()

    override fun initView() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.setActivity(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }
}