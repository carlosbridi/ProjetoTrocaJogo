package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.Jogo.TempJogoBusca;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoBuscaCRUD;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItensJogoTroca;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.ListaJogos;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


/**
 * Created by carlosbridi on 17/12/15.
 */
public class ActivityListarJogosBusca extends ListActivity {


    private List<TempJogoBusca> mListaJogos;
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

        Temp_JogoBuscaCRUD temp_jogoCRUD = new Temp_JogoBuscaCRUD(getApplicationContext());

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
