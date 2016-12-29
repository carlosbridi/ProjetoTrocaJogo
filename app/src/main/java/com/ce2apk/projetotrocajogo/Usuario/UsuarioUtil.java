package com.ce2apk.projetotrocajogo.Usuario;

import android.content.Context;
import android.content.SharedPreferences;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Util.SharedPref;

/**
 * Created by carlosbridi on 10/11/15.
 */
public class UsuarioUtil {

    public UsuarioUtil(){

    }

    public static boolean usuarioEmCache(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("usuariologado", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("usuariologado", false);
    }

    public static Usuario obterUsuario(Context ctx){

        Usuario usuarioLogado = new Usuario();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE);
        usuarioLogado.setId(Integer.valueOf(sharedPreferences.getString("id", "0")));
        usuarioLogado.setNome(sharedPreferences.getString("nome", ""));
        usuarioLogado.setNomeUsuario(sharedPreferences.getString("nomeusuario", ""));
        usuarioLogado.setEmail(sharedPreferences.getString("email", ""));
        usuarioLogado.setTelefone(sharedPreferences.getString("telefone", ""));
        usuarioLogado.setCep(sharedPreferences.getString("cep", ""));
        usuarioLogado.setLogradouro(sharedPreferences.getString("logradouro", ""));
        usuarioLogado.setNumero(sharedPreferences.getString("numero", ""));
        usuarioLogado.setComplemento(sharedPreferences.getString("complemento", ""));
        usuarioLogado.setEstado(sharedPreferences.getString("estado", ""));
        usuarioLogado.setBairro(sharedPreferences.getString("bairro", ""));
        usuarioLogado.setCidade(sharedPreferences.getString("cidade", ""));

        return usuarioLogado;
    }


    public static void removerDadosUsuario(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("usuarioLogado", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", "");
        editor.putString("nome", "");
        editor.putString("nomeusuario", "");
        editor.putString("email", "");
        editor.putString("telefone", "");
        editor.putString("cep", "");
        editor.putString("logradouro", "");
        editor.putString("numero", "");
        editor.putString("complemento", "");
        editor.putString("bairro", "");
        editor.putString("cidade", "");
        editor.commit();
    }

    public static void gravarID(Context ctx, int ID){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("dadosUsuario", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", String.valueOf(ID));
        editor.commit();

    }

    public static void carregouJogos(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("jogosColecao", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("buscouDados", true);
        editor.commit();
    }

    public static boolean jogosNaColecao(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("jogosColecao", context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean("buscouDados", false));
    }


    public static void carregouInteresses(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("interessesColecao", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("buscouDados", true);
        editor.commit();
    }

    public static boolean interessesNaColecao(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("interessesColecao", context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean("buscouDados", false));
    }


    public static void carregouTrocas(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("trocasUsuario", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("buscouDados", true);
        editor.commit();
    }

    public static boolean trocasNaBase(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("trocasUsuario", context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean("buscouDados", false));
    }

}
