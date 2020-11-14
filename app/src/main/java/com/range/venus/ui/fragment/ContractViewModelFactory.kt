package com.range.venus.ui.fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.range.venus.data.db.VenusDao
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider

class ContractViewModelFactory(
    private val context: Context,
    private val apiService: ApiService,
    private val venusDao: VenusDao,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContractViewModel(context, apiService, venusDao, unitProvider) as T
    }
}