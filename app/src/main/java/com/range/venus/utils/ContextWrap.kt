package com.range.venus.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

@Suppress("DEPRECATION")
open class ContextWrap(base: Context?) : ContextWrapper(base) {

    companion object {
        fun wrap(ctx: Context, newLocale: Locale): ContextWrap {

            var context: Context = ctx
            val res: Resources = ctx.resources
            val configuration: Configuration = res.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)

                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context = ctx.createConfigurationContext(configuration)
            } else {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            return ContextWrap(context)
        }
    }
}