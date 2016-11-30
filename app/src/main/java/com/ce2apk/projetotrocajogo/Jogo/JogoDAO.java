package com.ce2apk.projetotrocajogo.Jogo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;

import java.util.List;

/**
 * Created by carlosbridi on 28/12/15.
 */
public class JogoDAO {

    private SQLiteDatabase db;
    private PersistenceHelper banco;


    public JogoDAO(Context context){
        banco = new PersistenceHelper(context);
    }

    public boolean jogoNaColecao(int codJogo, int codPlataforma){
        db = banco.getWritableDatabase();

        String sql = "select 1 from jogousuario where id = "+codJogo+" and plataforma = "+codPlataforma;

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.moveToNext();
    }

    public boolean jogoNosInteresses(int codJogo, int codPlataforma){
        db = banco.getWritableDatabase();

        String sql = "select 1 from jogousuariointeresse where id = "+codJogo+" and plataforma = "+codPlataforma;

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.moveToNext();
    }


}
