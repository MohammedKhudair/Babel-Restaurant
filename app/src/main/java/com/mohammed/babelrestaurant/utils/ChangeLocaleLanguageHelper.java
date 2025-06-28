package com.mohammed.babelrestaurant.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class ChangeLocaleLanguageHelper {

    public static Context setLocaleLanguage(Context context, String language) {
        persistLanguage(context, language);

       // return updateResources(context, language);
        return updateResourcesLegacy(context,language);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    private static void persistLanguage(Context context, String language) {
        SharedPreferencesHelper.
                setLanguagePreferences(Constants.LANGUAGE_KEY, language, context);
    }

}
