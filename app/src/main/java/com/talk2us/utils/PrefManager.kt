package com.talk2us.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.talk2us.MainApplication
import com.talk2us.utils.Constants.CLIENT_ID
import com.talk2us.utils.Constants.COUNSELLOR_ID


object PrefManager {
    private val preference by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val context: Context
        get() = MainApplication.instance.applicationContext

    fun putBoolean(preferenceKey: String, preferenceValue: Boolean) {
        preference.edit().putBoolean(preferenceKey, preferenceValue).apply()
    }

    fun getBoolean(preferenceKey: String, defaultValue: Boolean): Boolean {
        return preference.getBoolean(preferenceKey, defaultValue)
    }

    fun putString(preferenceKey: String, preferenceValue: String) {
        preference.edit().putString(preferenceKey, preferenceValue).apply()
    }

    fun getString(preferenceKey: String, defaultValue: String): String? {
        return preference.getString(preferenceKey, defaultValue)
    }

    fun getChatId():String{
       return getString(
            COUNSELLOR_ID,
            Constants.NOT_DEFINED
        ) + getString(CLIENT_ID, Constants.NOT_DEFINED)

    }

    fun getClientId():String{
        return getString(CLIENT_ID,Constants.NOT_DEFINED) as String
    }
    fun getCounsellorId():String{
        return getString(COUNSELLOR_ID,Constants.NOT_DEFINED) as String
    }

    fun sessionEnded(){
        putString(COUNSELLOR_ID,Constants.NOT_DEFINED)
    }
}