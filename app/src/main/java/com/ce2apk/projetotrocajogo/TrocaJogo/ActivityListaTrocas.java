package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItensJogoTroca;
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
        dadosUsuario = UsuarioUtil.obterUsuario(activitytrocas.getContext(), "dadosUsuario");
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
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, getActivity().getApplicationContext(), "Buscando trocas", this, true);
            webServiceTask.addParameter("idUsuario", String.valueOf(dadosUsuario.getId()));
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "BuscarTrocas"});
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
                JSONArray jsonArray = result.getJSONArray("Troca");

                for (int i = 0; i < jsonArray.length(); i++) {
                    mListaWebService.add(parseTroca(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                mListaWebService.add(parseTroca(result.getJSONObject("Troca")));
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
                case "TROCA_ANALISE":{
                    troca.setStatusTroca(StatusTroca.TROCA_ANALISE);
                    break;
                }

                case "TROCA_ANDAMENTO":{
                    troca.setStatusTroca(StatusTroca.TROCA_ANDAMENTO);
                    break;
                }

                case "TROCA_CANCELADA":{
                    troca.setStatusTroca(StatusTroca.TROCA_CANCELADA);
                    break;
                }

                case "TROCA_CONCLUIDA":{
                    troca.setStatusTroca(StatusTroca.TROCA_CONCLUIDA);
                    break;
                }

                case "TROCA_REJEITADA":{
                    troca.setStatusTroca(StatusTroca.TROCA_REJEITADA);
                    break;
                }
            }

            JSONObject jsonJogos =  jsonObject.getJSONObject("jogoTroca");

            ItensJogoTroca itensJogoTroca = new ItensJogoTroca();

            itensJogoTroca.setNomeUsuarioTroca(jsonJogos.getString("nomeUsuarioTroca"));
            itensJogoTroca.setNomeUsuarioOferta(jsonJogos.getString("nomeUsuarioOferta"));
            itensJogoTroca.setIdUsuarioTroca(jsonJogos.getInt("idUsuarioTroca"));
            itensJogoTroca.setIdUsuarioOferta(jsonJogos.getInt("idUsuarioOferta"));

            jsonJogos = jsonObject.getJSONObject("jogoTroca").getJSONObject("jogoOferta");
            itensJogoTroca.getJogoOferta().setId(jsonJogos.getInt("id"));
            itensJogoTroca.getJogoOferta().setNomejogo(jsonJogos.getString("nomejogo"));
            itensJogoTroca.getJogoOferta().setDescricao(jsonJogos.getString("descricao"));
            itensJogoTroca.getJogoOferta().setCategoria(jsonJogos.getInt("categoria"));
            itensJogoTroca.getJogoOferta().setPlataforma(new Plataforma(jsonJogos.getInt("plataforma")));
            itensJogoTroca.getJogoOferta().setImagem(jsonJogos.getString("imagem"));
            itensJogoTroca.getJogoOferta().setAno(jsonJogos.getInt("ano"));

            jsonJogos = jsonObject.getJSONObject("jogoTroca").getJSONObject("jogoTroca");
            itensJogoTroca.getJogoTroca().setId(jsonJogos.getInt("id"));
            itensJogoTroca.getJogoTroca().setNomejogo(jsonJogos.getString("nomejogo"));
            itensJogoTroca.getJogoTroca().setDescricao(jsonJogos.getString("descricao"));
            itensJogoTroca.getJogoTroca().setCategoria(jsonJogos.getInt("categoria"));
            itensJogoTroca.getJogoTroca().setPlataforma(new Plataforma(jsonJogos.getInt("plataforma")));
            itensJogoTroca.getJogoTroca().setImagem(jsonJogos.getString("imagem"));
            itensJogoTroca.getJogoTroca().setAno(jsonJogos.getInt("ano"));
            
            troca.setJogosTroca(itensJogoTroca);

            return troca;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    public void buscarDadosServidor(){
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, getActivity().getApplicationContext(), "Buscando trocas, aguarde", this, false);
        webServiceTask.addParameter("idUsuario", String.valueOf(dadosUsuario.getId()));
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "BuscarTrocas"});
    }

}
