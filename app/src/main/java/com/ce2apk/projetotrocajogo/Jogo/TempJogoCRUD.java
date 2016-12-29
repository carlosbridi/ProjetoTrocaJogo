package com.ce2apk.projetotrocajogo.Jogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Util.ListaJogos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 19/12/15.
 */
public class TempJogoCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;

    public TempJogoCRUD(Context context){
        banco = new PersistenceHelper(context);
    }


    public long inserirJogosColecao(List<Jogo> jogos){
        for (Jogo xJogo : jogos) {
            inserirJogoColecao(xJogo);
        }
        return 0;
    }

    public long inserirJogoColecao(Jogo jogo){
        ContentValues contentValues;
        long resultado;

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            contentValues = new ContentValues();
            contentValues.put("id", jogo.getId());
            contentValues.put("nomejogo", jogo.getNomejogo());
            contentValues.put("descricao", jogo.getDescricao());
            contentValues.put("categoria", jogo.getCategoria());
            contentValues.put("plataforma", jogo.getPlataforma().getId());
            contentValues.put("ano", jogo.getAno());
            contentValues.put("imagem", jogo.getImagem());
            contentValues.put("interesse", jogo.isInteresse());
            contentValues.put("idjogoplataforma", jogo.getIdJogoPlataforma());
            resultado = db.insert("temp_jogo", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
        db.close();

        return resultado;
    }

    public long removerTodaListaTemp(){
        long resultado;

        String where = "id > 0";

        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {
            resultado = db.delete("temp_jogo", where, null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
        db.close();

        return resultado;
    }

    public ListaJogos listarJogo() {
        String sql = "select * from temp_jogo where interesse = 0";

        ListaJogos listaJogos = new ListaJogos();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()){
                Jogo jogo = new Jogo();
                jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                jogo.setInteresse(false);
                listaJogos.addJogo(jogo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  listaJogos;
    }

    public ListaJogos listarJogoInteresse() {
        String sql = "select * from temp_jogo where interesse = 1";

        ListaJogos listaJogos = new ListaJogos();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()){
                Jogo jogo = new Jogo();
                jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                jogo.setInteresse(true);
                listaJogos.addJogo(jogo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  listaJogos;
    }
}