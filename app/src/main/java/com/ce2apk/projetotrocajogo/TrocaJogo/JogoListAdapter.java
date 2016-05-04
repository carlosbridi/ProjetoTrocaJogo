package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.JogoDAO;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Util.ParserArray;

import java.util.List;

import cdflynn.android.library.crossview.CrossView;

public class JogoListAdapter extends BaseAdapter{

	private Context context;
	private List<Jogo> lista;
	private ImageView imgAdicionarJogo;
    private ImageView imgAdicionarColecao;

	public JogoListAdapter(Context context, List<Jogo> lista) {
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
		View view = inflater.inflate(R.layout.listarjogos_model, null);

		if(jogo.getImagem() != null){
			ImageView imagem = (ImageView) view.findViewById(R.id.list_image);
			imagem.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem()));
		}

		TextView nomeJogo = (TextView) view.findViewById(R.id.nomeJogo);
		nomeJogo.setText(jogo.getNomejogo());

		TextView plataformaJogo = (TextView) view.findViewById(R.id.plataformaJogo);
		plataformaJogo.setText(ParserArray.plataformaJogo(jogo.getPlataforma()));

		TextView anoJogo = (TextView) view.findViewById(R.id.anoJogo);
		anoJogo.setText(String.valueOf(jogo.getAno()));

        TextView categoriaJogo = (TextView) view.findViewById(R.id.categoriaJogo);
		categoriaJogo.setText(ParserArray.categoriaJogo(jogo.getCategoria()));


        String packageName = view.getContext().getPackageName();


        imgAdicionarJogo = (ImageView) view.findViewById(R.id.imageJogoColecao);

		JogoDAO jogoDAO = new JogoDAO(view.getContext());
		if(jogoDAO.jogoNaColecao(jogo.getId(), jogo.getPlataforma())){
            imgAdicionarJogo.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_deletejogo));
            imgAdicionarJogo.invalidate();

			imgAdicionarJogo.setTag("1");
		}else{
            String fnm = "ic_adicionar_black";
            int imgId = view.getResources().getIdentifier(packageName+":drawable/"+fnm , null, null);

            imgAdicionarJogo.setImageBitmap(BitmapFactory.decodeResource(view.getResources(), imgId));
            imgAdicionarJogo.invalidate();
		}

        imgAdicionarColecao = (ImageView) view.findViewById(R.id.imageInteresse);
        if(jogoDAO.jogoNosInteresses(jogo.getId(), jogo.getPlataforma())){

            imgAdicionarColecao.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_interesse_marcado));
            imgAdicionarColecao.invalidate();

            imgAdicionarColecao.setTag("1");
		}else{
            String fnm = "ic_interesse";
            int imgId = view.getResources().getIdentifier(packageName+":drawable/"+fnm , null, null);

            imgAdicionarColecao.setImageBitmap(BitmapFactory.decodeResource(view.getResources(), imgId));
            imgAdicionarColecao.invalidate();
        }

		return view;
	}



}
