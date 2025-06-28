package com.mohammed.babelrestaurant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

/**
 * This Class save the state of the language or darkMode preferences.
 */

public class SharedPreferencesHelper {
    private static final String DEFAULT_LANGUAGE = "ar";
    private final static String DEFAULT_DARK_MODE = "follow_system_mode";
    private static SharedPreferences preference;

    // Save language or darkMode preference.

    public static void setLanguagePreferences(String key, String value, Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().putString(key, value).apply();
    }

    public static void setDarkModePreferences(String key, String value, Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().putString(key, value).apply();
    }

    // Get language or darkMode preference.


    public static String getLanguagePreferences(String key, Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString(key, DEFAULT_LANGUAGE);
    }

    public static String getDarkModePreferences(String key, Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString(key, DEFAULT_DARK_MODE);
    }


}
