package com.range.venus.ui.fragment

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.db.entities.DebitModel
import com.range.venus.data.db.entities.UserModel
import com.range.venus.utils.lazyDeferred
import kotlinx.android.synthetic.main.item_contract_container.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.abs

class ContractViewModel : ViewModel(), Observable {

    private var view: View? = null
    private var venusDao: VenusDao? = null
    private val decimalFormat: DecimalFormat = DecimalFormat("###,###.##")

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    @Bindable
    val tvDebit = MutableLiveData<String>()

    val tvSom = MutableLiveData<String>()

    fun setView(mView: View, mVenusDao: VenusDao) {
        view = mView
        venusDao = mVenusDao
        loadData()
    }

    private fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        val model = lazyDeferred { venusDao?.getUser() }.value.await()
        val debit = lazyDeferred { venusDao?.getDebit() }.value.await()
        if (model != null && debit != null) {
            bindUI(model, debit)
        } else return@launch
    }

    private fun bindUI(model: UserModel, debit: DebitModel) =
        viewModelScope.launch(Dispatchers.Main) {
            tvStudentName.value = model.fio
            tvUniverName.value = model.universiteti
            tvGroupName.value = "${model.yonalishi}, ${model.guruhi}"
            tvDebit.value = decimalFormat.format(abs(debit.summa.toInt()))
            if (debit.summa.substring(0, 1) == "-") {
                tvSom.value = "${view!!.context.getString(R.string.text_som)} (${view!!.context.getString(R.string.text_debit)})"
            } else {
                tvSom.value = "${view!!.context.getString(R.string.text_som)} (${view!!.context.getString(R.string.text_credit)})"
            }
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