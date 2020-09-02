package com.range.venus.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.databinding.ActivityLoginBinding
import com.range.venus.ui.base.BaseActivity

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun initView() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.setActivity(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }
}