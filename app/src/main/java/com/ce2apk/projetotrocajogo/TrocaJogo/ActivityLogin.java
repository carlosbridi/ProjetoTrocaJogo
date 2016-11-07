package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ce2apk.projetotrocajogo.Password.CryptPass;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.github.mrengineer13.snackbar.SnackBar;
import com.iangclifton.android.floatlabel.FloatLabel;

public class ActivityLogin extends FragmentActivity implements AsyncTaskCompleteListener {

    private FloatLabel _emailText;
    private FloatLabel _senhaText;
    private Button _loginButton;
    private TextView _criarConta;
    private static final int REQUEST_SIGNUP = 0;
    private SnackBar mSnackBar;
    private WebServiceTask wst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin);

        if (!UsuarioUtil.usuarioEmCache(getBaseContext())) {
            _emailText = (FloatLabel) findViewById(R.id.input_email);
            _senhaText = (FloatLabel) findViewById(R.id.input_password);
            _loginButton = (Button) findViewById(R.id.btn_login);
            _criarConta = (TextView) findViewById(R.id.link_signup);

            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });

            _criarConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ActivityCadastro.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }else{
            Intent it = new Intent(this, ActivityPrincipal.class);
            startActivity(it);
            finish();
        }
    }


    public void login(){
        if (!isOnline()){
            return;
        }

        if(!validate()){
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        try {
            wst = new WebServiceTask(WebServiceTask.POST_TASK, this, "Efetuando login", this, "Erro ao efetuar o login, tente novamente mais tarde!");
            wst.addParameter("email", _emailText.getEditText().getText().toString());
            wst.addParameter("senha", CryptPass.Crype(_senhaText.getEditText().getText().toString()));
            wst.execute(new String[]{consts.SERVICE_URL + "login"});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((wst != null) && (wst.getStatus() == AsyncTask.Status.RUNNING)) {
            wst.cancel(true);
        }
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!(manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting())) {
            mSnackBar = new SnackBar.Builder(this)
                    .withMessage("É necessário conexão com a internet!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            return false;
        }
        return true;
    }

    public boolean validate(){
        boolean valid = true;

        String email = _emailText.getEditText().getText().toString();
        String password = _senhaText.getEditText().getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.getEditText().setError("informe um email valido!");
            valid = false;
        } else {
            _emailText.getEditText().setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _senhaText.getEditText().setError("A senha deve ter de 4 a 10 caracteres");
            valid = false;
        } else {
            _senhaText.getEditText().setError(null);
        }

        return valid;
    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        int codResult = result.getInt("codResultado");

        if ((codResult == 0) || (codResult == -1)) {
            Toast.makeText(getApplicationContext(), result.getString("msgResultado"), Toast.LENGTH_SHORT).show();
            onLoginFailed();
        } else {
            Intent it = new Intent(getApplicationContext(), ActivityPrincipal.class);
            it.putExtra("codResultado", codResult);
            startActivity(it);
            finish();
        }
    }

    @Override
    public void onTaskReturnNull(String msg) {
        new SnackBar.Builder(this)
                .withMessage(msg)
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }

}
