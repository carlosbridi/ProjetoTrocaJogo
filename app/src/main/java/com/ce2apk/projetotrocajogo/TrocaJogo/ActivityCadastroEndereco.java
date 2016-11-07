package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.SharedPref;
import com.ce2apk.projetotrocajogo.Util.SharedPrefDAO;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;
import com.iangclifton.android.floatlabel.FloatLabel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityCadastroEndereco extends FragmentActivity implements AsyncTaskCompleteListener {

    private FloatLabel _cep;
    private FloatLabel _logradouro;
    private FloatLabel _numero;
    private FloatLabel _complemento;
    private FloatLabel _bairro;
    private Spinner _estado;
    private FloatLabel _cidade;
    private Button btn_atualizarDdaos;

    private int FCodUsuarioAtualizar = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityusuarioendereco);

        SharedPreferences sharedPreferences = getSharedPreferences("dadosUsuario", MODE_PRIVATE);
        FCodUsuarioAtualizar = Integer.valueOf(sharedPreferences.getString("id", "0"));


        _cep = (FloatLabel) findViewById(R.id.input_cep);
        _logradouro = (FloatLabel) findViewById(R.id.input_logradouro);
        _numero = (FloatLabel) findViewById(R.id.input_numero);
        _complemento = (FloatLabel) findViewById(R.id.input_complemento);
        _bairro = (FloatLabel) findViewById(R.id.input_bairro);
        _estado = (Spinner) findViewById(R.id.input_estado);
        _cidade = (FloatLabel) findViewById(R.id.input_cidade);

        btn_atualizarDdaos = (Button) findViewById(R.id.btn_atualizarDdaos);
        btn_atualizarDdaos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastro();
            }
        });

        loadDataShared();

        _cep.getEditText().addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int after) {


                // Quando o texto é alterado o onTextChange é chamado
                // Essa flag evita a chamada infinita desse método
                if (isUpdating){
                    isUpdating = false;
                    return;
                }

                // Ao apagar o texto, a máscara é removida,
                // então o posicionamento do cursor precisa
                // saber se o texto atual tinha ou não, máscara
                boolean hasMask =
                        s.toString().indexOf('.') > -1 ||
                                s.toString().indexOf('-') > -1;

                // Remove o '.' e '-' da String
                String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "");

                // as variáveis before e after dizem o tamanho
                // anterior e atual da String, se after > before
                // é pq está apagando
                if (after > before) {
                    // Se tem mais de 5 caracteres (sem máscara)
                    // coloca o '.' e o '-'
                    if (str.length() > 5) {
                        str = str.substring(0, 2) + '.' +
                              str.substring(2, 5) + '-' +
                              str.substring(5);

                        // Se tem mais de 2, coloca só o ponto
                    } else if (str.length() > 2) {
                        str = str.substring(0, 2) + '.' +
                              str.substring(2);
                    }
                    // Seta a flag pra evitar chamada infinita
                    isUpdating = true;
                    // seta o novo texto
                    _cep.getEditText().setText(str);
                    // seta a posição do cursor
                    _cep.getEditText().setSelection(_cep.getEditText().getText().length());

                } else {
                    isUpdating = true;
                    _cep.getEditText().setText(str);
                    // Se estiver apagando posiciona o cursor
                    // no local correto. Isso trata a deleção dos
                    // caracteres da máscara.
                    _cep.getEditText().setSelection(
                            Math.max(0, Math.min(
                                    hasMask ? start - before : start,
                                    str.length())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public Boolean valid(){
        boolean valid = true;

        String cep = _cep.getEditText().getText().toString();
        String cidade = _cidade.getEditText().getText().toString();

        if (cep.isEmpty()){
            _cep.getEditText().setError("nenhum cep informado");
            valid = false;
        }else{
            _cep.getEditText().setError(null);
        }

        if (cidade.isEmpty()){
            _cidade.getEditText().setError("nenhuma cidade informada");
            valid = false;
        }else{
            _cidade.getEditText().setError(null);
        }

        return valid;
    }


    public void cadastro() {
        if (!valid()) {
            onCadastroFailed();
            return;
        }

        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.PUT_TASK, this, "Atualizando dados", this);
        webServiceTask.addParameter("id", String.valueOf(FCodUsuarioAtualizar));
        webServiceTask.addParameter("cep", _cep.getEditText().getText().toString());
        webServiceTask.addParameter("logradouro", _logradouro.getEditText().getText().toString());
        webServiceTask.addParameter("numero", _numero.getEditText().getText().toString());
        webServiceTask.addParameter("complemento", _complemento.getEditText().getText().toString());
        webServiceTask.addParameter("bairro", _bairro.getEditText().getText().toString());
        webServiceTask.addParameter("estado", String.valueOf(_estado.getSelectedItemPosition()));
        webServiceTask.addParameter("cidade", _cidade.getEditText().getText().toString());

        webServiceTask.execute(new String[]{consts.SERVICE_URL + "UsuarioWS"});
    }

    public void onCadastroFailed(){
        new SnackBar.Builder(this)
                .withMessage("Falha na atualização dos dados!")
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (result.getInt("codResultado") == 1){
            result.getString("msgResultado");
            gravarSharedPreff();

            new SnackBar.Builder(this)
                    .withMessage("Dados atualizados com sucesso!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

        }else{
            onCadastroFailed();
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

    public void gravarSharedPreff(){
        Map<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("cep", _cep.getEditText().getText().toString());
        hashMap.put("logradouro", _logradouro.getEditText().getText().toString());
        hashMap.put("numero", _numero.getEditText().getText().toString());
        hashMap.put("complemento", _complemento.getEditText().getText().toString());
        hashMap.put("bairro", _bairro.getEditText().getText().toString());
        hashMap.put("estado", String.valueOf(_estado.getSelectedItemPosition()));
        hashMap.put("cidade", _cidade.getEditText().getText().toString());

        SharedPrefDAO dao = new SharedPrefDAO();
        dao.gravarSharedPref(new SharedPref("dadosUsuario", this, hashMap, MODE_PRIVATE));
    }

    public void loadDataShared() {
        Usuario usuario = UsuarioUtil.obterUsuario(this, "dadosUsuario");

        _cep.getEditText().setText(usuario.getCep());
        _logradouro.getEditText().setText(usuario.getLogradouro());
        _numero.getEditText().setText(usuario.getNumero());
        if (usuario.getEstado().equals("")){
         _estado.setSelection(0);
        }else {
            _estado.setSelection(Integer.valueOf(usuario.getEstado()));
        }

        _cidade.getEditText().setText(usuario.getCidade());
        _bairro.getEditText().setText(usuario.getBairro());
        _complemento.getEditText().setText(usuario.getComplemento());
    }



}
