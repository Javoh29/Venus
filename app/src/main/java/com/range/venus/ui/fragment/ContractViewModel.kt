package com.range.venus.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.model.DateModel
import com.range.venus.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
class ContractViewModel : ViewModel(), Observable {

    private var view: View? = null
    private var venusDao: VenusDao? = null
    private val decimalFormat: DecimalFormat = DecimalFormat("###,###.##")
    private var course: Int = 0
    private val simpleYear = SimpleDateFormat("yyyy")
    private val simpleMoth = SimpleDateFormat("MM")
    private val simpleDate = SimpleDateFormat("MM.yyyy")
    val message = MutableLiveData<String>()
    val dialogTop = MutableLiveData<Boolean>()
    val listDate = MutableLiveData<List<DateModel>>()
    val radioChecked = MutableLiveData<Int>()
    val listYear: ArrayList<String> = ArrayList()

    @Bindable
    val spinnerVisible = MutableLiveData<Int>()

    @Bindable
    val selectYear = MutableLiveData<Int>()

    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    @Bindable
    val tvDebit = MutableLiveData<String>()

    @Bindable
    val tvSom = MutableLiveData<String>()

    fun setView(mView: View, mVenusDao: VenusDao) {
        view = mView
        venusDao = mVenusDao
        loadData()

        radioChecked.value = R.id.radioAll

        radioChecked.observeForever {
            if (it != null) {
                if (it == R.id.radioMoth) {
                    spinnerVisible.value = View.VISIBLE
                } else {
                    spinnerVisible.value = View.GONE
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = lazyDeferred { venusDao?.getUser() }.value.await()
            if (model != null) {
                viewModelScope.launch(Dispatchers.Main) {
                    tvStudentName.value = model.fio
                    tvUniverName.value = model.universiteti
                    tvGroupName.value = "${model.yonalishi}, ${model.guruhi}"
                }
                viewModelScope.launch(Dispatchers.IO) {
                    course = model.kursi.toInt()
                    bindSortAll()
                }
            } else return@launch
        }
        viewModelScope.launch(Dispatchers.IO) {
            val model = lazyDeferred { venusDao?.getDebit() }.value.await()
            if (model != null) {
                viewModelScope.launch(Dispatchers.Main) {
                    tvDebit.value = decimalFormat.format(model.summa.toInt())
                    if (model.summa.substring(0, 1) == "-") {
                        tvSom.value =
                            "${view!!.context.getString(R.string.text_som)} (${view!!.context.getString(R.string.text_debit)})"
                    } else {
                        tvSom.value =
                            "${view!!.context.getString(R.string.text_som)} (${view!!.context.getString(R.string.text_credit)})"
                    }
                }
            } else return@launch
        }
    }

    private fun bindSortAll() {
        val list: ArrayList<DateModel> = ArrayList()
        listYear.clear()
        val currentYear = simpleYear.format(Date()).toInt()
        val currentDate = simpleDate.format(Date())
        var oldYear: Int = if (course == 1) {
            if (simpleMoth.format(Date()).toInt() > 9) {
                listYear.add(currentYear.toString())
                simpleYear.format(Date()).toInt()
            } else {
                var y = currentYear
                for (i in 1..course) {
                    listYear.add(y--.toString())
                }
                simpleYear.format(Date()).toInt() - course
            }
        } else {
            var y = currentYear
            for (i in 1..course) {
                listYear.add(y--.toString())
            }
            simpleYear.format(Date()).toInt() - course
        }
        var end = true
        var moth = 9
        var m: String
        while (end) {
            m = if (moth < 10) {
                "0$moth"
            } else moth.toString()
            if (oldYear == currentYear) {
                list.add(DateModel(textDate = getMoth(moth), formatDate = "${m}.$oldYear"))
            } else {
                list.add(
                    DateModel(
                        textDate = getMoth(moth) + " $oldYear",
                        formatDate = "${m}.$oldYear"
                    )
                )
            }
            when {
                currentDate == "$m.${oldYear}" -> {
                    end = false
                }
                moth == 12 -> {
                    moth = 1
                    oldYear++
                }
                else -> moth++
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            listDate.value = list
        }
    }

    private fun bindSortYear() {
        val list: ArrayList<DateModel> = ArrayList()
        listYear.forEach {
            list.add(DateModel(textDate = "$it ${view!!.context.getString(R.string.text_year)}", formatDate = it))
        }
        viewModelScope.launch(Dispatchers.Main) {
            listDate.value = list
        }
    }

    private fun bindSortMoth() {
        val list: ArrayList<DateModel> = ArrayList()
        if (listYear[selectYear.value!!] == simpleYear.format(Date())) {
            val moth: Int = simpleMoth.format(Date()).toInt()
            if (course == 1) {
                if (moth >= 9) {
                    for (i in 9..moth) {
                        list.add(DateModel(textDate = getMoth(i), formatDate = "${i}.${listYear[selectYear.value!!]}"))
                    }
                } else {
                    for (i in 1..moth) {
                        list.add(DateModel(textDate = getMoth(i), formatDate = "${i}.${listYear[selectYear.value!!]}"))
                    }
                }
            } else {
                for (i in 1..moth) {
                    list.add(DateModel(textDate = getMoth(i), formatDate = "${i}.${listYear[selectYear.value!!]}"))
                }
            }
        } else {
            for (i in 1..12) {
                list.add(DateModel(textDate = "${getMoth(i)} ${listYear[selectYear.value!!]}", formatDate = "${i}.${listYear[selectYear.value!!]}"))
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            listDate.value = list
        }
    }

    private fun getMoth(moth: Int): String {
        return when (moth) {
            1 -> view!!.context.getString(R.string.text_yan)
            2 -> view!!.context.getString(R.string.text_fev)
            3 -> view!!.context.getString(R.string.text_mar)
            4 -> view!!.context.getString(R.string.text_apr)
            5 -> view!!.context.getString(R.string.text_may)
            6 -> view!!.context.getString(R.string.text_jun)
            7 -> view!!.context.getString(R.string.text_jul)
            8 -> view!!.context.getString(R.string.text_avg)
            9 -> view!!.context.getString(R.string.text_snt)
            10 -> view!!.context.getString(R.string.text_okt)
            11 -> view!!.context.getString(R.string.text_nyb)
            12 -> view!!.context.getString(R.string.text_dek)
            else -> ""
        }
    }

    fun openDialog() {
        dialogTop.value = true
    }

    fun closeDialog() {
        dialogTop.value = false
    }

    fun enterDialog() {
        when (radioChecked.value) {
            R.id.radioYear -> bindSortYear()
            R.id.radioMoth -> bindSortMoth()
            R.id.radioAll -> bindSortAll()
        }
        dialogTop.value = false
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