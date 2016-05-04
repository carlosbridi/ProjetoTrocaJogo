package com.ce2apk.projetotrocajogo.TableUtils;

/**
 * Created by carlosbridi on 19/12/15.
 */
public class DDLTables {

    public static final String ddlJogo = "CREATE TABLE JOGO ("+
                    "id INTEGER NOT NULL," +
                    "nomejogo TEXT NOT NULL," +
                    "descricao TEXT NOT NULL," +
                    "categoria INTEGER NOT NULL," +
                    "plataforma integer not null,"+
                    "ano INTEGER NOT NULL," +
                    "imagem TEXT)" ;

    public static final String ddltemp_Jogo = "CREATE TABLE TEMP_JOGO ("+
            "id INTEGER NOT NULL," +
            "nomejogo TEXT NOT NULL," +
            "descricao TEXT NOT NULL," +
            "categoria INTEGER NOT NULL," +
            "plataforma integer not null,"+
            "ano INTEGER NOT NULL," +
            "imagem TEXT)" ;

    public static final String ddltemp_jogoInteresse = "CREATE TABLE TEMP_JOGOINTERESSE ("+
            "id INTEGER NOT NULL," +
            "nomejogo TEXT NOT NULL," +
            "descricao TEXT NOT NULL," +
            "categoria INTEGER NOT NULL," +
            "plataforma integer not null,"+
            "ano INTEGER NOT NULL," +
            "imagem TEXT)" ;

    public static final String ddlInteresse = "CREATE TABLE INTERESSE ("+
            "id INTEGER NOT NULL," +
            "nomejogo TEXT NOT NULL," +
            "descricao TEXT NOT NULL," +
            "categoria INTEGER NOT NULL," +
            "plataforma integer not null,"+
            "ano INTEGER NOT NULL," +
            "imagem TEXT)" ;


    public static final String ddltemp_jogoBuscaUsuarios = "CREATE TABLE TEMP_JOGOBUSCAUSUARIOS ("+
            "id INTEGER NOT NULL," +
            "idjogotroca INTEGER NOT NULL, "+
            "idusuariotroca INTEGER NOT NULL, "+
            "nomeusuario TEXT NOT NULL, "+
            "nomejogo TEXT NOT NULL," +
            "descricao TEXT NOT NULL," +
            "categoria INTEGER NOT NULL," +
            "plataforma integer not null,"+
            "ano INTEGER NOT NULL," +
            "imagem TEXT)" ;

    public static final String ddlCabecalhoTroca = "CREATE TABLE TROCA ("+
            "idtroca INTEGER NOT NULL, "+
            "datatroca date not null, "+
            "status TEXT)";

    public static final String ddlItensTroca = "CREATE TABLE ITEMTROCA ("+
            "iditemtroca INTEGER NOT NULL, "+
            "idtroca INTEGER NOT NULL, "+
            "idusuariooferta INTEGER NOT NULL, "+
            "idusuariotroca INTEGER NOT NULL, "+
            "idjogooferta INTEGER NOT NULL, "+
            "idjogotroca INTEGER NOT NULL, "+
            "nomeusuariooferta TEXT NOT NULL, "+
            "nomeusuariotroca TEXT NOT NULL, "+
            "nomejogooferta TEXT NOT NULL," +
            "nomejogotroca TEXT NOT NULL," +
            "descricaooferta TEXT NOT NULL," +
            "descricaotroca TEXT NOT NULL," +
            "categoriaoferta INTEGER NOT NULL," +
            "categoriatroca INTEGER NOT NULL," +
            "plataformaOferta integer not null,"+
            "plataformaTroca integer not null,"+
            "anooferta INTEGER NOT NULL," +
            "anotroca INTEGER NOT NULL," +
            "imagemoferta TEXT,"+
            "imagemtroca TEXT)";
}
