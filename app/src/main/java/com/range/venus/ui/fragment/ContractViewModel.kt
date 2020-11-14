package com.range.venus.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.range.venus.R
import com.range.venus.data.db.VenusDao
import com.range.venus.data.model.DateModel
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
class ContractViewModel(
    private val context: Context,
    private val apiService: ApiService,
    private val venusDao: VenusDao,
    private val unitProvider: UnitProvider
) : ViewModel(), Observable {

    private val decimalFormat: DecimalFormat = DecimalFormat("###,###.##")
    private val simpleYear = SimpleDateFormat("yyyy")
    private val simpleMoth = SimpleDateFormat("MM")
    private val simpleDate = SimpleDateFormat("MM.yyyy")
    private var userID: String = unitProvider.getUserID()
    val message = MutableLiveData<Int>()
    val listDate = ArrayList<DateModel>()
    val blockUser = MutableLiveData<Boolean>()
    val changeLang = MutableLiveData<Boolean>()
    val update = MutableLiveData<Boolean>()


    @Bindable
    val tvStudentName = MutableLiveData<String>()

    @Bindable
    val tvUniverName = MutableLiveData<String>()

    @Bindable
    val tvGroupName = MutableLiveData<String>()

    @Bindable
    val tvGroup = MutableLiveData<String>()

    @Bindable
    val tvDebit = MutableLiveData<String>()

    @Bindable
    val tvSom = MutableLiveData<String>()

    @Bindable
    val imgLang = MutableLiveData<Drawable>()

    @SuppressLint("UseCompatLoadingForDrawables")
    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            if (unitProvider.getLang()) {
                imgLang.value = context.getDrawable(R.drawable.ic_uzb)
            } else {
                imgLang.value = context.getDrawable(R.drawable.ic_rus)
            }
        }
        try {
            if (unitProvider.isOnline()) {
                val params: HashMap<String, String> = HashMap()
                params["user_id"] = userID
                val response = apiService.getPayments(params)

                if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                    response.body()!!.forEach {
                        venusDao.insertPayments(it)
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        message.value = R.string.text_err_loading_data
                    }
                }

                val response2 = apiService.getDebit(params)
                if (response2.isSuccessful && response2.body()!!.isNotEmpty()) {
                    venusDao.insertDebit(response2.body()!![0])
                    viewModelScope.launch(Dispatchers.Main) {
                        tvDebit.value = decimalFormat.format(response2.body()!![0].summa.toInt())
                        if (response2.body()!![0].summa.substring(0, 1) == "-") {
                            tvSom.value =
                                "${context.getString(R.string.text_som)} (${context.getString(R.string.text_debit)})"
                        } else {
                            tvSom.value =
                                "${context.getString(R.string.text_som)} (${context.getString(R.string.text_credit)})"
                        }
                    }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        message.value = R.string.text_err_loading_data
                    }
                }
                params["pass"] = unitProvider.getPassword()
                val response3 = apiService.checkLogin(params)
                if (response3.isSuccessful && response3.body()!!.isNotEmpty()) {
                    venusDao.insertUser(response3.body()!![0])
                    viewModelScope.launch(Dispatchers.Main) {
                        tvStudentName.value = response3.body()!![0].fio
                        tvUniverName.value = response3.body()!![0].universiteti
                        tvGroupName.value = response3.body()!![0].yonalishi
                        tvGroup.value = response3.body()!![0].guruhName
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        bindSortAll()
                    }
                } else if (response3.body()!!.isEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        message.value = R.string.text_wrong_password
                    }
                    unitProvider.saveUserID("")
                    unitProvider.savePassword("")
                    viewModelScope.launch(Dispatchers.Main) {
                        blockUser.value = true
                    }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        message.value = R.string.text_err_loading_data
                    }
                }
            } else {
                loadDB()
            }
        } catch (e: Exception) {
            loadDB()
        }
    }

    private fun loadDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = lazyDeferred { venusDao.getUser() }.value.await()
            if (model != null) {
                viewModelScope.launch(Dispatchers.Main) {
                    tvStudentName.value = model.fio
                    tvUniverName.value = model.universiteti
                    tvGroupName.value = model.yonalishi
                    tvGroup.value = model.guruhName
                }
                viewModelScope.launch(Dispatchers.IO) {
                    bindSortAll()
                }
            } else return@launch
        }
        viewModelScope.launch(Dispatchers.IO) {
            val model = lazyDeferred { venusDao.getDebit() }.value.await()
            if (model != null) {
                viewModelScope.launch(Dispatchers.Main) {
                    tvDebit.value = decimalFormat.format(model.summa.toInt())
                    if (model.summa.substring(0, 1) == "-") {
                        tvSom.value =
                            "${context.getString(R.string.text_som)} (${context.getString(R.string.text_debit)})"
                    } else {
                        tvSom.value =
                            "${context.getString(R.string.text_som)} (${context.getString(R.string.text_credit)})"
                    }
                }
            } else return@launch
        }
    }

    private fun bindSortAll() {
        var y = false
        val list: ArrayList<DateModel> = ArrayList()
        val currentYear = simpleYear.format(Date()).toInt()
        val currentMoth = simpleMoth.format(Date())
        var nextYear = currentYear + 1
        var end = true
        var moth = 9
        var m: String
        if (currentMoth.toInt() < 9) {
            y = true
            nextYear = currentYear - 1
        }
        while (end) {
            m = if (moth < 10) {
                "0$moth"
            } else moth.toString()
            if (moth in 1..8) {
                if (y) {
                    list.add(DateModel(textDate = getMoth(moth), formatDate = "${m}.$currentYear"))
                } else {
                    list.add(
                        DateModel(
                            textDate = getMoth(moth) + " $nextYear",
                            formatDate = "${m}.$nextYear"
                        )
                    )
                }
            } else {
                if (y) {
                    list.add(
                        DateModel(
                            textDate = getMoth(moth) + " $nextYear",
                            formatDate = "${m}.$nextYear"
                        )
                    )
                } else {
                    list.add(
                        DateModel(
                            textDate = getMoth(moth),
                            formatDate = "${m}.$nextYear"
                        )
                    )
                }
            }
            if (currentMoth.toInt() == moth) {
                end = false
            }
            if (moth > 11) {
                moth = 0
            }
            moth++
        }
        viewModelScope.launch(Dispatchers.Main) {
            listDate.addAll(list)
            update.value = true
        }
    }

    private fun getMoth(moth: Int): String {
        return when (moth) {
            1 -> context.getString(R.string.text_yan)
            2 -> context.getString(R.string.text_fev)
            3 -> context.getString(R.string.text_mar)
            4 -> context.getString(R.string.text_apr)
            5 -> context.getString(R.string.text_may)
            6 -> context.getString(R.string.text_jun)
            7 -> context.getString(R.string.text_jul)
            8 -> context.getString(R.string.text_avg)
            9 -> context.getString(R.string.text_snt)
            10 -> context.getString(R.string.text_okt)
            11 -> context.getString(R.string.text_nyb)
            12 -> context.getString(R.string.text_dek)
            else -> ""
        }
    }

    fun changeLanguage() {
        unitProvider.saveLang(!unitProvider.getLang())
        changeLang.value = true
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}