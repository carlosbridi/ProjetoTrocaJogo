package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoUtil;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by carlosbridi on 09/11/15.
 */
public class ActivityInteresses extends android.support.v4.app.ListFragment implements AsyncTaskCompleteListener, View.OnClickListener {

    private JogoInteresseCRUD jogoInteresseCRUD;
    private AsyncTaskCompleteListener<String> tela;
    private List<Jogo> listaInteresse;
    private int mPosicaoJogo;
    private ActivityMeusInteressesListAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View activityInteresses = inflater.inflate(R.layout.activityinteresses, container, false);

        jogoInteresseCRUD = new JogoInteresseCRUD(container.getContext());
        tela = this;

        return activityInteresses;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (UsuarioUtil.interessesNaColecao(getActivity().getApplicationContext())) {
            listaInteresse = jogoInteresseCRUD.listarInteresse();
            mAdapter = new ActivityMeusInteressesListAdapter(getActivity().getApplicationContext(), listaInteresse, this);
            setListAdapter(mAdapter);
        }else{
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, getActivity().getApplicationContext(), "Buscando dados do usuário", this, true);
            webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(getActivity().getApplicationContext(), "dadosUsuario").getId()));
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "listarInteressesJogosUsuario"});
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getView().getContext(), ActivityDetalheJogo.class);
                it.putExtra("jogo", (Serializable) listaInteresse.get(position));
                startActivity(it);
            }
        });
    }

    public static int getJogoInteresseList(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent().getParent().getParent();
        return listView.getPositionForView(parentRow);
    }

    public void removerInteresse(View view, View viewAux){
        jogoInteresseCRUD = new JogoInteresseCRUD(view.getContext());
        mPosicaoJogo = getJogoInteresseList(view);
        final Jogo jogo = listaInteresse.get(mPosicaoJogo);
        if (jogoInteresseCRUD.removerInteresse(jogo) > 0){
            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, viewAux.getContext(), "Removendo jogo", tela);
            webServiceTask.addParameter("idJogo", String.valueOf(jogo.getId()));
            webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(viewAux.getContext(), "dadosUsuario").getId()));
            webServiceTask.addParameter("idPlataforma", String.valueOf(jogo.getPlataforma()));

            webServiceTask.execute(new String[]{consts.SERVICE_URL + "RemoverJogoInteresse"});

        }else{
            Toast.makeText(viewAux.getContext(), "Problemas ao remover", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View v) {
        final View viewAux = getView();
        final AlertDialog.Builder builder = new AlertDialog.Builder(viewAux.getContext());
        builder.setTitle("Excluir");
        builder.setMessage("Deseja remover o jogo dos interesses?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removerInteresse(v, viewAux);
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
        if(UsuarioUtil.interessesNaColecao(getActivity().getApplicationContext())) {
            if (result.get("codResultado").equals("1")) {
                listaInteresse.remove(mPosicaoJogo);
                mAdapter.notifyDataSetChanged();
            }
        }else{
            JogoInteresseCRUD jogoCRUD = new JogoInteresseCRUD(getActivity().getApplicationContext());
            try{
                try {
                    JSONArray jsonArray = result.getJSONArray("Jogo");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jogoCRUD.inserirJogoInteresse(JogoUtil.pareserJogo(jsonArray.getJSONObject(i)));
                    }
                }catch (JSONException e){
                    jogoCRUD.inserirJogoInteresse(JogoUtil.pareserJogo(result.getJSONObject("Jogo")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            listaInteresse = jogoCRUD.listarInteresse();
            mAdapter = new ActivityMeusInteressesListAdapter(getActivity().getApplicationContext(), listaInteresse, this);
            setListAdapter(mAdapter);

            UsuarioUtil.carregouInteresses(getActivity().getApplicationContext());
        }
    }

    @Override
    public void onTaskReturnNull(String msg) {
        //do anything
    }

}
