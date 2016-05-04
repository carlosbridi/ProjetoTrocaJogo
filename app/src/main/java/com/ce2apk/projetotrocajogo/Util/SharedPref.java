package com.ce2apk.projetotrocajogo.Util;

import android.content.Context;

import com.ce2apk.projetotrocajogo.Usuario.Usuario;

import java.util.Map;

/**
 * Created by carlosbridi on 03/11/15.
 */
public class SharedPref {


    private String sharedPreffName;
    private Context context;
    private Map<String, String> mapValores;
    private int modeContext;

    public SharedPref(String sharedPreffName, Context context, Map<String, String> mapValores, int modeContext) {
        this.sharedPreffName = sharedPreffName;
        this.context = context;
        this.mapValores = mapValores;
        this.modeContext = modeContext;
    }

    public String getSharedPreffName() {
        return sharedPreffName;
    }

    public void setSharedPreffName(String sharedPreffName) {
        this.sharedPreffName = sharedPreffName;
    }

    public int getModeContext() {
        return modeContext;
    }

    public void setModeContext(int modeContext) {
        this.modeContext = modeContext;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Map<String, String> getMapValores() {
        return mapValores;
    }

    public void setMapValores(Map<String, String> mapValores) {
        this.mapValores = mapValores;
    }

}
