package com.range.venus.ui.fragment

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation

class HomeViewModel : ViewModel(), Observable {

    private var view: View? = null

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    init {
        tvStudentName.value = "Ergashev Javohir"
        tvUniverName.value = "Farg`ona Politexnika Unstituti"
        tvGroupName.value = "Arxitektura, 10-20"
    }

    fun setView(mView: View) {
        view = mView
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
    }
}