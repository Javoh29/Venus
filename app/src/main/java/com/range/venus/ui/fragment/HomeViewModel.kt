package com.range.venus.ui.fragment

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.range.venus.App.Companion.isLoadData
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(), Observable {

    private var view: View? = null
    private var venusDao: VenusDao? = null
    private var apiService: ApiService? = null
    private var unitProvider: UnitProvider? = null
    private var userID: String = ""
    var message = MutableLiveData<Int>()
    var showDialog = MutableLiveData<Boolean>()

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    fun setView(mView: View, mVenusDao: VenusDao, api: ApiService, unit: UnitProvider) {
        view = mView
        venusDao = mVenusDao
        apiService = api
        unitProvider = unit
        userID = unit.getUserID()
        loadData()
    }

    private fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        if (isLoadData && unitProvider!!.isOnline()) {
            viewModelScope.launch(Dispatchers.Main) { showDialog.value = true }
            val params: HashMap<String, String> = HashMap()
            params["user_id"] = userID
            val response = apiService?.getPayments(params)

            if (response!!.isSuccessful && response.body()!!.isNotEmpty()) {
                response.body()!!.forEach {
                    venusDao?.insertPayments(it)
                }
                isLoadData = false
                viewModelScope.launch(Dispatchers.Main) {
                    message.value = R.string.text_data_loaded
                    message.value = 0
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    message.value = R.string.text_err_loading_data
                }
            }

            val response2 = apiService?.getDebit(params)
            if (response2!!.isSuccessful && response2.body()!!.isNotEmpty()) {
                venusDao?.insertDebit(response2.body()!![0])
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    message.value = R.string.text_err_loading_data
                }
            }

            viewModelScope.launch(Dispatchers.Main) { showDialog.value = false }
        }
        getUser()
    }

    private fun getUser() = viewModelScope.launch {
        val model = lazyDeferred { venusDao?.getUser() }.value.await()
        if (model != null) {
            GlobalScope.launch(Dispatchers.Main) {
                tvStudentName.value = model.fio
                tvUniverName.value = model.universiteti
                tvGroupName.value = model.guruhi
            }
        }else return@launch
    }

    fun openContract() {
        Navigation.findNavController(view!!)
            .navigate(HomeFragmentDirections.actionHomeFragmentToContractFragment())
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun onCleared() {
        super.onCleared()
        view = null
        apiService = null
        venusDao = null
    }
}