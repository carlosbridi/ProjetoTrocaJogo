package com.ce2apk.projetotrocajogo.Util;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 14/04/15.
 */
public class ListaJogos implements Serializable {

    private List<Jogo> listaJogo;

    public ListaJogos(){
        listaJogo = new ArrayList<Jogo>();
    }

    public boolean addJogo(Jogo jogo){
        return listaJogo.add(jogo);
    }

    public int getSize(){
        return listaJogo.size();
    }

    public List<Jogo> getListaJogo(){
        return listaJogo;
    }

    public Jogo get(int position) { return listaJogo.get(position); };

}
