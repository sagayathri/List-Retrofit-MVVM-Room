package com.gayathriarumugam.spectrumtask.Utils

import android.content.Context
import android.content.SharedPreferences
import java.io.File

object PreferencesManager {
    //Shared Preference field used to save and retrieve JSON string
    lateinit var preferences: SharedPreferences

    //Name of Shared Preference file
    internal const val PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME"
    val constant = Constant()

    fun with(application: Context) {
        preferences = application.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun preferenceFileExist(context: Context): Boolean {
        val file = File("/data/data/" + context.getPackageName() + "/shared_prefs/" + PREFERENCES_FILE_NAME + ".xml");
        return file.exists()
    }

    fun saveURL() {
        preferences.edit().putString(PREFERENCES_FILE_NAME, constant.base_URL).apply()
    }
}