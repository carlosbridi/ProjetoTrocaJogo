package com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;

/**
 * Created by carlosbridi on 19/01/16.
 */
public class ItemJogoTrocaCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;

    public ItemJogoTrocaCRUD(Context context){
        banco = new PersistenceHelper(context);
    }

    public long inserirJogosTroca(int idTroca, ItemJogoTroca itemJogoTroca){

        ContentValues contentValues;
        long resultado = 0;

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            contentValues = new ContentValues();

            contentValues.put("idtroca", idTroca);
            contentValues.put("iditemtroca", 1);
            contentValues.put("idusuariooferta", itemJogoTroca.getIdUsuarioOferta());
            contentValues.put("idusuariotroca", itemJogoTroca.getIdUsuarioTroca());
            contentValues.put("nomeusuariotroca", itemJogoTroca.getNomeUsuarioTroca());
            contentValues.put("nomeusuariooferta", itemJogoTroca.getNomeUsuarioOferta());
            contentValues.put("idjogooferta", itemJogoTroca.getJogoOferta().getId());
            contentValues.put("idjogotroca", itemJogoTroca.getJogoTroca().getId());
            contentValues.put("nomejogooferta", itemJogoTroca.getJogoOferta().getNomejogo());
            contentValues.put("nomejogotroca", itemJogoTroca.getJogoTroca().getNomejogo());
            contentValues.put("descricaooferta", itemJogoTroca.getJogoOferta().getDescricao());
            contentValues.put("descricaotroca", itemJogoTroca.getJogoTroca().getDescricao());
            contentValues.put("categoriaoferta", itemJogoTroca.getJogoOferta().getCategoria());
            contentValues.put("categoriatroca", itemJogoTroca.getJogoTroca().getCategoria());
            contentValues.put("plataformaoferta", itemJogoTroca.getJogoOferta().getPlataforma().getId());
            contentValues.put("plataformatroca", itemJogoTroca.getJogoTroca().getPlataforma().getId());
            contentValues.put("anotroca", itemJogoTroca.getJogoTroca().getAno());
            contentValues.put("anooferta", itemJogoTroca.getJogoOferta().getAno());
            contentValues.put("imagemoferta", itemJogoTroca.getJogoOferta().getImagem());
            contentValues.put("imagemtroca", itemJogoTroca.getJogoTroca().getImagem());

            resultado = db.insert("itemtroca", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();

        return resultado;
    }

    public boolean itemJogoTrocaEmTroca(int idJogo, int idPlataforma){

        String sql = "SELECT 1 FROM ITEMTROCA WHERE IDJOGOTROCA = " + idJogo + " AND PLATAFORMAJOGOTROCA = " + idPlataforma;

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            return cursor.moveToNext();

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean itemJogoTrocaEmOferta(int idJogo, int plataforma){

        String sql = "SELECT 1 "+
                       "FROM ITEMTROCA ITEMTROCA"+
                 "INNER JOIN TROCA ON TROCA.IDTROCA = ITEMTROCA.IDTROCA "+
                      "WHERE IDJOGOOFERTA = " + idJogo + " AND "+
                            "PLATAFORMAJOGOOFERTA = " + plataforma;


        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            return cursor.moveToNext();

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void deleteAllItensTroca(){
        String where = "1=1";
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            db.delete("itemtroca", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();
    }


}
