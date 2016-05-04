package com.ce2apk.projetotrocajogo.Jogo;

import com.ce2apk.projetotrocajogo.Troca.ItensJogoTroca;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosbridi on 20/02/16.
 */
public class JogoUtil {

    public static Jogo pareserJogo(JSONObject jsonObject){
        JSONObject field = jsonObject;

        Jogo jogo = new Jogo();

        try {
            jogo.setId(field.getInt("id"));
            jogo.setNomejogo(field.getString("nomejogo"));
            jogo.setDescricao(field.getString("descricao"));
            jogo.setCategoria(field.getInt("categoria"));
            jogo.setPlataforma(field.getInt("plataforma"));
            jogo.setCategoria(field.getInt("categoria"));
            jogo.setAno(field.getInt("ano"));
            if (field.has("imagem"))
                jogo.setImagem(field.getString("imagem"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jogo;
    }


    public static ItensJogoTroca pareserJogoItensTroca(JSONObject jsonJogos){
        JSONObject field = jsonJogos;

        ItensJogoTroca itensJogoTroca = new ItensJogoTroca();

        try {
            itensJogoTroca.getJogoOferta().setId(jsonJogos.getInt("idOferta"));
            itensJogoTroca.getJogoOferta().setNomejogo(jsonJogos.getString("nomejogooferta"));
            itensJogoTroca.getJogoOferta().setDescricao(jsonJogos.getString("descricaooferta"));
            itensJogoTroca.getJogoOferta().setCategoria(jsonJogos.getInt("categoriaoferta"));
            itensJogoTroca.getJogoOferta().setPlataforma(jsonJogos.getInt("plataformaoferta"));
            itensJogoTroca.getJogoOferta().setImagem(jsonJogos.getString("imagemoferta"));
            itensJogoTroca.getJogoOferta().setAno(jsonJogos.getInt("anooferta"));

            itensJogoTroca.getJogoTroca().setId(jsonJogos.getInt("idtroca"));
            itensJogoTroca.getJogoTroca().setNomejogo(jsonJogos.getString("nomejogotroca"));
            itensJogoTroca.getJogoTroca().setDescricao(jsonJogos.getString("descricaotroca"));
            itensJogoTroca.getJogoTroca().setCategoria(jsonJogos.getInt("categoriatroca"));
            itensJogoTroca.getJogoTroca().setPlataforma(jsonJogos.getInt("plataformatroca"));
            itensJogoTroca.getJogoTroca().setImagem(jsonJogos.getString("imagemtroca"));
            itensJogoTroca.getJogoTroca().setAno(jsonJogos.getInt("anotroca"));

            itensJogoTroca.setNomeUsuarioTroca(jsonJogos.getString("nomeUsuarioTroca"));
            itensJogoTroca.setNomeUsuarioOferta(jsonJogos.getString("nomeUsuarioOferta"));
            itensJogoTroca.setIdUsuarioTroca(jsonJogos.getInt("idUsuarioTroca"));
            itensJogoTroca.setIdUsuarioOferta(jsonJogos.getInt("idUsuarioOferta"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        return itensJogoTroca;
    }


    public static TempJogoBusca parserTempBusca(JSONObject jsonObject){
        JSONObject field = jsonObject;

        TempJogoBusca itensJogoTroca = new TempJogoBusca();

        try {
            itensJogoTroca.setId(field.getInt("id"));
            //itensJogoTroca.setId(field.getInt("idJogoTroca"));
            itensJogoTroca.setNomejogo(field.getString("nomejogo"));
            itensJogoTroca.setDescricao(field.getString("descricao"));
            itensJogoTroca.setCategoria(field.getInt("categoria"));
            itensJogoTroca.setPlataforma(field.getInt("plataforma"));
            itensJogoTroca.setCategoria(field.getInt("categoria"));
            itensJogoTroca.setAno(field.getInt("ano"));
            if (field.has("imagem"))
                itensJogoTroca.setImagem(field.getString("imagem"));
            itensJogoTroca.setIdUsuarioTroca(field.getInt("idUsuarioTroca"));
            itensJogoTroca.setNomeUsuarioTroca(field.getString("nomeUsuarioTroca"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        return itensJogoTroca;
    }

}
