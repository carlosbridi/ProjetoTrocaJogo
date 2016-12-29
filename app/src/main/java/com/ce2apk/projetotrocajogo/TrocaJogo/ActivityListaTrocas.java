package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;
import com.ce2apk.projetotrocajogo.Troca.StatusTroca;
import com.ce2apk.projetotrocajogo.Troca.Troca;
import com.ce2apk.projetotrocajogo.Troca.TrocaCRUD;
import com.ce2apk.projetotrocajogo.TrocaJogo.Adapters.ActivityTrocasListAdapter;
import com.ce2apk.projetotrocajogo.Usuario.Usuario;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 09/11/15.
 */

public class ActivityListaTrocas extends android.support.v4.app.ListFragment implements AsyncTaskCompleteListener{

    private TrocaCRUD trocaCRUD;
    static AsyncTaskCompleteListener<String> tela = null;
    private int mPosicaoTroca;
    private ActivityTrocasListAdapter mAdapter;
    private Usuario dadosUsuario;

    private List<Troca> mListaTroca;
    private List<Troca> mListaWebService;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View activitytrocas = inflater.inflate(R.layout.activitytrocas, container, false);
        trocaCRUD = new TrocaCRUD(container.getContext());
        tela = this;
        mListaTroca = new ArrayList<Troca>();
        mListaWebService = new ArrayList<Troca>();
        dadosUsuario = UsuarioUtil.obterUsuario(activitytrocas.getContext());
        return activitytrocas;
    }

    @Override
    public void onStart() {
        super.onStart();

        mListaTroca.clear();
        if (UsuarioUtil.trocasNaBase(getActivity().getApplicationContext())) {
            mListaTroca = trocaCRUD.listarTroca();
            mAdapter = new ActivityTrocasListAdapter(getActivity().getApplicationContext(), mListaTroca);
            setListAdapter(mAdapter);
        }else{
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.GET_TASK, getActivity().getApplicationContext(), "Buscando trocas", this, true);
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "TrocaWS?idUsuario"+String.valueOf(dadosUsuario.getId())});
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(view.getContext(), ActivityDetalheTroca.class);
                it.putExtra("idTroca", mListaTroca.get(position).getId());
                startActivity(it);
            }
        });

    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {

        if (!result.toString().equals("{}") || (!result.toString().equals(""))) {
            mListaWebService.clear();
            try {
                JSONArray jsonArray = result.getJSONArray("TrocaDTO");

                for (int i = 0; i < jsonArray.length(); i++) {
                    mListaWebService.add(parseTroca(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                mListaWebService.add(parseTroca(result.getJSONObject("TrocaDTO")));
            }

            TrocaCRUD trocaCRUD = new TrocaCRUD(getActivity().getApplicationContext());
            //trocaCRUD.deleteTrocas();

            for (int i = 0; i < mListaWebService.size(); i++){
                Troca troca = mListaWebService.get(i);
                if (trocaCRUD.atualizarStatusTroca(troca.getId(), troca.getStatusTroca()) == 0) {
                    trocaCRUD.inserirTroca(troca);
                }
            }

            mListaTroca = trocaCRUD.listarTroca();
            mAdapter = new ActivityTrocasListAdapter(getActivity().getApplicationContext(), mListaTroca);
            setListAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onTaskReturnNull(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), "Ocorreu um erro ao buscar os dados, tente novamente mais tarde!", Toast.LENGTH_LONG).show();
    }

    @Nullable
    private Troca parseTroca(JSONObject jsonObject){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Troca troca = new Troca();
            troca.setId(Integer.valueOf(jsonObject.getInt("id")));


            try {
                troca.setDataTroca(format.parse(jsonObject.getString("dataTroca")));
            }catch (ParseException e){
                e.printStackTrace();
            }


            switch(jsonObject.getString("statusTroca")){
                case "ANALISE":{
                    troca.setStatusTroca(StatusTroca.TROCA_ANALISE);
                    break;
                }

                case "ANDAMENTO":{
                    troca.setStatusTroca(StatusTroca.TROCA_ANDAMENTO);
                    break;
                }

                case "CANCELADA":{
                    troca.setStatusTroca(StatusTroca.TROCA_CANCELADA);
                    break;
                }

                case "CONCLUIDA":{
                    troca.setStatusTroca(StatusTroca.TROCA_CONCLUIDA);
                    break;
                }

                case "REJEITADA":{
                    troca.setStatusTroca(StatusTroca.TROCA_REJEITADA);
                    break;
                }
            }


            ItemJogoTroca itemJogoTroca = new ItemJogoTroca();

            itemJogoTroca.setNomeUsuarioTroca(jsonObject.getString("nomeUsuarioTroca"));
            itemJogoTroca.setNomeUsuarioOferta(jsonObject.getString("nomeUsuarioOferta"));
            itemJogoTroca.setIdUsuarioTroca(jsonObject.getInt("idUsuarioTroca"));
            itemJogoTroca.setIdUsuarioOferta(jsonObject.getInt("idUsuarioOferta"));

            definirJogoOferta(jsonObject, itemJogoTroca);
            definirJogoTroca(jsonObject, itemJogoTroca);

            troca.setJogosTroca(itemJogoTroca);

            return troca;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }


    private void definirJogoOferta(JSONObject jsonJogos, ItemJogoTroca itemJogoTroca){
        try {
            JSONObject jsonItemTroca = jsonJogos = jsonJogos.getJSONObject("itemTroca");
            JSONObject jsonJogoOferta = jsonItemTroca.getJSONObject("jogoOferta");

            itemJogoTroca.getJogoOferta().setId(jsonJogoOferta.getInt("id"));
            itemJogoTroca.getJogoOferta().setNomejogo(jsonJogoOferta.getString("nomejogo"));
            itemJogoTroca.getJogoOferta().setDescricao(jsonJogoOferta.getString("descricao"));
            itemJogoTroca.getJogoOferta().setCategoria(jsonJogoOferta.getInt("categoria"));

            JSONObject jsonJogoPlataforma = jsonItemTroca.getJSONObject("jogoPlataformaTroca");
            JSONObject jsonPlataforma = jsonJogoPlataforma.getJSONObject("plataforma");
            itemJogoTroca.getJogoOferta().setPlataforma(new Plataforma(jsonPlataforma.getInt("id")));

            itemJogoTroca.getJogoOferta().setIdJogoPlataforma(jsonJogoPlataforma.getInt("id"));
            itemJogoTroca.getJogoOferta().setImagem(jsonJogoOferta.getString("imagem"));
            itemJogoTroca.getJogoOferta().setAno(jsonJogoOferta.getInt("ano"));
        }catch (JSONException e){
            throw new RuntimeException("Erro ao definir jogo de oferta.");
        }
    }

    private void definirJogoTroca(JSONObject jsonJogos, ItemJogoTroca itemJogoTroca){
        try {
            JSONObject jsonItemTroca = jsonJogos = jsonJogos.getJSONObject("itemTroca");
            JSONObject jsonJogoTroca = jsonItemTroca.getJSONObject("jogoTroca");

            itemJogoTroca.getJogoTroca().setId(jsonJogoTroca.getInt("id"));
            itemJogoTroca.getJogoTroca().setNomejogo(jsonJogoTroca.getString("nomejogo"));
            itemJogoTroca.getJogoTroca().setDescricao(jsonJogoTroca.getString("descricao"));
            itemJogoTroca.getJogoTroca().setCategoria(jsonJogoTroca.getInt("categoria"));

            JSONObject jsonJogoPlataforma = jsonItemTroca.getJSONObject("jogoPlataformaTroca");
            JSONObject jsonPlataforma = jsonJogoPlataforma.getJSONObject("plataforma");
            itemJogoTroca.getJogoTroca().setPlataforma(new Plataforma(jsonPlataforma.getInt("id")));

            itemJogoTroca.getJogoOferta().setIdJogoPlataforma(jsonJogoPlataforma.getInt("id"));

            itemJogoTroca.getJogoTroca().setImagem(jsonJogoTroca.getString("imagem"));
            itemJogoTroca.getJogoTroca().setAno(jsonJogoTroca.getInt("ano"));
        }catch(JSONException e){
            throw new RuntimeException("Erro ao definir jogo de troca.");
        }
    }

    public void buscarDadosServidor(){
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, getActivity().getApplicationContext(), "Buscando trocas, aguarde", this, false);
        webServiceTask.addParameter("idUsuario", String.valueOf(dadosUsuario.getId()));
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "BuscarTrocas"});
    }

}
