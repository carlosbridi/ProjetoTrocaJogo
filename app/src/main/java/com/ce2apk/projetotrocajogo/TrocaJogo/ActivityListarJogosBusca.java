package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ce2apk.projetotrocajogo.Jogo.JogoBuscaUsuario;
import com.ce2apk.projetotrocajogo.Jogo.TempJogoBuscaCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.TrocaJogo.Adapters.JogoBuscaUsuariosListAdapter;

import java.util.List;


/**
 * Created by carlosbridi on 17/12/15.
 */
public class ActivityListarJogosBusca extends ListActivity {


    private List<JogoBuscaUsuario> mListaJogos;
    private int mAdicionarJogoColecao = 0; // 0 = Adicionando / 1 = Removendo
    private int mWebServiceRequest = 0; // 0 = Adicionando / 1 = Removendo
    private JogoBuscaUsuariosListAdapter mAdapter;
    private ImageView imageViewJogoColecao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultadopesquisajogos);

        registerForContextMenu(getListView());

        ActionBar bar = getActionBar();
        if(bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TempJogoBuscaCRUD temp_jogoCRUD = new TempJogoBuscaCRUD(getApplicationContext());

        mListaJogos = temp_jogoCRUD.listarJogo();
        mAdapter = new JogoBuscaUsuariosListAdapter(this, mListaJogos);
        setListAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent();
                it.putExtra("idCodJogo", mListaJogos.get(position).getId());
                setResult(RESULT_OK, it);
                finish();
            }
        });

    }

    public int getJogoList(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent().getParent().getParent();
        return listView.getPositionForView(parentRow);
    }

}
