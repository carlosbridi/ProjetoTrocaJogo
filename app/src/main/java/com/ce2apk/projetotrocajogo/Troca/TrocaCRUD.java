package com.ce2apk.projetotrocajogo.Troca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTroca;
import com.ce2apk.projetotrocajogo.Troca.ItemJogoTroca.ItemJogoTrocaCRUD;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.TrocaJogoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 10/03/16.
 */
public class TrocaCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;
    private Context context;

    public TrocaCRUD(Context context){
        banco = new PersistenceHelper(context);
        this.context = context;
    }

    public long inserirTroca(List<Troca> listaTroca){
        long i = 0;

        for (Troca troca : listaTroca){
             i += inserirTroca(troca);
        }

        if (listaTroca.size() == i)
            return 1;
        else
            return 0;
    }

    public long inserirTroca(Troca troca){
        ContentValues contentValues;
        long resultado;


        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            contentValues = new ContentValues();
            contentValues.put("idtroca", troca.getId()); //Conseguir um ID
            contentValues.put("datatroca", TrocaJogoUtil.getDateTime(troca.getDataTroca()));
            contentValues.put("status", troca.getStatusTroca().toString());
            resultado = db.insert("troca", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();

        try{
            ItemJogoTrocaCRUD itemJogoTrocaCRUD = new ItemJogoTrocaCRUD(context);
            itemJogoTrocaCRUD.inserirJogosTroca(troca.getId(), troca.getJogosTroca());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        return resultado;
    }

    public List<Troca> listarTroca(){

        String sql = "SELECT troca.idtroca, "+
                "troca.datatroca, "+
                "troca.status," +
                " itemtroca.iditemtroca, "+
                "itemtroca.idtroca, "+
                "itemtroca.idusuariooferta, "+
                "itemtroca.idusuariotroca, "+
                "itemtroca.idjogooferta, "+
                "itemtroca.idjogotroca, "+
                "itemtroca.nomeusuariooferta, "+
                "itemtroca.nomeusuariotroca, "+
                "itemtroca.nomejogooferta, "+
                "itemtroca.nomejogotroca, "+
                "itemtroca.descricaooferta, "+
                "itemtroca.descricaotroca, "+
                "itemtroca.categoriaoferta, "+
                "itemtroca.categoriatroca, "+
                "itemtroca.plataformaoferta, "+
                "itemtroca.plataformatroca, "+
                "itemtroca.anooferta, "+
                "itemtroca.anotroca, "+
                "itemtroca.imagemoferta, "+
                "itemtroca.imagemtroca "+
                "FROM troca "+
                "INNER JOIN itemtroca itemtroca ON troca.idtroca = itemtroca.idtroca ";

        List<Troca> listaTrocas = new ArrayList<Troca>();
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            Troca troca = new Troca();
            troca = parseTroca(troca, cursor);
            listaTrocas.add(troca);
        }

        return  listaTrocas;
    }




    public Troca buscarTroca(int idTroca) {

        String sql = "SELECT troca.idtroca, "+
                "troca.datatroca, "+
                "troca.status," +
                " itemtroca.iditemtroca, "+
                "itemtroca.idtroca, "+
                "itemtroca.idusuariooferta, "+
                "itemtroca.idusuariotroca, "+
                "itemtroca.idjogooferta, "+
                "itemtroca.idjogotroca, "+
                "itemtroca.nomeusuariooferta, "+
                "itemtroca.nomeusuariotroca, "+
                "itemtroca.nomejogooferta, "+
                "itemtroca.nomejogotroca, "+
                "itemtroca.descricaooferta, "+
                "itemtroca.descricaotroca, "+
                "itemtroca.categoriaoferta, "+
                "itemtroca.categoriatroca, "+
                "itemtroca.plataformaoferta, "+
                "itemtroca.plataformatroca, "+
                "itemtroca.anooferta, "+
                "itemtroca.anotroca, "+
                "itemtroca.imagemoferta, "+
                "itemtroca.imagemtroca "+
                "FROM troca "+
                "INNER JOIN itemtroca itemtroca ON troca.idtroca = itemtroca.idtroca "+
                "where troca.idtroca = " + idTroca;

        List<Troca> listaTrocas = new ArrayList<Troca>();
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Troca troca = new Troca();

        if (cursor.moveToNext()) {
            troca = parseTroca(troca, cursor);
        }

        return troca;
    }

    public int atualizarStatusTroca(int idTroca, StatusTroca statusTroca){
        int upd = 0;
        String where = "idtroca="+idTroca;

        Troca troca = this.buscarTroca(idTroca);

        if (troca.getId() > 0) {
            upd = -1;

            if (!troca.getStatusTroca().toString().equals(statusTroca.toString())) {

                db = banco.getWritableDatabase();

                db.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status", statusTroca.toString());

                    upd = db.update("troca", contentValues, where, null);

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    db.endTransaction();
                }
                db.endTransaction();

                if (troca.getStatusTroca().equals(StatusTroca.TROCA_ANDAMENTO) && statusTroca.equals(StatusTroca.TROCA_CONCLUIDA)) {
                    TrocaConcluida trocaConcluida = new TrocaConcluida(idTroca, context, UsuarioUtil.obterUsuario(context));
                    trocaConcluida.efetuarTroca();
                }
            }
        }

//        if (troca.getId() > 0) {
//            if (troca.getStatusTroca().equals(StatusTroca.TROCA_ANDAMENTO) && statusTroca.equals(StatusTroca.TROCA_CONCLUIDA)) {
//                TrocaConcluida trocaConcluida = new TrocaConcluida(idTroca, context, UsuarioUtil.obterUsuario(context, "dadosUsuario"));
//                trocaConcluida.efetuarTroca();
//            }
//        }

        return upd;
    }


    public void deleteTrocas(){
        String where = "1=1";
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            db.delete("troca", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();


        ItemJogoTrocaCRUD itemJogoTrocaCRUD = new ItemJogoTrocaCRUD(context);
        itemJogoTrocaCRUD.deleteAllItensTroca();
    }


    private Troca parseTroca(Troca troca, Cursor cursor){
        troca.setId(cursor.getInt(cursor.getColumnIndex("idtroca")));

        switch (cursor.getString(cursor.getColumnIndex("status"))) {
            case "A": {
                troca.setStatusTroca(StatusTroca.TROCA_ANALISE);
                break;
            }
            case "N": {
                troca.setStatusTroca(StatusTroca.TROCA_ANDAMENTO);
                break;
            }
            case "D": {
                troca.setStatusTroca(StatusTroca.TROCA_CANCELADA);
                break;
            }
            case "C": {
                troca.setStatusTroca(StatusTroca.TROCA_CONCLUIDA);
                break;
            }
            case "R": {
                troca.setStatusTroca(StatusTroca.TROCA_REJEITADA);
                break;
            }
            default:
                troca.setStatusTroca(StatusTroca.TROCA_ANALISE);
        }

        //carregando jogos da base
        ItemJogoTroca itemJogoTroca = new ItemJogoTroca();

        itemJogoTroca.setIdUsuarioTroca(cursor.getInt(cursor.getColumnIndex("idusuariotroca")));
        itemJogoTroca.setIdUsuarioOferta(cursor.getInt(cursor.getColumnIndex("idusuariooferta")));
        itemJogoTroca.setNomeUsuarioTroca(cursor.getString(cursor.getColumnIndex("nomeusuariotroca")));
        itemJogoTroca.setNomeUsuarioOferta(cursor.getString(cursor.getColumnIndex("nomeusuariooferta")));

        itemJogoTroca.getJogoOferta().setId(cursor.getInt(cursor.getColumnIndex("idjogooferta")));
        itemJogoTroca.getJogoOferta().setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogooferta")));
        itemJogoTroca.getJogoOferta().setDescricao(cursor.getString(cursor.getColumnIndex("descricaooferta")));
        itemJogoTroca.getJogoOferta().setCategoria(cursor.getInt(cursor.getColumnIndex("categoriaoferta")));
        itemJogoTroca.getJogoOferta().setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataformaOferta"))));
        itemJogoTroca.getJogoOferta().setAno(cursor.getInt(cursor.getColumnIndex("anooferta")));
        itemJogoTroca.getJogoOferta().setImagem(cursor.getString(cursor.getColumnIndex("imagemoferta")));

        itemJogoTroca.getJogoTroca().setId(cursor.getInt(cursor.getColumnIndex("idjogotroca")));
        itemJogoTroca.getJogoTroca().setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogotroca")));
        itemJogoTroca.getJogoTroca().setDescricao(cursor.getString(cursor.getColumnIndex("descricaotroca")));
        itemJogoTroca.getJogoTroca().setCategoria(cursor.getInt(cursor.getColumnIndex("categoriatroca")));
        itemJogoTroca.getJogoTroca().setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataformaTroca"))));
        itemJogoTroca.getJogoTroca().setAno(cursor.getInt(cursor.getColumnIndex("anotroca")));
        itemJogoTroca.getJogoTroca().setImagem(cursor.getString(cursor.getColumnIndex("imagemtroca")));

        troca.setJogosTroca(itemJogoTroca);
        return troca;
    }

}
