package com.ce2apk.projetotrocajogo.Jogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Util.ListaJogos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 19/12/15.
 */
public class JogoCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;

    public JogoCRUD(Context context){
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
            contentValues.put("idjogoplataforma", jogo.getIdJogoPlataforma());
            contentValues.put("ano", jogo.getAno());
            contentValues.put("imagem", jogo.getImagem());
            contentValues.put("interesse", jogo.isInteresse());
            resultado = db.insert("jogousuario", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();

        return resultado;
    }

    public void removerJogos(){
        String where = "1=1 and interesse = 0 ";
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            db.delete("jogousuario", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();
    }


    public long removerJogoColecao(int idJogo, int idPlataforma){
        long resultado;

        String where = "interesse = 0 and id = "+ idJogo + " and plataforma = "+idPlataforma;
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            resultado = db.delete("jogousuario", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();
        return resultado;
    }

    public long removerJogoColecao(Jogo jogo){
        long resultado;

        String where = "interesse = 0 and id = "+ jogo.getId()+ " and plataforma = "+jogo.getPlataforma().getId()+ " and categoria = " + jogo.getCategoria();
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            resultado = db.delete("jogousuario", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();
        return resultado;
    }

    public List<Jogo> listarJogo() {
        String sql = "select * from jogousuario where interesse = 0 ";

        List<Jogo> listaJogos = new ArrayList<Jogo>();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()){
                Jogo jogo = new Jogo();
                jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                listaJogos.add(jogo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  listaJogos;
    }

    public Jogo obterDadosJogo(int idJogo){

        String sql = "select * from jogousuario where id = " + idJogo + " and interesse = 0 ";
        Jogo jogo = new Jogo();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()){
                jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return jogo;

    }

}