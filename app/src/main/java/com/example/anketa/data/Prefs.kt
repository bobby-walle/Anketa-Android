package com.example.anketa.data

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val FILE_PREFS = "prefsAnketa"
    val PREFS_ROLE = "prefsRole"

    val RESPONSE_COUNTER = "responseCounter"
    val RESPONSE = "response"
    val PREFS_PROFILE_DATA = "isProfileDataSet"

    private val appContext = context.applicationContext
    private val preferences: SharedPreferences =
        appContext.getSharedPreferences(FILE_PREFS, Context.MODE_PRIVATE)

    fun onChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    var role: Role
        get() = Role.valueOf(preferences.getString(PREFS_ROLE, Role.Employer.toString())!!)
        set(value) = preferences.edit().putString(PREFS_ROLE, value.toString()).apply()

    var isProfileDataSet: Boolean
        get() = preferences.getBoolean(PREFS_PROFILE_DATA, false)
        set(value) = preferences.edit().putBoolean(PREFS_PROFILE_DATA, value).apply()

    var responseCounter: Int
        get() = preferences.getInt(RESPONSE_COUNTER, 1)
        set(value) = preferences.edit().putInt(RESPONSE_COUNTER, value).apply()


    fun addResponse(id: String, cardsCount: Int) {
        if (responseCounter <= cardsCount) {
            val key = RESPONSE + id
            if (!preferences.contains(key)) {
                preferences.edit().putString(key, id).apply()
                if (responseCounter < cardsCount) {
                    responseCounter += 1
                }
            }
        }
    }

    fun getListOfResponsesID(): ArrayList<String?> =
        arrayListOf<String?>().apply {
            for (el in 1..responseCounter) {
                add(preferences.getString(RESPONSE + el, null))
            }
        }


    fun clearData() {
        preferences.edit().clear().commit()
    }
}