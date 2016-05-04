package com.ce2apk.projetotrocajogo.Usuario;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ce2apk.projetotrocajogo.Imagens.AsyncTaskCompleteImageCache;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosbridi on 20/02/16.
 */
public class DadosUsuario extends AsyncTask<Integer, String, Integer> implements AsyncTaskCompleteListener {

    private int codUsuario;
    private String msgBusca;
    private int tipBusca;
    private Context mContext;

    private ProgressDialog pDlg;

    public DadosUsuario(Context context, int codUsuario, String msgBusca, int tipBusca){
        this.codUsuario = codUsuario;
        this.msgBusca = msgBusca;
        this.tipBusca = tipBusca;
        this.mContext = context;
    }

    private void showProgressDialog() {
        pDlg = new ProgressDialog(mContext);
        pDlg.setMessage(msgBusca);
        pDlg.setIndeterminate(true);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDlg.setCancelable(false);
        pDlg.show();
    }

    @Override
    protected void onPreExecute() {
        showProgressDialog();
    }


    @Override
    protected Integer doInBackground(Integer... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {

    }

    @Override
    public void onTaskReturnNull(String msg) {
        //doAnything
    }
}
