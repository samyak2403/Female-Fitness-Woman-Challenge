package com.arrowwould.womenhomeworkout.common

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.*

object LocaleManager {
    const val LANGUAGE_ENGLISH = "en"
    private const val LANGUAGE_KEY = "language_key"
    fun setLocale(c: Context): Context {
        return updateResources(c, getLanguage(c))
    }



    fun getLanguage(c: Context?): String {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(c)

        val lan = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Locale.getDefault().language
        } else {
             Resources.getSystem().configuration.locale.language
        };

        return prefs.getString(Constant.PREF_LANGUAGE, LANGUAGE_ENGLISH)?:LANGUAGE_ENGLISH
    }



    private fun updateResources(context: Context, language: String): Context {
        var context: Context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = context.getResources()
        val config = Configuration(res.getConfiguration())
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.getDisplayMetrics())
        }
        return context
    }

    fun getLocale(res: Resources): Locale {
        val config: Configuration = res.configuration
        return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
    }
}