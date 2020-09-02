package com.range.venus.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.range.venus.R
import com.range.venus.databinding.ActivityLoginBinding
import com.range.venus.ui.base.BaseActivity

class LoginActivity : BaseActivity(R.layout.activity_login) {

    private lateinit var singInViewModel: SingInViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun initView() {
        singInViewModel = ViewModelProvider(this).get(SingInViewModel::class.java)
        singInViewModel.setActivity(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = singInViewModel
        binding.lifecycleOwner = this
    }
}