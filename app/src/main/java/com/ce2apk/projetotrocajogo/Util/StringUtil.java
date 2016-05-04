package com.ce2apk.projetotrocajogo.Util;

/**
 * Created by carlosbridi on 26/11/15.
 */
public class StringUtil {

    public static String retiraCaracteresEspeciais(String stringFonte){
        String passa = stringFonte.toLowerCase();
        passa = passa.replaceAll("[ÂÀÁÄÃ]", "A");
        passa = passa.replaceAll("[âãàáä]", "a");
        passa = passa.replaceAll("[ÊÈÉË]", "E");
        passa = passa.replaceAll("[êèéë]", "e");
        passa = passa.replaceAll("ÎÍÌÏ", "I");
        passa = passa.replaceAll("îíìï", "i");
        passa = passa.replaceAll("[ÔÕÒÓÖ]", "O");
        passa = passa.replaceAll("[ôõòóö]", "o");
        passa = passa.replaceAll("[ÛÙÚÜ]", "U");
        passa = passa.replaceAll("[ûúùü]", "u");
        passa = passa.replaceAll("Ç", "C");
        passa = passa.replaceAll("ç", "c");
        passa = passa.replaceAll("[ýÿ]", "y");
        passa = passa.replaceAll("Ý", "Y");
        passa = passa.replaceAll("ñ", "n");
        passa = passa.replaceAll("Ñ", "N");
        passa = passa.replaceAll("['\"]", "");
        passa = passa.replaceAll("[<>()\\{\\}]", "");
        passa = passa.replaceAll("['\\\\.,()|/]", "");
        passa = passa.replaceAll("[^!-ÿ]{1}[^ -ÿ]{0,}[^!-ÿ]{1}|[^!-ÿ]{1}", " ");
        passa = passa.replaceAll(" ", "");
        return passa;
    }

}
