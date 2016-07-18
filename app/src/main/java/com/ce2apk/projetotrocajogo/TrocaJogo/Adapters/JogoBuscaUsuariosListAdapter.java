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
import com.ce2apk.projetotrocajogo.Jogo.TempJogoBusca;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.ItensJogoTroca;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import org.w3c.dom.Text;

import java.util.List;

public class JogoBuscaUsuariosListAdapter extends BaseAdapter{

	private Context context;
	private List<TempJogoBusca> lista;

	public JogoBuscaUsuariosListAdapter(Context context, List<TempJogoBusca> lista) {
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
		TempJogoBusca itensJogoTroca = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listarjogos_buscamodel, null);

		if(itensJogoTroca.getImagem() != null){
			ImageView imagem = (ImageView) view.findViewById(R.id.list_image);
			imagem.setImageBitmap(ImagemUtil.getBitmapFromString(itensJogoTroca.getImagem()));
		}

		TextView nomeJogo = (TextView) view.findViewById(R.id.nomeJogo);
		nomeJogo.setText(itensJogoTroca.getNomejogo());

		TextView usuarioBusca = (TextView) view.findViewById(R.id.txtUsuarioBusca);
		usuarioBusca.setText(itensJogoTroca.getNomeUsuarioTroca());

		TextView plataformaJogo = (TextView) view.findViewById(R.id.plataformaJogo);
		plataformaJogo.setText(ParserArray.plataformaJogo(itensJogoTroca.getPlataforma()));

		TextView anoJogo = (TextView) view.findViewById(R.id.anoJogo);
		anoJogo.setText(String.valueOf(itensJogoTroca.getAno()));

        TextView categoriaJogo = (TextView) view.findViewById(R.id.categoriaJogo);
		categoriaJogo.setText(ParserArray.categoriaJogo(itensJogoTroca.getCategoria()));

		return view;
	}



}
