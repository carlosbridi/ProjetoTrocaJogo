package com.ce2apk.projetotrocajogo.WebService;

import org.json.JSONException;

/**
 * Interface padrão que retornar os dados do WebService
 */
public interface AsyncTaskCompleteListener<JSONObject> {
    public void onTaskComplete(org.json.JSONObject result) throws JSONException;
    public void onTaskReturnNull(String msg);
}
