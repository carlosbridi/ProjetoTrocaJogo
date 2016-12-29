package com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca;

import android.os.Parcel;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;

import java.io.Serializable;

/**
 * Created by carlosbridi on 19/01/16.
 */
public class ItemJogoTroca implements Serializable {

    private Jogo jogoOferta;
    private Jogo jogoTroca;

    private int idUsuarioOferta;
    private int idUsuarioTroca;
    private String nomeUsuarioTroca;
    private String nomeUsuarioOferta;

    public ItemJogoTroca(){
        this.jogoOferta = new Jogo();
        this.jogoTroca = new Jogo();
    }

    public Jogo getJogoOferta() {
        return jogoOferta;
    }

    public void setJogoOferta(Jogo jogoOferta) {
        this.jogoOferta = jogoOferta;
    }

    public Jogo getJogoTroca() {
        return jogoTroca;
    }

    public void setJogoTroca(Jogo jogoTroca) {
        this.jogoTroca = jogoTroca;
    }

    public int getIdUsuarioOferta() {
        return idUsuarioOferta;
    }

    public void setIdUsuarioOferta(int idUsuarioOferta) {
        this.idUsuarioOferta = idUsuarioOferta;
    }

    public int getIdUsuarioTroca() {
        return idUsuarioTroca;
    }

    public void setIdUsuarioTroca(int idUsuarioTroca) {
        this.idUsuarioTroca = idUsuarioTroca;
    }

    public String getNomeUsuarioTroca() {
        return nomeUsuarioTroca;
    }

    public void setNomeUsuarioTroca(String nomeUsuarioTroca) {
        this.nomeUsuarioTroca = nomeUsuarioTroca;
    }

    public String getNomeUsuarioOferta() {
        return nomeUsuarioOferta;
    }

    public void setNomeUsuarioOferta(String nomeUsuarioOferta) {
        this.nomeUsuarioOferta = nomeUsuarioOferta;
    }
}
