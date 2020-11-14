package com.range.venus

import androidx.multidex.MultiDexApplication
import com.range.venus.data.db.VenusDatabase
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.data.pravider.UnitProviderImpl
import com.range.venus.ui.fragment.ContractViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : MultiDexApplication(), KodeinAware {

    companion object {
        var isLoadData: Boolean = true
    }

    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@App))

            bind() from singleton { VenusDatabase(instance())}
            bind() from singleton { instance<VenusDatabase>().venusDao() }
            bind() from singleton { ApiService() }
            bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
            bind() from provider { ContractViewModelFactory(instance(), instance(), instance(), instance()) }

        }

}