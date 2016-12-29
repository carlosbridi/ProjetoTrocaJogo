package com.ce2apk.projetotrocajogo.Util;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlosbridi on 03/11/15.
 */
public class SharedPrefDAO {

    private SharedPreferences.Editor gravarValoresSharedPref(SharedPref sharedPref) {
        SharedPreferences sharedPreferences = sharedPref.getContext().getSharedPreferences(sharedPref.getSharedPreffName(), sharedPref.getModeContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> entry : sharedPref.getMapValores().entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        return editor;
    }

    public boolean gravarSharedPref(SharedPref sharedPref) {
        return gravarValoresSharedPref(sharedPref).commit();
    }
}
