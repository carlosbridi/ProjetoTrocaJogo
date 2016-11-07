package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoUtil;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.TrocaJogo.Adapters.ActivityMeusJogosListAdapter;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 09/11/15.
 */

public class ActivityMeusJogos extends android.support.v4.app.ListFragment implements AsyncTaskCompleteListener, View.OnClickListener{

    private JogoCRUD jogoCRUD;
    static AsyncTaskCompleteListener<String> tela = null;
    private List<Jogo> listaJogo;
    private int mPosicaoJogo;
    private ActivityMeusJogosListAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View activitymeusjogos = inflater.inflate(R.layout.activitymeusjogos, container, false);
        jogoCRUD = new JogoCRUD(container.getContext());
        tela = this;
        listaJogo = new ArrayList<Jogo>();

        return activitymeusjogos;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerForContextMenu(getListView());
        if (UsuarioUtil.jogosNaColecao(getActivity().getApplicationContext())) {
            listaJogo.clear();
            listaJogo = jogoCRUD.listarJogo();
            mAdapter = new ActivityMeusJogosListAdapter(getActivity().getApplicationContext(), listaJogo, this);
            setListAdapter(mAdapter);
        }else {
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, getActivity().getApplicationContext(), "Buscando dados do usuário", this, false);
            webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(getActivity().getApplicationContext(), "dadosUsuario").getId()));
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "listarColecaoJogosUsuario"});
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getView().getContext(), ActivityDetalheJogo.class);
                it.putExtra("jogo", (Serializable) listaJogo.get(position));
                startActivity(it);
            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:{
                Toast.makeText(getActivity().getApplicationContext(), "tesste", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menusearch, menu);
    }

    public static int getJogoList(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent().getParent().getParent();
        return listView.getPositionForView(parentRow);
    }

    public void removerJogo(View view, View viewAux){
        jogoCRUD = new JogoCRUD(view.getContext());
        mPosicaoJogo = getJogoList(view);
        final Jogo jogo = listaJogo.get(mPosicaoJogo);
        if (jogoCRUD.removerJogoColecao(jogo) > 0){
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, viewAux.getContext(), "Removendo jogo", tela);
            webServiceTask.addParameter("idJogo", String.valueOf(jogo.getId()));
            webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(viewAux.getContext(), "dadosUsuario").getId()));
            webServiceTask.addParameter("idPlataforma", String.valueOf(jogo.getPlataforma()));

            webServiceTask.execute(new String[]{consts.SERVICE_URL + "RemoverJogoColecao"});

        }else{
            Toast.makeText(viewAux.getContext(), "Problemas ao remover", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View v) {
        final View viewAux = getView();
        final AlertDialog.Builder builder = new AlertDialog.Builder(viewAux.getContext());
        builder.setTitle("Excluir");
        builder.setMessage("Deseja remover o jogo da coleção?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removerJogo(v, viewAux);
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


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (UsuarioUtil.jogosNaColecao(getActivity().getApplicationContext())) {
            if (result.get("codResultado").equals("1")) {
                Toast.makeText(getView().getContext(), result.get("msgResultado").toString(), Toast.LENGTH_SHORT).show();
                listaJogo.remove(mPosicaoJogo);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            JogoCRUD jogoCRUD = new JogoCRUD(getActivity().getApplicationContext());
            jogoCRUD.removerJogos();
            try{
                try {
                    JSONArray jsonArray = result.getJSONArray("Jogo");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jogoCRUD.inserirJogoColecao(JogoUtil.parserJogo(jsonArray.getJSONObject(i)));
                    }
                }catch (JSONException e){
                    jogoCRUD.inserirJogoColecao(JogoUtil.parserJogo(result.getJSONObject("Jogo")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            listaJogo.clear();
            listaJogo = jogoCRUD.listarJogo();
            mAdapter = new ActivityMeusJogosListAdapter(getActivity().getApplicationContext(), listaJogo, this);
            setListAdapter(mAdapter);

            UsuarioUtil.carregouJogos(getActivity().getApplicationContext());
        }
    }

    @Override
    public void onTaskReturnNull(String msg) {
    }

}
