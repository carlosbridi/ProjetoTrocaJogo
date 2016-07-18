package com.ce2apk.projetotrocajogo.TrocaJogo.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.StatusTroca;
import com.ce2apk.projetotrocajogo.Troca.Troca;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import java.util.List;

/**
 * Created by carlosbridi on 28/12/15.
 */
public class ActivityTrocasListAdapter extends BaseAdapter  {

    private Context context;
    private List<Troca> lista;


    public ActivityTrocasListAdapter(Context context, List lista){
        this.context = context;
        this.lista = lista;

    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Troca troca = lista.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listatrocas_model, null);


        TextView txtStatusTroca = (TextView) view.findViewById(R.id.statustroca);

        switch (troca.getStatusTroca().toString()){
            case "A":{
                txtStatusTroca.setText("Troca em Analise");
                txtStatusTroca.setTextColor(Color.BLUE);
                break;
            }

            case "N":{
                txtStatusTroca.setText("Troca em Andamento");
                txtStatusTroca.setTextColor(Color.BLUE);
                break;
            }

            case "C":{
                txtStatusTroca.setText("Troca Concluída");
                txtStatusTroca.setTextColor(Color.GREEN);
                break;
            }
            case "R":{
                txtStatusTroca.setText("Troca Rejeitada");
                txtStatusTroca.setTextColor(Color.RED);
                break;
            }

            case "D":{
                txtStatusTroca.setText("Troca Cancelada");
                txtStatusTroca.setTextColor(Color.RED);
                break;
            }
        }

        if (troca.getJogosTroca().getJogoTroca().getImagem() != null) {
            ImageView imgViewJogoTroca = (ImageView) view.findViewById(R.id.imgJogoTroca);
            imgViewJogoTroca.setImageBitmap(ImagemUtil.getBitmapFromString(troca.getJogosTroca().getJogoTroca().getImagem()));
        }


        if (troca.getJogosTroca().getJogoOferta().getImagem() != null) {
            ImageView imgViewJogoOferta = (ImageView) view.findViewById(R.id.imgJogoOferta);
            imgViewJogoOferta.setImageBitmap(ImagemUtil.getBitmapFromString(troca.getJogosTroca().getJogoOferta().getImagem()));
        }

        TextView txtNomeJogoOferta = (TextView) view.findViewById(R.id.nomeJogoOferta);
        txtNomeJogoOferta.setText(troca.getJogosTroca().getJogoOferta().getNomejogo());

        TextView txtNomeJogoTroca = (TextView) view.findViewById(R.id.nomeJogoTroca);
        txtNomeJogoTroca.setText(troca.getJogosTroca().getJogoTroca().getNomejogo());

        ImageView imgView1 = (ImageView) view.findViewById(R.id.imageView);
        ImageView imgView2 = (ImageView) view.findViewById(R.id.imageView2);


        if (UsuarioUtil.obterUsuario(view.getContext(), "dadosUsuario").getId() == troca.getJogosTroca().getIdUsuarioOferta()) {
            imgView1.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_jogorecebido));
            imgView2.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_jogoenviado));
        }else{
            imgView1.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_jogoenviado));
            imgView2.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_jogorecebido));
        }


        return view;
    }
}
