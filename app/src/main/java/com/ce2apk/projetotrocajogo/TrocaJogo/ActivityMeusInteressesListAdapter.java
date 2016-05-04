package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import java.util.List;

/**
 * Created by carlosbridi on 28/12/15.
 */
public class ActivityMeusInteressesListAdapter extends BaseAdapter  {

    private Context context;
    private List<Jogo> lista;
    private View.OnClickListener tela;

    public ActivityMeusInteressesListAdapter(Context context, List lista, View.OnClickListener tela){
        this.context = context;
        this.lista = lista;
        this.tela = tela;
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
        Jogo jogo = lista.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listameusinteresses_model, null);

        if (jogo.getImagem() != null) {
            ImageView imgJogo = (ImageView) view.findViewById(R.id.list_image);
            imgJogo.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));
        }

        TextView nomeJogo = (TextView) view.findViewById(R.id.nomeJogo);
        nomeJogo.setText(jogo.getNomejogo());

        TextView plataformaJogo = (TextView) view.findViewById(R.id.plataformaJogo);
        plataformaJogo.setText(ParserArray.plataformaJogo(jogo.getPlataforma()));

        TextView anoJogo = (TextView) view.findViewById(R.id.anoJogo);
        anoJogo.setText(String.valueOf(jogo.getAno()));

        TextView categoriaJogo = (TextView) view.findViewById(R.id.categoriaJogo);
        categoriaJogo.setText(ParserArray.categoriaJogo(jogo.getCategoria()));

        ImageView imageView = (ImageView) view.findViewById(R.id.imageInteressePrincipal);
        imageView.setOnClickListener(tela);

        return view;
    }
}
