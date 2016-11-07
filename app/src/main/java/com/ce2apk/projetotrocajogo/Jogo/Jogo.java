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
	private String imagem;
	private boolean interesse;
	
	public Jogo(){
		
	}

	public Jogo(int id, String nomejogo, String descricao, int categoria,
			int ano, Plataforma plataforma, String imagem) {
		super();
		this.id = id;
		this.nomejogo = nomejogo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.ano = ano;
		this.plataforma = plataforma;
		this.imagem = imagem;
		this.interesse = false;
	}


	public Jogo(int id, String nomejogo, String descricao, int categoria, int ano, Plataforma plataforma, String imagem, boolean interesse) {
		super();
		this.id = id;
		this.nomejogo = nomejogo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.ano = ano;
		this.plataforma = plataforma;
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
		setImagem(source.readString());
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomejogo() {
		return nomejogo;
	}
	public void setNomejogo(String nomejogo) {
		this.nomejogo = nomejogo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getCategoria() {
		return categoria;
	}
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public Plataforma getPlataforma() {
		return plataforma;
	}
	public void setPlataforma(Plataforma plataforma) {
		this.plataforma = plataforma;
	}
	public String getImagem() {return imagem;}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public boolean isInteresse() {
		return interesse;
	}

	public void setInteresse(boolean interesse) {
		this.interesse = interesse;
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
