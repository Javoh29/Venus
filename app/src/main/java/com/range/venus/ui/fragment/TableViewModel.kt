package com.range.venus.ui.fragment

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.range.venus.data.db.VenusDao
import com.range.venus.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TableViewModel : ViewModel(), Observable {

    private var view: View? = null
    private var venusDao: VenusDao? = null
    var message = MutableLiveData<Int>()

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    fun setView(mView: View, mVenusDao: VenusDao) {
        view = mView
        venusDao = mVenusDao
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
        } else return@launch
    }

    fun onBack() {
        Navigation.findNavController(view!!).popBackStack()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}