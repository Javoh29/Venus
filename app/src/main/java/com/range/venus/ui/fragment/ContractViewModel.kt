package com.range.venus.ui.fragment

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation

class ContractViewModel : ViewModel(), Observable {

    private var view: View? = null

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    @Bindable
    val tvDebit = MutableLiveData<String>()

    init {
        tvStudentName.value = "Ergashev Javohir"
        tvUniverName.value = "Farg`ona Politexnika Unstituti"
        tvGroupName.value = "Arxitektura, 10-20"
        tvDebit.value = "-3 848 000"
    }

    fun setView(mView: View) {
        view = mView
    }

    fun onBack() {
        Navigation.findNavController(view!!).popBackStack()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun onCleared() {
        super.onCleared()
        view = null
    }
}