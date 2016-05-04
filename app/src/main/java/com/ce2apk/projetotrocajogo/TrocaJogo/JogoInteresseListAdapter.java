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
import com.ce2apk.projetotrocajogo.Jogo.JogoDAO;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import java.util.List;

import cdflynn.android.library.crossview.CrossView;

public class JogoInteresseListAdapter extends BaseAdapter{

	private Context context;
	private List<Jogo> lista;

	public JogoInteresseListAdapter(Context context, List<Jogo> lista) {
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
		View view = inflater.inflate(R.layout.listarinteresses_model, null);

		ImageView imgJogo = (ImageView) view.findViewById(R.id.list_image);
		if (jogo.getImagem()!=null)
			imgJogo.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));

		TextView nomeJogo = (TextView) view.findViewById(R.id.nomeJogo);
		nomeJogo.setText(jogo.getNomejogo());

		TextView plataformaJogo = (TextView) view.findViewById(R.id.plataformaJogo);
		plataformaJogo.setText(ParserArray.plataformaJogo(jogo.getPlataforma()));

		TextView anoJogo = (TextView) view.findViewById(R.id.anoJogo);
		anoJogo.setText(String.valueOf(jogo.getAno()));

        TextView categoriaJogo = (TextView) view.findViewById(R.id.categoriaJogo);
		categoriaJogo.setText(ParserArray.categoriaJogo(jogo.getCategoria()));

		JogoDAO jogoDAO = new JogoDAO(view.getContext());

		if(jogoDAO.jogoNosInteresses(jogo.getId(), jogo.getPlataforma())){
			ImageView imageView = (ImageView) view.findViewById(R.id.imageInteresse);
			imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_interesse_marcado));
			imageView.setTag("1");
		}

		return view;
	}



}
