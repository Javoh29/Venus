package com.range.venus.ui.activity

import android.content.Intent
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.range.venus.App.Companion.isOnStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(), Observable {

    private var activity: LoginActivity? = null

    @Bindable
    val inputUserID = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()

    @Bindable
    val progress = MutableLiveData<Int>()

    @Bindable
    val enable = MutableLiveData<Boolean>()

    init {
        progress.value = View.INVISIBLE
        enable.value = true
    }

    fun setActivity(mActivity: LoginActivity) {
        activity = mActivity
    }

    fun enterLogin() = viewModelScope.launch {
        progress.value = View.VISIBLE
        delay(2000)
        progress.value = View.INVISIBLE
        if (activity != null){
            isOnStart = false
            activity?.startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}