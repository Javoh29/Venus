package com.range.venus.ui.activity

import android.content.Intent
import com.range.venus.R
import com.range.venus.ui.base.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun initView() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}