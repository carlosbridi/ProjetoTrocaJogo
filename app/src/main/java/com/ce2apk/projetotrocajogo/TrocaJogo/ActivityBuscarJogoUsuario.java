package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;

import com.ce2apk.projetotrocajogo.Jogo.JogoUtil;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoBuscaCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;
import com.iangclifton.android.floatlabel.FloatLabel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosbridi on 19/11/15.
 */
public class ActivityBuscarJogoUsuario extends AppCompatActivity implements AsyncTaskCompleteListener {

    private FloatLabel _nomeJogo;
    private Spinner _categoriaJogo;
    private Spinner _plataformaJogo;
    private Button btn_buscarJogo;
    private WebServiceTask webServiceTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfiltrobusca);

        _nomeJogo = (FloatLabel) findViewById(R.id.input_nomeJogo);
        _categoriaJogo = (Spinner) findViewById(R.id.spinnerCategoria);
        _plataformaJogo = (Spinner) findViewById(R.id.spinnerPlataforma);
        btn_buscarJogo = (Button) findViewById(R.id.btn_buscarJogo);
        btn_buscarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarJogos();
            }
        });

    }

    public boolean valid(){

        boolean valido = true;

        String nomeJogo = _nomeJogo.getEditText().getText().toString();

        if (nomeJogo.isEmpty()){
            _nomeJogo.getEditText().setError("é necessário um nome de jogo");
            valido = false;
        }else{
            _nomeJogo.getEditText().setError(null);
        }
        return valido;
    }


    public void onFindFailed(){
        new SnackBar.Builder(this)
                .withMessage("Falha na busca dos jogos!")
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }


    public void buscarJogos(){
        View v = this.getCurrentFocus();
        if(v != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        if(!valid()) {
            onFindFailed();
        }

        webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Buscando jogos", this);
        webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(this, "dadosUsuario").getId()));
        webServiceTask.addParameter("nome", _nomeJogo.getEditText().getText().toString());
        webServiceTask.addParameter("categoria", String.valueOf(_categoriaJogo.getSelectedItemPosition()));
        webServiceTask.addParameter("plataforma", String.valueOf(_plataformaJogo.getSelectedItemPosition()));
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "BuscarJogos/JogosUsuarios"});

    }


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if(!result.toString().equals("{}")){

            Temp_JogoBuscaCRUD temp_jogoCRUD = new Temp_JogoBuscaCRUD(getApplicationContext());
            temp_jogoCRUD.removerTodaColecao();

            try {
                try {
                    JSONArray jsonArray = result.getJSONArray("JogoUsuarioDTO");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        temp_jogoCRUD.inserirJogoColecao(JogoUtil.parserTempBusca(jsonArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    temp_jogoCRUD.inserirJogoColecao(JogoUtil.parserTempBusca(result.getJSONObject("JogoUsuarioDTO")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent it = new Intent(this, ActivityListarJogosBusca.class);
            startActivityForResult(it, 190);


        }else{
            new SnackBar.Builder(this)
                    .withMessage("Nenhum jogo encontrado com esse filtro!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 190){
            if (resultCode == RESULT_OK){
                Intent it = new Intent();
                it.putExtra("idCodJogo", data.getExtras().getInt("idCodJogo"));
                setResult(RESULT_OK, it);
                finish();
            }
        }
    }
}
