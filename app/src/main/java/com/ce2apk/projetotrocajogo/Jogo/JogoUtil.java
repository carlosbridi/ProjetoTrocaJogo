package com.ce2apk.projetotrocajogo.Jogo;

import android.util.JsonToken;

import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Troca.ItensJogoTroca;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 20/02/16.
 */
public class JogoUtil {

    private static List<Integer> listaIdJogoPlataforma;

    public static List<Jogo> parserJogo(JSONObject jsonObject) throws JSONException {
        List<Jogo> listaJogo = new ArrayList<Jogo>();
        listaIdJogoPlataforma = new ArrayList();
        int xControle = 0;

        try{
            JSONArray jsonArrayJogo = jsonObject.getJSONArray("Jogo");
            for (int x = 0; x< jsonArrayJogo.length(); x++){
                JSONObject jsonJogo = jsonArrayJogo.getJSONObject(x);
                List<Plataforma> listaPlataforma = obterPlataformas(jsonJogo);

                for (Plataforma xPlataforma : listaPlataforma) {
                    Jogo jogo = new Jogo();
                    jogo.setId(jsonJogo.getInt("id"))
                            .setDescricao(jsonJogo.getString("descricao"))
                            .setNomejogo(jsonJogo.getString("nomejogo"))
                            .setAno(jsonJogo.getInt("ano"))
                            .setCategoria(jsonJogo.getInt("categoria"))
                            .setPlataforma(xPlataforma)
                            .setIdJogoPlataforma(listaIdJogoPlataforma.get(xControle))
                            .setImagem(jsonJogo.getString("imagem"));

                    listaJogo.add(jogo);
                    xControle = xControle + 1;
                }
            }
        }catch (JSONException e){
            JSONObject jsonJogo = jsonObject.getJSONObject("jogo");
            List<Plataforma> listaPlataforma = obterPlataformas(jsonJogo);

            for (Plataforma xPlataforma : listaPlataforma) {
                Jogo jogo = new Jogo();
                jogo.setId(jsonJogo.getInt("id"))
                        .setDescricao(jsonJogo.getString("descricao"))
                        .setNomejogo(jsonJogo.getString("nomejogo"))
                        .setAno(jsonJogo.getInt("ano"))
                        .setCategoria(jsonJogo.getInt("categoria"))
                        .setPlataforma(xPlataforma)
                        .setIdJogoPlataforma(listaIdJogoPlataforma.get(xControle))
                        .setImagem(jsonJogo.getString("imagem"));

                listaJogo.add(jogo);
                xControle =+ 1;
            }
        }
        return listaJogo;

    }

    public static List<Plataforma> obterPlataformas(JSONObject jsonObjectPlataforma) throws JSONException {
        List<Plataforma> listaPlataforma = new ArrayList<Plataforma>();

        try{
            JSONArray jsonArrayPlataforma = jsonObjectPlataforma.getJSONArray("plataforma");
            for (int x = 0; x < jsonArrayPlataforma.length(); x++){
                listaIdJogoPlataforma.add(jsonArrayPlataforma.getJSONObject(x).getInt("id"));
                JSONObject jsonPlataforma = jsonArrayPlataforma.getJSONObject(x).getJSONObject("plataforma");
                listaPlataforma.add(new Plataforma(jsonPlataforma.getInt("id")));
            }
        }catch (JSONException e){
            listaIdJogoPlataforma.add(jsonObjectPlataforma.getJSONObject("plataforma").getInt("id"));
            JSONObject jsonPlataforma = jsonObjectPlataforma.getJSONObject("plataforma").getJSONObject("plataforma");
            listaPlataforma.add(new Plataforma(jsonPlataforma.getInt("id")));
        }
        return listaPlataforma;
    }



    public static ItensJogoTroca pareserJogoItensTroca(JSONObject jsonJogos){
        JSONObject field = jsonJogos;

        ItensJogoTroca itensJogoTroca = new ItensJogoTroca();

        try {
            itensJogoTroca.getJogoOferta().setId(jsonJogos.getInt("idOferta"));
            itensJogoTroca.getJogoOferta().setNomejogo(jsonJogos.getString("nomejogooferta"));
            itensJogoTroca.getJogoOferta().setDescricao(jsonJogos.getString("descricaooferta"));
            itensJogoTroca.getJogoOferta().setCategoria(jsonJogos.getInt("categoriaoferta"));
            itensJogoTroca.getJogoOferta().setPlataforma(new Plataforma(jsonJogos.getInt("plataformaoferta")));
            itensJogoTroca.getJogoOferta().setImagem(jsonJogos.getString("imagemoferta"));
            itensJogoTroca.getJogoOferta().setAno(jsonJogos.getInt("anooferta"));

            itensJogoTroca.getJogoTroca().setId(jsonJogos.getInt("idtroca"));
            itensJogoTroca.getJogoTroca().setNomejogo(jsonJogos.getString("nomejogotroca"));
            itensJogoTroca.getJogoTroca().setDescricao(jsonJogos.getString("descricaotroca"));
            itensJogoTroca.getJogoTroca().setCategoria(jsonJogos.getInt("categoriatroca"));
            itensJogoTroca.getJogoTroca().setPlataforma(new Plataforma(jsonJogos.getInt("plataformatroca")));
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
            JSONObject jsonPlataforma = field.getJSONObject("plataforma");

            itensJogoTroca.setId(field.getInt("id"));
            //itensJogoTroca.setId(field.getInt("idJogoTroca"));
            itensJogoTroca.setNomejogo(field.getString("nomejogo"));
            itensJogoTroca.setDescricao(field.getString("descricao"));
            itensJogoTroca.setCategoria(field.getInt("categoria"));
            itensJogoTroca.setPlataforma(new Plataforma(jsonPlataforma.getInt("id")));
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
