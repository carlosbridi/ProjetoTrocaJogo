package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Password.CryptPass;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.StringUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;
import com.iangclifton.android.floatlabel.FloatLabel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ActivityCadastro extends FragmentActivity implements AsyncTaskCompleteListener {


    private Button _buttonCadastro;
    private FloatLabel _nomeText;
    private FloatLabel _nomeUsuarioText;
    private FloatLabel _emailText;
    private FloatLabel _senhaText;
    private FloatLabel _telefoneText;
    private TextView _linkLogin;

    private Boolean FAtualizarDados = false;
    private int FCodUsuarioAtualizar = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitycadastro);

        _nomeText = (FloatLabel) findViewById(R.id.input_name);
        _nomeUsuarioText = (FloatLabel) findViewById(R.id.input_nameusuario);
        _emailText = (FloatLabel) findViewById(R.id.input_email);
        _senhaText = (FloatLabel) findViewById(R.id.input_password);
        _telefoneText = (FloatLabel) findViewById(R.id.input_phone);
        _linkLogin = (TextView) findViewById(R.id.link_login);

        _nomeText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!FAtualizarDados)
                    _nomeUsuarioText.getEditText().setText(StringUtil.retiraCaracteresEspeciais(_nomeText.getEditText().getText().toString().trim()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _buttonCadastro = (Button) findViewById(R.id. btn_signup);
        _buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastro();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.getBoolean("atualizarDados")){
                FAtualizarDados = true;
                _nomeUsuarioText.getEditText().setEnabled(false);
                _linkLogin.setVisibility(View.INVISIBLE);

                Usuario usuario = UsuarioUtil.obterUsuario(this);

                _nomeText.setText(usuario.getNome());
                _nomeUsuarioText.setText(usuario.getNomeUsuario());
                _emailText.setText(usuario.getEmail());
                _telefoneText.setText(usuario.getTelefone());
                FCodUsuarioAtualizar = usuario.getId();
                _buttonCadastro.setText("Atualizar dados");

            }
        }

        if (!FAtualizarDados) {

            Cursor c = this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            int count = c.getCount();

            c.moveToFirst();
            int position = c.getPosition();
            if (count == 1 && position == 0) {
                _nomeText.getEditText().setText(c.getString(c.getColumnIndex("display_name")));
            }
            c.close();

            AccountManager am = AccountManager.get(this);
            Account[] accounts = am.getAccounts();
            for (Account account : accounts) {
                if (account.type.compareTo("com.google") == 0) {
                    _emailText.getEditText().setText(account.name);
                }
            }
        }



    }

    public void cadastro() {
        if (!valid()) {
            onCadastroFailed();
            return;
        }

        String msgTask;
        if (FAtualizarDados){
            msgTask = "Atualizando dados...";
        }else{
            msgTask = "Efetuando cadastro...";
        }

        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, msgTask, this);;
        webServiceTask.addParameter("nome", _nomeText.getEditText().getText().toString());
        webServiceTask.addParameter("nomeusuario", _nomeUsuarioText.getEditText().getText().toString());
        try {
            webServiceTask.addParameter("senha", CryptPass.Crype(_senhaText.getEditText().getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        webServiceTask.addParameter("email", _emailText.getEditText().getText().toString());
        webServiceTask.addParameter("telefone", _telefoneText.getEditText().getText().toString());

        if (FAtualizarDados){
            webServiceTask.addParameter("id", String.valueOf(FCodUsuarioAtualizar));

        }else {
            webServiceTask.addParameter("id", "0");
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "UsuarioWS"});
        }

    }
    
    public void onCadastroFailed(){
        new SnackBar.Builder(this)
                .withMessage("Falha no cadastro!")
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }

    public boolean valid(){
        boolean valid = true;

        String nome = _nomeText.getEditText().getText().toString();
        String nomeusuario = _nomeUsuarioText.getEditText().getText().toString();
        String email = _emailText.getEditText().getText().toString();
        String senha = _senhaText.getEditText().getText().toString();
        String telefone = _telefoneText.getEditText().getText().toString();

        if (nome.isEmpty()){
            _nomeText.getEditText().setError("informe um nome válido!");
            valid = false;
        }else{
            _nomeText.getEditText().setError(null);
        }

        if (nomeusuario.isEmpty()){
            _nomeUsuarioText.getEditText().setError("informe um nome de usuário válido!");
            valid = false;
        }else{
            _nomeUsuarioText.getEditText().setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.getEditText().setError("informe um email valido!");
            valid = false;
        } else {
            _emailText.getEditText().setError(null);
        }

        if (senha.isEmpty() || senha.length() < 4 || senha.length() > 10) {
            _senhaText.getEditText().setError("a senha deve ter de 4 a 10 caracteres");
            valid = false;
        } else {
            _senhaText.getEditText().setError(null);
        }

        if (telefone.isEmpty() || !Patterns.PHONE.matcher(telefone).matches()) {
            _telefoneText.getEditText().setError("informe um telefone valido!");
            valid = false;
        } else {
            _telefoneText.getEditText().setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void doLogin(View v){
        Intent itLogin = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(itLogin);
        finish();
    }

    public void doLogoff(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("usuariologado", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("usuariologado", false);
        editor.commit();
    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if ((result.getInt("codResultado") == 1) && (!FAtualizarDados)) {
            finish();
        }

        if(FAtualizarDados){
            doLogoff();

            Intent it = new Intent();
            it.putExtra("codResultado", FCodUsuarioAtualizar);
            setResult(Activity.RESULT_OK, it);
            finish();
        }
        Toast.makeText(getApplicationContext(), result.getString("msgResultado"), Toast.LENGTH_SHORT).show();

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
