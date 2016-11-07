package com.ce2apk.projetotrocajogo.TrocaJogo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoDAO;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import java.util.List;

public class JogosColecaoAdapter extends BaseAdapter{

	private Context context;
	private List<Jogo> lista;

	public JogosColecaoAdapter(Context context, List<Jogo> lista) {
		this.context = context;
		this.lista = lista;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(int position) {
		return lista.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Jogo jogo = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listarcolecaojogos_model, null);

		if(jogo.getImagem() != null){
			ImageView imagem = (ImageView) view.findViewById(R.id.list_image);
			imagem.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));
		}

		TextView nomeJogo = (TextView) view.findViewById(R.id.nomeJogo);
		nomeJogo.setText(jogo.getNomejogo());

		TextView plataformaJogo = (TextView) view.findViewById(R.id.plataformaJogo);
		plataformaJogo.setText(ParserArray.plataformaJogo(jogo.getPlataforma().getId()));

		TextView anoJogo = (TextView) view.findViewById(R.id.anoJogo);
		anoJogo.setText(String.valueOf(jogo.getAno()));

        TextView categoriaJogo = (TextView) view.findViewById(R.id.categoriaJogo);
		categoriaJogo.setText(ParserArray.categoriaJogo(jogo.getCategoria()));

		return view;
	}



}
