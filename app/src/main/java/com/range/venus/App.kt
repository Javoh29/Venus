package com.range.venus

import android.app.Application
import com.range.venus.data.db.VenusDatabase
import com.range.venus.data.network.ApiService
import com.range.venus.data.pravider.UnitProvider
import com.range.venus.data.pravider.UnitProviderImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@App))

            bind() from singleton { VenusDatabase(instance())}
            bind() from singleton { instance<VenusDatabase>().venusDao() }
            bind() from singleton { ApiService() }
            bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }

        }

}