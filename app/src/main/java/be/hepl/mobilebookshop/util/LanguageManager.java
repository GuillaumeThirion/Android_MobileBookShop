package be.hepl.mobilebookshop.util;

import android.content.res.Configuration;

import java.util.Locale;

public class LanguageManager {

    private static String languageCode = Locale.getDefault().getLanguage(); // Langue par défaut du système


    /* STATIC METHODS */

    public static String getLanguageCode() {
        return languageCode;
    }

    public static Configuration getConfiguration() {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        return config;
    }

    public static void setLanguageCode(String code) {
        languageCode = code;
    }
}
