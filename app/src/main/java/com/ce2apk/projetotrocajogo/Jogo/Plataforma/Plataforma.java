package com.ce2apk.projetotrocajogo.Jogo.Plataforma;

import com.ce2apk.projetotrocajogo.Util.ParserArray;

/**
 * Created by carlosbridi on 27/10/16.
 */
public class Plataforma {

    private int id;
    private String descricao;

    public Plataforma(){

    }

    public Plataforma(int id, String descricao){
        super();
        this.id = id;
        this.descricao = descricao;
    }

    public Plataforma(int idPlataforma){
        super();
        this.id = idPlataforma;
        this.descricao = ParserArray.plataformaJogo(this.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
