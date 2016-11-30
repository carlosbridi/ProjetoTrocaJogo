package com.ce2apk.projetotrocajogo.Jogo;


import android.os.Parcel;
import android.os.Parcelable;

import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;

import java.io.Serializable;

public class Jogo implements Serializable, Parcelable {

	private int id;
	private String nomejogo;
	private String descricao;
	private int categoria;
	private int ano;
	private Plataforma plataforma;
	private int idJogoPlataforma;
	private String imagem;
	private boolean interesse;
	
	public Jogo(){
		
	}

	public Jogo(int id, String nomejogo, String descricao, int categoria,
				int ano, Plataforma plataforma, int idJogoPlataforma, String imagem) {
		super();
		this.id = id;
		this.nomejogo = nomejogo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.ano = ano;
		this.plataforma = plataforma;
		this.idJogoPlataforma = idJogoPlataforma;
		this.imagem = imagem;
		this.interesse = false;
	}


	public Jogo(int id, String nomejogo, String descricao, int categoria, int ano, Plataforma plataforma, int idJogoPlataforma, String imagem, boolean interesse) {
		super();
		this.id = id;
		this.nomejogo = nomejogo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.ano = ano;
		this.plataforma = plataforma;
		this.idJogoPlataforma = idJogoPlataforma;
		this.imagem = imagem;
		this.interesse = interesse;
	}

	public Jogo(Parcel source) {
		setId(source.readInt());
		setNomejogo(source.readString());
		setDescricao(source.readString());
		setCategoria(source.readInt());
		setAno(source.readInt());
		setPlataforma(new Plataforma(source.readInt()));
		setIdJogoPlataforma(source.readInt());
		setImagem(source.readString());
	}

	public int getId() {
		return id;
	}
	public Jogo setId(int id) {
		this.id = id;
		return this;
	}
	public String getNomejogo() {
		return nomejogo;
	}
	public Jogo setNomejogo(String nomejogo) {
		this.nomejogo = nomejogo;
		return this;
	}
	public String getDescricao() {
		return descricao;
	}
	public Jogo setDescricao(String descricao) {
		this.descricao = descricao;
		return this;
	}
	public int getCategoria() {
		return categoria;
	}
	public Jogo setCategoria(int categoria) {
		this.categoria = categoria;
		return this;
	}
	public int getAno() {
		return ano;
	}
	public Jogo setAno(int ano) {
		this.ano = ano;
		return this;
	}
	public Plataforma getPlataforma() {
		return plataforma;
	}
	public Jogo setPlataforma(Plataforma plataforma) {
		this.plataforma = plataforma;
		return this;
	}
	public String getImagem() {return imagem;}
	public Jogo setImagem(String imagem) {
		this.imagem = imagem;
		return this;
	}

	public boolean isInteresse() {
		return interesse;
	}

	public Jogo setInteresse(boolean interesse) {
		this.interesse = interesse;
		return this;
	}

	public int getIdJogoPlataforma() {
		return idJogoPlataforma;
	}

	public Jogo setIdJogoPlataforma(int idJogoPlataforma) {
		this.idJogoPlataforma = idJogoPlataforma;
		return this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getNomejogo());
		dest.writeString(getDescricao());
		dest.writeInt(getCategoria());
		dest.writeInt(getAno());
		dest.writeInt(getPlataforma().getId());
		dest.writeInt(getIdJogoPlataforma());
		dest.writeString(getImagem());
	}

	public static final Parcelable.Creator<Jogo> CREATOR = new Parcelable.Creator<Jogo>(){
		@Override
		public Jogo createFromParcel(Parcel source) {
			return new Jogo(source);
		}
		@Override
		public Jogo[] newArray(int size) {
			return new Jogo[size];
		}
	};

}
