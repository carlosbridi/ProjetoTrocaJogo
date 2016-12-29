package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.Imagens.AsyncTaskCompleteImageCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;
import com.ce2apk.projetotrocajogo.Troca.StatusTroca;
import com.ce2apk.projetotrocajogo.Troca.Troca;
import com.ce2apk.projetotrocajogo.Troca.TrocaCRUD;
import com.ce2apk.projetotrocajogo.Troca.TrocaConcluida;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.ParserArray;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosbridi on 07/04/16.
 */
public class ActivityDetalheTroca extends Activity implements AsyncTaskCompleteListener, AsyncTaskCompleteImageCache {

    private int idTroca = 0;
    private ImageView imagemJogoTroca;
    private ImageView imagemJogoOferta;

    private TextView txtDetalheTituloTroca;
    private TextView txtDetalheAnoTroca;
    private TextView txtDetalheCategoriaTroca;
    private TextView txtDetalhePlataformaTroca;
    private TextView txtDetalheDescricaoTroca;
    private TextView txtDetalheNomeUsuarioTroca;

    private TextView txtDetalheTituloOferta;
    private TextView txtDetalheAnoOferta;
    private TextView txtDetalheCategoriaOferta;
    private TextView txtDetalhePlataformaOferta;
    private TextView txtDetalheDescricaoOferta;
    private TextView txtDetalheNomeUsuarioOferta;

    private LinearLayout llAceitarRejeitar;
    private LinearLayout llDadosConluir;
    private LinearLayout llFechar;
    private LinearLayout llCancelar;

    private ImagemCache imagemCacheTroca;
    private ImagemCache imagemCacheOferta;

    private ImageView imgJogo1;
    private ImageView imgJogo2;

    private enum IMG_BUSCA  {IMG_OFERTA, IMG_TROCA};
    private IMG_BUSCA imagemBusca;
    private Usuario mUsuario;

    private enum TIPO_BUSCA {tp_none, tp_busca_dados_usuario};
    private TIPO_BUSCA tipo_busca = TIPO_BUSCA.tp_none;

    private Troca mTroca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhetroca);

        imagemJogoTroca = (ImageView) findViewById(R.id.imageDetalheTrocaOferta);
        imagemJogoOferta = (ImageView) findViewById(R.id.imageDetalheTrocaColecao);

        txtDetalheTituloOferta = (TextView) findViewById(R.id.txtDetalheTituloOferta);
        txtDetalheTituloTroca = (TextView) findViewById(R.id.txtDetalheTituloTroca);

        txtDetalheCategoriaOferta = (TextView) findViewById(R.id.txtDetalheCategoriaOferta);
        txtDetalheCategoriaTroca = (TextView) findViewById(R.id.txtDetalheCategoriaTroca);

        txtDetalhePlataformaOferta = (TextView) findViewById(R.id.txtDetalhePlataformaOferta);
        txtDetalhePlataformaTroca = (TextView) findViewById(R.id.txtDetalhePlataformaTroca);

        txtDetalheAnoOferta = (TextView) findViewById(R.id.txtDetalheAnoOferta);
        txtDetalheAnoTroca = (TextView) findViewById(R.id.txtDetalheAnoTroca);

        txtDetalheDescricaoOferta = (TextView) findViewById(R.id.txtDetalheDescricaoOferta);
        txtDetalheDescricaoTroca = (TextView) findViewById(R.id.txtDetalheDescricaoTroca);

        txtDetalheNomeUsuarioTroca = (TextView) findViewById(R.id.txtDetalheNomeUsuarioTroca);
        txtDetalheNomeUsuarioOferta = (TextView) findViewById(R.id.txtDetalheNomeUsuarioOferta);


        llAceitarRejeitar = (LinearLayout) findViewById(R.id.llaceitarrejeitar);
        llDadosConluir = (LinearLayout) findViewById(R.id.llConcluirDados);
        llFechar = (LinearLayout) findViewById(R.id.llfechar);
        llCancelar = (LinearLayout) findViewById(R.id.llcancelarTroca);

        mUsuario = UsuarioUtil.obterUsuario(this);

        llAceitarRejeitar.setVisibility(View.GONE);
        llDadosConluir.setVisibility(View.GONE);
        llFechar.setVisibility(View.GONE);
        llCancelar.setVisibility(View.GONE);

        imagemCacheTroca = new ImagemCache();
        imagemCacheTroca.setContext(this);
        imagemCacheTroca.setCallback(this);

        imagemCacheOferta = new ImagemCache();
        imagemCacheOferta.setContext(this);
        imagemCacheOferta.setCallback(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
          idTroca = extras.getInt("idTroca", 0);
          TrocaCRUD trocaCRUD = new TrocaCRUD(this);
          mTroca = trocaCRUD.buscarTroca(idTroca);

//            /**
//             *      STATUS TROCA     |   MINHA TROCA (OFERTA)   | OFERTA DOS OUTROS (TROCA)
//             *     TROCA_ANALISE     |      llCancelar          |    llAceitarRejeitar
//             *    TROCA_ANDAMENTO    |     llDadosUsuario       |    llDadosUsuario
//             */


        switch (mTroca.getStatusTroca().toString()) {
            case "A": {
                if (mTroca.getJogosTroca().getIdUsuarioOferta() == mUsuario.getId()) {
                    llCancelar.setVisibility(View.VISIBLE);
                    txtDetalheNomeUsuarioOferta.setVisibility(View.GONE);
                } else {
                    llAceitarRejeitar.setVisibility(View.VISIBLE);
                }

                break;
            }
            case "D": {
                llFechar.setVisibility(View.VISIBLE);
                break;
            }
            case "N": {
                llDadosConluir.setVisibility(View.VISIBLE);
                break;
            }
            case "R": {
                llFechar.setVisibility(View.VISIBLE);
                break;
            }
            case "C":{
                llFechar.setVisibility(View.VISIBLE);
                break;
            }
        }

            AtualizarDescricaoOferta(mTroca.getJogosTroca().getJogoOferta());
        }
    }


    private void AtualizarDescricaoOferta(Jogo jogo){
        txtDetalheTituloOferta.setText(jogo.getNomejogo());
        txtDetalheAnoOferta.setText(String.valueOf(jogo.getAno()));
        txtDetalheCategoriaOferta.setText(ParserArray.categoriaJogo(jogo.getCategoria()));
        txtDetalheDescricaoOferta.setText(jogo.getDescricao());
        txtDetalhePlataformaOferta.setText(ParserArray.plataformaJogo(jogo.getPlataforma().getId()));
        txtDetalheNomeUsuarioOferta.setText(mTroca.getJogosTroca().getNomeUsuarioOferta());

        imagemBusca = IMG_BUSCA.IMG_OFERTA;
        imagemCacheOferta.setCodJogo(jogo.getId());
        if (imagemCacheOferta.imageInCacheDir()){
            imagemJogoOferta.setImageBitmap(imagemCacheOferta.loadImageCacheDir());
            AtualizarDescricaoTroca(mTroca.getJogosTroca());
        }else{
            imagemJogoOferta.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));
            imagemCacheOferta.loadImageRemoteServer();
        }
    }

    private void AtualizarDescricaoTroca(ItemJogoTroca itemJogoTroca){
        txtDetalheTituloTroca.setText(itemJogoTroca.getJogoTroca().getNomejogo());
        txtDetalheAnoTroca.setText(String.valueOf(itemJogoTroca.getJogoTroca().getAno()));
        txtDetalheCategoriaTroca.setText(ParserArray.categoriaJogo(itemJogoTroca.getJogoTroca().getCategoria()));
        txtDetalheDescricaoTroca.setText(itemJogoTroca.getJogoTroca().getDescricao());
        txtDetalhePlataformaTroca.setText(ParserArray.plataformaJogo(itemJogoTroca.getJogoTroca().getPlataforma().getId()));
        txtDetalheNomeUsuarioTroca.setText(itemJogoTroca.getNomeUsuarioTroca());

        imagemBusca = IMG_BUSCA.IMG_TROCA;
        imagemCacheTroca.setCodJogo(itemJogoTroca.getJogoTroca().getId());
        if (imagemCacheTroca.imageInCacheDir()){
            imagemJogoTroca.setImageBitmap(imagemCacheTroca.loadImageCacheDir());
        }else{
            imagemJogoTroca.setImageBitmap(ImagemUtil.getBitmapFromString(itemJogoTroca.getJogoTroca().getImagem()));
            imagemCacheTroca.loadImageRemoteServer();
        }
    }

    @Override
    public void onTaskCompleteImageCache() throws JSONException {
        if (imagemBusca == IMG_BUSCA.IMG_OFERTA) {
            imagemJogoOferta.setImageBitmap(imagemCacheOferta.loadImageCacheDir());
            AtualizarDescricaoTroca(mTroca.getJogosTroca());
        }
        else
            imagemJogoTroca.setImageBitmap(imagemCacheTroca.loadImageCacheDir());
    }

    public void dadosUsuario(View view){
        tipo_busca = TIPO_BUSCA.tp_busca_dados_usuario;
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Buscando dados do usuário", this);
        webServiceTask.addParameter("id", String.valueOf(mTroca.getJogosTroca().getIdUsuarioTroca()));
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "DadosUsuario"});
    }


    public void fecharDetalhes(View view){
        finish();
    }


    public void aceitarTroca(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Aceitar");
        builder.setMessage("Deseja aceitar a troca?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTroca.setStatusTroca(StatusTroca.TROCA_ANDAMENTO);
                aceitarRejeitarTroca(StatusTroca.TROCA_ANDAMENTO);
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void rejeitarTroca(View view){

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Rejeitar");
        builder.setMessage("Deseja rejeitar a troca?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTroca.setStatusTroca(StatusTroca.TROCA_REJEITADA);
                aceitarRejeitarTroca(StatusTroca.TROCA_REJEITADA);
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void aceitarRejeitarTroca(StatusTroca statusTroca){
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Atualizando troca", this);
        webServiceTask.addParameter("idTroca", String.valueOf(idTroca));
        webServiceTask.addParameter("statusTroca", statusTroca.toString());
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "AtualizarStatusTroca"});
    }

    public void cancelarTroca(View view){
        mTroca.setStatusTroca(StatusTroca.TROCA_CANCELADA);
        tipo_busca = TIPO_BUSCA.tp_none;
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Cancelando troca", this);
        webServiceTask.addParameter("idTroca", String.valueOf(idTroca));
        webServiceTask.addParameter("statusTroca", mTroca.getStatusTroca().toString());
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "AtualizarStatusTroca"});
    }

    public void concluirTroca(View view){
        mTroca.setStatusTroca(StatusTroca.TROCA_CONCLUIDA);
        tipo_busca = TIPO_BUSCA.tp_none;
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Concluindo troca", this);
        webServiceTask.addParameter("idTroca", String.valueOf(idTroca));
        webServiceTask.addParameter("statusTroca", mTroca.getStatusTroca().toString());
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "AtualizarStatusTroca"});
    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (tipo_busca == TIPO_BUSCA.tp_busca_dados_usuario){
            Intent it = new Intent(this, ActivityDadosUsuarioTroca.class);

            it.putExtra("id", result.getInt("id"));
            it.putExtra("nome", result.getString("nome"));
            it.putExtra("nomeusuario", result.getString("nomeUsuario"));
            it.putExtra("telefone", result.getString("telefone"));
            it.putExtra("email", result.getString("email"));
            startActivity(it);
        }
        else
            if ((!result.toString().equals("")) || (!result.toString().equals("{}"))){
                if (result.getInt("codResultado") == 1) {
                    TrocaCRUD trocaCRUD = new TrocaCRUD(this);
                    trocaCRUD.atualizarStatusTroca(idTroca, mTroca.getStatusTroca());

                    if (mTroca.getStatusTroca().toString().equals(StatusTroca.TROCA_CONCLUIDA.toString())){
                        TrocaConcluida trocaConcluida = new TrocaConcluida(mTroca.getId(), this, mUsuario);
                        trocaConcluida.efetuarTroca();
                    }

                    finish();
                }
        }
    }

    @Override
    public void onTaskReturnNull(String msg) {

    }

}
