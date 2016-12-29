package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.Imagens.AsyncTaskCompleteImageCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoBuscaUsuario;
import com.ce2apk.projetotrocajogo.Jogo.TempJogoBuscaCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTrocaCRUD;
import com.ce2apk.projetotrocajogo.Troca.StatusTroca;
import com.ce2apk.projetotrocajogo.Troca.Troca;
import com.ce2apk.projetotrocajogo.Troca.TrocaCRUD;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.ParserArray;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by carlosbridi on 05/03/16.
 */
public class ActivityNovaTroca extends FragmentActivity implements AsyncTaskCompleteImageCache, AsyncTaskCompleteListener {

    private ImageView imgJogoTroca;
    private ImageView imgJogoOferta;

    private TextView txtTituloTroca;
    private TextView txtAnoTroca;
    private TextView txtCategoriaTroca;
    private TextView txtPlataformaTroca;
    private TextView txtDescricaoTroca;
    private TextView txtNomeUsuario;

    private TextView txtTituloOferta;
    private TextView txtAnoOferta;
    private TextView txtCategoriaOferta;
    private TextView txtPlataformaOferta;
    private TextView txtDescricaoOferta;

    private ImagemCache imagemCache;

    private Troca novaTroca;
    private ItemJogoTroca itemTroca;
    private TempJogoBuscaCRUD temp_jogoBuscaCRUD;

    private enum IMG_BUSCA  {IMG_OFERTA, IMG_TROCA};

    private IMG_BUSCA imagemBusca;

    private final int COD_BUSCAR_JOGO_TROCA = 190;
    private final int COD_BUSCAR_JOGO_OFERTA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca);

        imgJogoOferta = (ImageView) findViewById(R.id.imageTrocaColecao);
        txtTituloOferta = (TextView) findViewById(R.id.txtTituloOferta);
        txtCategoriaOferta = (TextView) findViewById(R.id.txtCategoriaOferta);
        txtPlataformaOferta = (TextView) findViewById(R.id.txtPlataformaOferta);
        txtAnoOferta = (TextView) findViewById(R.id.txtAnoOferta);
        txtDescricaoOferta = (TextView) findViewById(R.id.txtDescricaoOferta);
        txtNomeUsuario = (TextView) findViewById(R.id.txtNomeUsuario);

        imgJogoTroca = (ImageView) findViewById(R.id.imageTrocaOferta);
        txtTituloTroca = (TextView) findViewById(R.id.txtTituloTroca);
        txtCategoriaTroca = (TextView) findViewById(R.id.txtCategoriaTroca);
        txtPlataformaTroca = (TextView) findViewById(R.id.txtPlataformaTroca);
        txtAnoTroca = (TextView) findViewById(R.id.txtAnoTroca);
        txtDescricaoTroca = (TextView) findViewById(R.id.txtDescricaoTroca);

        imagemCache = new ImagemCache();
        imagemCache.setContext(this);
        imagemCache.setCallback(this);

        itemTroca = new ItemJogoTroca();
        itemTroca.setJogoOferta(new Jogo());
        itemTroca.setJogoTroca(new Jogo());

        novaTroca = new Troca();
        novaTroca.setDataTroca(new Date());
        novaTroca.setStatusTroca(StatusTroca.TROCA_ANALISE);
        novaTroca.setJogosTroca(itemTroca);

        Usuario usuario = UsuarioUtil.obterUsuario(this);
        novaTroca.getJogosTroca().setIdUsuarioOferta(usuario.getId());
        novaTroca.getJogosTroca().setNomeUsuarioOferta(usuario.getNome());

        temp_jogoBuscaCRUD = new TempJogoBuscaCRUD(this);

        imgJogoTroca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagemBusca = IMG_BUSCA.IMG_TROCA;
                Intent it = new Intent(v.getContext(), ActivityBuscarJogoUsuario.class);
                startActivityForResult(it, COD_BUSCAR_JOGO_TROCA);
            }
        });

        imgJogoOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagemBusca = IMG_BUSCA.IMG_OFERTA;
                Intent it = new Intent(v.getContext(), ActivityListarColecao.class);
                startActivityForResult(it, COD_BUSCAR_JOGO_OFERTA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COD_BUSCAR_JOGO_OFERTA){
            if (resultCode == RESULT_OK){
                int idJogo = data.getExtras().getInt("idJogo", 0);
                JogoCRUD jogoCRUD = new JogoCRUD(this);

                Jogo jogo = jogoCRUD.obterDadosJogo(idJogo);
                itemTroca.setJogoOferta(jogo);

                AtualizarDescricaoOferta(jogo);
            }
        }else{
            if (resultCode == RESULT_OK){
                int idJogo = data.getExtras().getInt("idCodJogo", 0);
                JogoBuscaUsuario itensJogoTroca = temp_jogoBuscaCRUD.obterDadosJogo(idJogo);
                itemTroca.getJogoTroca().setId(itensJogoTroca.getId());
                itemTroca.getJogoTroca().setImagem(itensJogoTroca.getImagem());
                itemTroca.getJogoTroca().setAno(itensJogoTroca.getAno());
                itemTroca.getJogoTroca().setCategoria(itensJogoTroca.getCategoria());
                itemTroca.getJogoTroca().setPlataforma(itensJogoTroca.getPlataforma());
                itemTroca.getJogoTroca().setDescricao(itensJogoTroca.getDescricao());
                itemTroca.getJogoTroca().setNomejogo(itensJogoTroca.getNomejogo());
                itemTroca.getJogoTroca().setIdJogoPlataforma(itensJogoTroca.getIdJogoPlataforma());

                novaTroca.getJogosTroca().setNomeUsuarioTroca(itensJogoTroca.getNomeUsuarioTroca());
                novaTroca.getJogosTroca().setIdUsuarioTroca(itensJogoTroca.getIdUsuarioTroca());
                AtualizarDescricaoTroca(itensJogoTroca);
            }
        }
    }

    private void AtualizarDescricaoOferta(Jogo jogo){
        txtTituloOferta.setText(jogo.getNomejogo());
        txtAnoOferta.setText(String.valueOf(jogo.getAno()));
        txtCategoriaOferta.setText(ParserArray.categoriaJogo(jogo.getCategoria()));
        txtDescricaoOferta.setText(jogo.getDescricao().substring(0, 150) + "(...)");
        txtPlataformaOferta.setText(ParserArray.plataformaJogo(jogo.getPlataforma().getId()));

        imagemCache.setCodJogo(jogo.getId());
        if (imagemCache.imageInCacheDir()){
            imgJogoOferta.setImageBitmap(imagemCache.loadImageCacheDir());
        }else{
            imgJogoOferta.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));
            imagemCache.loadImageRemoteServer();
        }
    }

    private void AtualizarDescricaoTroca(JogoBuscaUsuario itensJogoTroca){
        txtTituloTroca.setText(itensJogoTroca.getNomejogo());
        txtAnoTroca.setText(String.valueOf(itensJogoTroca.getAno()));
        txtCategoriaTroca.setText(ParserArray.categoriaJogo(itensJogoTroca.getCategoria()));
        txtDescricaoTroca.setText(itensJogoTroca.getDescricao().substring(0, 150) + "(...)");
        txtPlataformaTroca.setText(ParserArray.plataformaJogo(itensJogoTroca.getPlataforma().getId()));
        txtNomeUsuario.setText(itensJogoTroca.getNomeUsuarioTroca());

        imagemCache.setCodJogo(itensJogoTroca.getId());
        if (imagemCache.imageInCacheDir()){
            imgJogoTroca.setImageBitmap(imagemCache.loadImageCacheDir());
        }else{
            imgJogoTroca.setImageBitmap(ImagemUtil.getBitmapFromString(itensJogoTroca.getImagem()));
            imagemCache.loadImageRemoteServer();
        }
    }

    public void cancelarTroca(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Troca");
        builder.setMessage("Deseja cancelar a troca?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    public void proporTroca(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Troca");
        builder.setMessage("Propor troca?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gravarTroca();
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

    private void gravarTroca(){
        novaTroca.getJogosTroca().getJogoTroca().setNomejogo(temp_jogoBuscaCRUD.obterDadosJogo(novaTroca.getJogosTroca().getJogoTroca().getId()).getNomejogo());
        novaTroca.getJogosTroca().setNomeUsuarioTroca(novaTroca.getJogosTroca().getNomeUsuarioTroca());

        if(isTrocaValida())
            sincronizarTroca();
    }

    private boolean isTrocaValida(){
        boolean retorno = true;

        if (novaTroca.getJogosTroca().getJogoOferta().getId() == 0){
            new SnackBar.Builder(this)
                    .withMessage("Nenhum jogo de oferta escolhido!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            retorno = false;
        }

        if ((retorno) && (novaTroca.getJogosTroca().getJogoTroca().getId() == 0)){
            new SnackBar.Builder(this)
                    .withMessage("Nenhum jogo de troca escolhido!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            retorno = false;
        }

        if ((retorno) &&
                ((novaTroca.getJogosTroca().getJogoOferta().getId() == novaTroca.getJogosTroca().getJogoTroca().getId()) &&
                novaTroca.getJogosTroca().getJogoOferta().getPlataforma().getId() == novaTroca.getJogosTroca().getJogoTroca().getPlataforma().getId())){
            new SnackBar.Builder(this)
                    .withMessage("Jogo de troca e de oferta são os mesmos!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            retorno = false;
        }

        ItemJogoTrocaCRUD itemJogoTrocaCRUD = new ItemJogoTrocaCRUD(this);

        if ((retorno) && (itemJogoTrocaCRUD.itemJogoTrocaEmOferta(novaTroca.getJogosTroca().getJogoOferta().getId(), novaTroca.getJogosTroca().getJogoTroca().getPlataforma().getId()))){
            new SnackBar.Builder(this)
                    .withMessage("Jogo de oferta já está participando de uma troca!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            retorno = false;
        }

        if ((retorno) && (itemJogoTrocaCRUD.itemJogoTrocaEmTroca(novaTroca.getJogosTroca().getJogoTroca().getId(), novaTroca.getJogosTroca().getJogoTroca().getPlataforma().getId()))){
            new SnackBar.Builder(this)
                    .withMessage("Jogo de troca já está participando de uma troca!")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();

            retorno = false;
        }


        return retorno;
    }


    private void sincronizarTroca(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            WebServiceTask wst = new WebServiceTask(WebServiceTask.PUT_TASK, this, "Gravando troca. Aguarde..", this);
            wst.addParameter("idTroca", String.valueOf(novaTroca.getId()));
            wst.addParameter("statusTroca", novaTroca.getStatusTroca().toString());
            wst.addParameter("dataTroca", simpleDateFormat.format(novaTroca.getDataTroca()));

            //Dados dos itens
            wst.addParameter("idUsuarioOferta", String.valueOf(novaTroca.getJogosTroca().getIdUsuarioOferta()));
            wst.addParameter("idUsuarioTroca", String.valueOf(novaTroca.getJogosTroca().getIdUsuarioTroca()));
            wst.addParameter("idJogoTroca", String.valueOf(novaTroca.getJogosTroca().getJogoTroca().getId()));
            wst.addParameter("idJogoOferta", String.valueOf(novaTroca.getJogosTroca().getJogoOferta().getId()));
            wst.addParameter("idJogoPlataformaOferta", String.valueOf(novaTroca.getJogosTroca().getJogoOferta().getIdJogoPlataforma()));
            wst.addParameter("idJogoPlataformaTroca", String.valueOf(novaTroca.getJogosTroca().getJogoTroca().getIdJogoPlataforma()));

            wst.execute(new String[]{consts.SERVICE_URL + "TrocaWS"});
        }catch(Exception e){
            new SnackBar.Builder(this)
                    .withMessage("Problemas ao registrar a troca no servidor.")
                    .withStyle(SnackBar.Style.DEFAULT)
                    .withDuration((short)3000)
                    .show();
        }

    }

    private void gravarTrocaLocal(int idTroca){
        novaTroca.setId(idTroca);
        TrocaCRUD trocaCRUD = new TrocaCRUD(getApplicationContext());
        trocaCRUD.inserirTroca(novaTroca);
    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (!result.toString().equals("{}")) {
            if (result.getInt("codResultado") > 0) {
                gravarTrocaLocal(result.getInt("codResultado"));

                new SnackBar.Builder(this)
                        .withMessage("Troca incluída com sucesso")
                        .withStyle(SnackBar.Style.DEFAULT)
                        .withDuration((short)3000)
                        .show();

                finish();
            }else{
                new SnackBar.Builder(this)
                        .withMessage("Ocorreu um erro ao incluir a troca. Tente novamente!")
                        .withStyle(SnackBar.Style.DEFAULT)
                        .withDuration((short)3000)
                        .show();
            }
        }else {
            new SnackBar.Builder(this)
                    .withMessage("Falha ao inserir uma troca. Verifique seu acesso a internet e tente novamente.")
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
    public void onTaskCompleteImageCache() throws JSONException {
        if (imagemBusca == IMG_BUSCA.IMG_OFERTA)
            imgJogoOferta.setImageBitmap(imagemCache.loadImageCacheDir());
        else
            imgJogoTroca.setImageBitmap(imagemCache.loadImageCacheDir());
    }
}
