package com.ce2apk.projetotrocajogo.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WebServiceTask extends AsyncTask<String, Integer, String>{

    public static final int POST_TASK = 1;
    public static final int GET_TASK = 2;
    public static final int DELETE_TASK = 3;
    public static final int PUT_TASK = 4;

    public JSONObject returnWS;

    private static final String TAG = "WebServiceTask";

    //Time out para conexão em milisegundos
    private static final int CONN_TIMEOUT = 7000;

    //Tempo de timeout em milisegundos para espera dos dados... (5 segundos é pouco e 10 é muito, escolhido 7 por ser a média)
    private static final int SOCKET_TIMEOUT = 7000;

    private int taskType = GET_TASK;
    private Context mContext = null;
    private String processMessage = "Processando...";
    private AsyncTaskCompleteListener<String> callback;
    private boolean hideProcessMessage = false;
    private String msgErro;

    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

    private ProgressDialog pDlg = null;

    public WebServiceTask(int taskType, Context mContext, String processMessage, AsyncTaskCompleteListener<String> cba) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.callback = cba;
    }

    public WebServiceTask(int taskType, Context mContext, String processMessage, AsyncTaskCompleteListener<String> cba, String msgErro) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.callback = cba;
        this.msgErro = msgErro;
    }

    public WebServiceTask(int taskType, Context mContext, String processMessage, AsyncTaskCompleteListener<String> cba, boolean hideProcessMessage) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.callback = cba;
        this.hideProcessMessage = true;
    }


    private void showProgressDialog() {
        pDlg = new ProgressDialog(mContext);
        pDlg.setMessage(processMessage);
        pDlg.setIndeterminate(true);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDlg.setCancelable(false);
        pDlg.show();
    }

    public void addParameter(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    @Override
    protected void onPreExecute() {
        if(!this.hideProcessMessage)
            showProgressDialog();
    }

    protected String doInBackground(String... urls) {

        String url = urls[0];
        String result = "";

        HttpResponse response = doResponse(url);

        if (response == null) {
            onPostExecute(result);
            return result;
        } else {
            try {
                result = inputStreamToString(response.getEntity().getContent());
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        try{
            if(!response.equals("") && (!response.equals("null") && !response.equals("{}"))){
              callback.onTaskComplete(new JSONObject(response));
            }else{
                if(msgErro != null)
                    callback.onTaskReturnNull(msgErro.equals("") ? "Erro ao retornar dados" : msgErro);
            }
        }catch (JSONException e){
            Log.v(TAG, "Problemas para obter resposta do servidor.");
        }

        if(!hideProcessMessage)
            pDlg.dismiss();

    }

    public void handleResponse(String response, Context context) {
        try {
            JSONObject jso = new JSONObject(response);
            returnWS = jso;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Estabelece conexão e define timeout do socket
    private HttpParams getHttpParams() {
        HttpParams htpp = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

        return htpp;
    }

    private HttpResponse doResponse(String url) {
        // Use our connection and data timeouts as parameters for our
        // DefaultHttpClient
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        HttpResponse response = null;

        try {
            switch (taskType) {
                case PUT_TASK:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    response = httpclient.execute(httpPut);
                    break;

                case POST_TASK:
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    response = httpclient.execute(httpPost);
                    break;

                case GET_TASK:
                    HttpGet httpget = new HttpGet(url);
                    response = httpclient.execute(httpget);
                    break;

                case DELETE_TASK:
                    HttpDelete httpDelete = new HttpDelete(url);
                    response = httpclient.execute(httpDelete);
                    break;


            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            if (!hideProcessMessage)
                pDlg.dismiss();
        }

        return response;
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }

}
