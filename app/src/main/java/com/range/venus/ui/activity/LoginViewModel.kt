package com.range.venus.ui.activity

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.range.venus.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel: ViewModel(), Observable {

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
        when {
            inputUserID.value == null -> {
                Toast.makeText(
                    activity,
                    activity?.getString(R.string.text_err_id_enter),
                    Toast.LENGTH_LONG
                ).show()
            }
            inputPassword.value == null -> {
                Toast.makeText(
                    activity,
                    activity?.getString(R.string.text_err_password_enter),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                enable.value = false
                progress.value = View.VISIBLE
                val params: HashMap<String, String> = HashMap()
                params["user_id"] = inputUserID.value!!
                params["pass"] = inputPassword.value!!
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        val response = activity!!.apiService.checkLogin(params)
                        if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                            activity!!.venusDao.insertUser(response.body()!![0])
                            activity!!.unitProvider.saveUserID(response.body()!![0].userId)
                            activity!!.unitProvider.savePassword(response.body()!![0].parol)
                            activity?.startActivity(Intent(activity, MainActivity::class.java))
                            activity?.finish()
                        } else if (response.isSuccessful && response.body()!!.isEmpty()) {
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.text_wrong_password),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.text_err_connect),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        activity?.runOnUiThread {
                            enable.value = true
                            progress.value = View.INVISIBLE
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun onCleared() {
        super.onCleared()
        activity = null
    }

}