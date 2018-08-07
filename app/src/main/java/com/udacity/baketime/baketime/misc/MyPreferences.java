package com.udacity.baketime.baketime.misc;

import android.content.Context;
import android.content.SharedPreferences;

import com.udacity.baketime.baketime.R;

public class MyPreferences {
    private static MyPreferences yourPreference;
    private final SharedPreferences sharedPreferences;

    private MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPrefString), Context.MODE_PRIVATE);
    }

    public static MyPreferences getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new MyPreferences(context);
        }
        return yourPreference;
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void saveDataInt(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }


    public int getDataInt(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }
}

