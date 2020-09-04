package com.range.venus.ui.fragment

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
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
    lateinit var dialogCalendar: DialogPlus
    val listDate = MutableLiveData<List<DateModel>>()

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
        dialogCalendar = DialogPlus.newDialog(view!!.context)
                .setContentHolder(ViewHolder(R.layout.dialog_filter))
                .setGravity(Gravity.TOP)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFooter(null)
                .setExpanded(false)
                .create()
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
                    bindList(model.kursi.toInt())
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

    private fun bindList(course: Int) {
        val list: ArrayList<DateModel> = ArrayList()
        val simpleMoth = SimpleDateFormat("MM")
        val simpleYear = SimpleDateFormat("yyyy")
        val simpleDate = SimpleDateFormat("MM.yyyy")

        val currentYear = simpleYear.format(Date()).toInt()
        val currentDate = simpleDate.format(Date())
        var oldYear: Int = if (course == 1) {
            if (simpleMoth.format(Date()).toInt() > 9) {
                simpleYear.format(Date()).toInt()
            } else {
                simpleYear.format(Date()).toInt() - course
            }
        } else {
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

    private fun getMoth(moth: Int): String {
        return when (moth) {
            1 -> "Yanvar"
            2 -> "Fevral"
            3 -> "Mart"
            4 -> "Aprel"
            5 -> "May"
            6 -> "Iyun"
            7 -> "Iyul"
            8 -> "Avgust"
            9 -> "Sentyabr"
            10 -> "Oktyabr"
            11 -> "Noyabr"
            12 -> "Dekabr"
            else -> ""
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