package com.dapp.talk2us.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.dapp.talk2us.MainApplication


object PrefManager {
    private val preference by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val context: Context
        get() = MainApplication.instance.applicationContext

    fun putBoolean(preferenceKey: Int, preferenceValue: Boolean) {
        preference.edit().putBoolean(context.getString(preferenceKey), preferenceValue).apply()
    }

    fun getBoolean(preferenceKey: Int, defaultValue: Boolean): Boolean {
        return preference.getBoolean(context.getString(preferenceKey), defaultValue)
    }
}