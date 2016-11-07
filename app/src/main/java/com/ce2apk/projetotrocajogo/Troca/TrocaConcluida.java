package com.ce2apk.projetotrocajogo.Troca;

import android.content.Context;

import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoDAO;
import com.ce2apk.projetotrocajogo.Jogo.JogoUtil;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosbridi on 22/04/16.
 */
public class TrocaConcluida {

    private int idTroca;
    private Context mContext;
    private Troca mTroca;
    private Usuario mUsuario;

    public TrocaConcluida(int idTroca, Context context, Usuario usuario){
        this.idTroca = idTroca;
        this.mContext = context;
        this.mUsuario = usuario;

        TrocaCRUD trocaCRUD = new TrocaCRUD(mContext);

        mTroca = trocaCRUD.buscarTroca(idTroca);
    }

    public void efetuarTroca(){

        JogoCRUD jogoCRUD = new JogoCRUD(mContext);

        JogoDAO jogoDAO = new JogoDAO(mContext);


        if (mTroca.getJogosTroca().getIdUsuarioOferta() == mUsuario.getId()){
            jogoCRUD.removerJogoColecao(mTroca.getJogosTroca().getJogoOferta().getId(), mTroca.getJogosTroca().getJogoOferta().getPlataforma().getId());
            if (!jogoDAO.jogoNaColecao(mTroca.getJogosTroca().getJogoTroca().getId(), mTroca.getJogosTroca().getJogoTroca().getPlataforma().getId())){
                jogoCRUD.inserirJogoColecao(mTroca.getJogosTroca().getJogoTroca());
            }
        }else{
            jogoCRUD.removerJogoColecao(mTroca.getJogosTroca().getJogoTroca().getId(), mTroca.getJogosTroca().getJogoTroca().getPlataforma().getId());
            if (!jogoDAO.jogoNaColecao(mTroca.getJogosTroca().getJogoOferta().getId(), mTroca.getJogosTroca().getJogoOferta().getPlataforma().getId())){
               jogoCRUD.inserirJogoColecao(mTroca.getJogosTroca().getJogoOferta());
            }
        }

    }


}
