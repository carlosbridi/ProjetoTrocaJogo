package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.R;

import java.util.List;

/**
 * Created by carlosbridi on 05/03/16.
 */
public class ActivityListarColecao extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymeusjogos);



        JogoCRUD jogoCRUD = new JogoCRUD(this);
        final List<Jogo> listaJogo = jogoCRUD.listarJogo();

        JogosColecaoAdapter adapter = new JogosColecaoAdapter(this, listaJogo);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent();
                it.putExtra("idJogo", listaJogo.get(position).getId());
                setResult(Activity.RESULT_OK, it);
                finish();
            }
        });
    }
}
