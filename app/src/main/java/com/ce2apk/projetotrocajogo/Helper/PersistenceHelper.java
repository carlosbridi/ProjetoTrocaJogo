package com.ce2apk.projetotrocajogo.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ce2apk.projetotrocajogo.TableUtils.DDLTables;

/**
 * Created by carlosbridi on 10/11/15.
 */
public class PersistenceHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO =  "TrocaJogo.db";
    public static final int VERSAO = 1;

    private static PersistenceHelper instance;

    public PersistenceHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        try{
            context.openOrCreateDatabase(NOME_BANCO, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        }catch (Exception nullpoinException){
            nullpoinException.printStackTrace();
        }
    }

    public static PersistenceHelper getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DDLTables.ddlJogo);
        db.execSQL(DDLTables.ddltemp_Jogo);
        db.execSQL(DDLTables.ddlInteresse);
        db.execSQL(DDLTables.ddltemp_jogoInteresse);
        db.execSQL(DDLTables.ddltemp_jogoBuscaUsuarios);
        db.execSQL(DDLTables.ddlCabecalhoTroca);
        db.execSQL(DDLTables.ddlItensTroca);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
