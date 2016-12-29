package com.ce2apk.projetotrocajogo.Jogo;

import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        try {
            JSONArray jsonArrayJogo = jsonObject.getJSONArray("Jogo");
            for (int x = 0; x < jsonArrayJogo.length(); x++) {
                JSONObject jsonJogo = jsonArrayJogo.getJSONObject(x);
                List<Plataforma> listaPlataforma = obterPlataformas(jsonJogo);

                for (Plataforma xPlataforma : listaPlataforma) {
                    Jogo jogo = extrairJogoJSON(jsonJogo);
                    jogo.setPlataforma(xPlataforma)
                        .setIdJogoPlataforma(listaIdJogoPlataforma.get(xControle));

                    listaJogo.add(jogo);
                    xControle = xControle + 1;
                }
            }
        } catch (JSONException e) {
            JSONObject jsonJogo = jsonObject.getJSONObject("jogo");
            List<Plataforma> listaPlataforma = obterPlataformas(jsonJogo);

            for (Plataforma xPlataforma : listaPlataforma) {
                Jogo jogo = extrairJogoJSON(jsonJogo);
                jogo.setPlataforma(xPlataforma)
                    .setIdJogoPlataforma(listaIdJogoPlataforma.get(xControle));

                listaJogo.add(jogo);
                xControle = +1;
            }
        }
        return listaJogo;
    }

    public static List<Jogo> parserJogoUsuario(JSONObject jsonObject) throws JSONException{
        List<Jogo> listaJogoUsuario = new ArrayList<>();
        try{
            JSONArray jsonArrayJogoUsuario = jsonObject.getJSONArray("JogoUsuarioDTO");
            for (int i = 0; i< jsonArrayJogoUsuario.length(); i++){
                JSONObject jsonJogo = jsonArrayJogoUsuario.getJSONObject(i);

                Jogo jogo = extrairJogoJSON(jsonJogo.getJSONObject("jogo"));
                jogo.setIdJogoPlataforma(jsonJogo.getJSONObject("jogoPlataforma").getInt("id"));
                jogo.setPlataforma(new Plataforma(jsonJogo.getJSONObject("jogoPlataforma").getJSONObject("plataforma").getInt("id")));
                jogo.setInteresse(jsonJogo.getBoolean("interesse"));
                listaJogoUsuario.add(jogo);
            }
        }catch(JSONException e){
            JSONObject jsonJogo = jsonObject.getJSONObject("JogoUsuarioDTO");

            Jogo jogo = extrairJogoJSON(jsonJogo.getJSONObject("jogo"));
            jogo.setIdJogoPlataforma(jsonJogo.getJSONObject("jogoPlataforma").getInt("id"));
            jogo.setPlataforma(new Plataforma(jsonJogo.getJSONObject("jogoPlataforma").getJSONObject("plataforma").getInt("id")));
            jogo.setInteresse(jsonJogo.getBoolean("interesse"));
            listaJogoUsuario.add(jogo);
        }

        return listaJogoUsuario;
    }

    public static Jogo extrairJogoJSON(JSONObject jsonJogo) throws JSONException{
        Jogo jogo = new Jogo();
        jogo.setId(jsonJogo.getInt("id"))
                .setDescricao(jsonJogo.getString("descricao"))
                .setNomejogo(jsonJogo.getString("nomejogo"))
                .setAno(jsonJogo.getInt("ano"))
                .setCategoria(jsonJogo.getInt("categoria"))
                .setImagem(jsonJogo.getString("imagem"));
        return jogo;
    }

    public static List<Plataforma> obterPlataformas(JSONObject jsonObjectPlataforma) throws JSONException {
        List<Plataforma> listaPlataforma = new ArrayList<Plataforma>();

        try{
            JSONArray jsonArrayPlataforma = jsonObjectPlataforma.getJSONArray("jogoPlataforma");
            for (int x = 0; x < jsonArrayPlataforma.length(); x++){
                listaIdJogoPlataforma.add(jsonArrayPlataforma.getJSONObject(x).getInt("id"));
                JSONObject jsonPlataforma = jsonArrayPlataforma.getJSONObject(x).getJSONObject("plataforma");
                listaPlataforma.add(new Plataforma(jsonPlataforma.getInt("id")));
            }
        }catch (JSONException e){
            listaIdJogoPlataforma.add(jsonObjectPlataforma.getJSONObject("jogoPlataforma").getInt("id"));
            JSONObject jsonPlataforma = jsonObjectPlataforma.getJSONObject("jogoPlataforma").getJSONObject("plataforma");
            listaPlataforma.add(new Plataforma(jsonPlataforma.getInt("id")));
        }
        return listaPlataforma;
    }


    public static ItemJogoTroca pareserJogoItensTroca(JSONObject jsonJogos){
        JSONObject field = jsonJogos;

        ItemJogoTroca itemJogoTroca = new ItemJogoTroca();

        try {
            itemJogoTroca.getJogoOferta().setId(jsonJogos.getInt("idOferta"));
            itemJogoTroca.getJogoOferta().setNomejogo(jsonJogos.getString("nomejogooferta"));
            itemJogoTroca.getJogoOferta().setDescricao(jsonJogos.getString("descricaooferta"));
            itemJogoTroca.getJogoOferta().setCategoria(jsonJogos.getInt("categoriaoferta"));
            itemJogoTroca.getJogoOferta().setPlataforma(new Plataforma(jsonJogos.getInt("plataformaoferta")));
            itemJogoTroca.getJogoOferta().setImagem(jsonJogos.getString("imagemoferta"));
            itemJogoTroca.getJogoOferta().setAno(jsonJogos.getInt("anooferta"));

            itemJogoTroca.getJogoTroca().setId(jsonJogos.getInt("idtroca"));
            itemJogoTroca.getJogoTroca().setNomejogo(jsonJogos.getString("nomejogotroca"));
            itemJogoTroca.getJogoTroca().setDescricao(jsonJogos.getString("descricaotroca"));
            itemJogoTroca.getJogoTroca().setCategoria(jsonJogos.getInt("categoriatroca"));
            itemJogoTroca.getJogoTroca().setPlataforma(new Plataforma(jsonJogos.getInt("plataformatroca")));
            itemJogoTroca.getJogoTroca().setImagem(jsonJogos.getString("imagemtroca"));
            itemJogoTroca.getJogoTroca().setAno(jsonJogos.getInt("anotroca"));

            itemJogoTroca.setNomeUsuarioTroca(jsonJogos.getString("nomeUsuarioTroca"));
            itemJogoTroca.setNomeUsuarioOferta(jsonJogos.getString("nomeUsuarioOferta"));
            itemJogoTroca.setIdUsuarioTroca(jsonJogos.getInt("idUsuarioTroca"));
            itemJogoTroca.setIdUsuarioOferta(jsonJogos.getInt("idUsuarioOferta"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        return itemJogoTroca;
    }


    public static JogoBuscaUsuario parserTempBusca(JSONObject jsonObject){
        JSONObject field = jsonObject;

        JogoBuscaUsuario itensJogoTroca = new JogoBuscaUsuario();

        try {
            JSONObject jsonJogoPlataforma = field.getJSONObject("jogoPlataforma");

            itensJogoTroca.setIdUsuarioTroca(field.getInt("idUsuario"));
            itensJogoTroca.setNomeUsuarioTroca(field.getString("nomeUsuario"));

            itensJogoTroca.setId(field.getJSONObject("jogo").getInt("id"));
            itensJogoTroca.setNomejogo(field.getJSONObject("jogo").getString("nomejogo"));
            itensJogoTroca.setDescricao(field.getJSONObject("jogo").getString("descricao"));
            itensJogoTroca.setCategoria(field.getJSONObject("jogo").getInt("categoria"));
            itensJogoTroca.setIdJogoPlataforma(jsonJogoPlataforma.getInt("id"));
            itensJogoTroca.setPlataforma(new Plataforma(jsonJogoPlataforma.getJSONObject("plataforma").getInt("id")));
            itensJogoTroca.setCategoria(field.getJSONObject("jogo").getInt("categoria"));
            itensJogoTroca.setAno(field.getJSONObject("jogo").getInt("ano"));
            itensJogoTroca.setImagem(field.getJSONObject("jogo").getString("imagem"));


        }catch (JSONException e){
            e.printStackTrace();
        }

        return itensJogoTroca;
    }

}
