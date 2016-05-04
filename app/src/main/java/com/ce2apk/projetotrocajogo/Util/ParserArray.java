package com.ce2apk.projetotrocajogo.Util;


/**
 * Created by carlosbridi on 17/12/15.
 */
public class ParserArray {

    public static String categoriaJogo(int position){
        switch (position){
            case 1: return "2D - Fighting";
            case 2: return "Plataforma";
            case 3: return "Luta";
            case 4: return "Ação";
            case 5: return "Aventura";
            case 6: return "Esporte";
            case 7: return "Simulador";
            case 8: return "Tabuleiro";
            case 9: return "Estratégia";
            case 10: return "Corrida";
            case 11: return "Casino";
            case 12: return "RPG";
            case 13: return "Educativo";
            case 14: return "Tiro";
            case 15: return "Fantasia";
            case 16: return "Interativo";
            case 17: return "Puzzle";
            case 18: return "Variados";
            case 19: return "Guerra";
            case 20: return "Pinball";

            default: return "";
        }

    }

    public static String plataformaJogo(int position){

        switch(position){
            case 1: return "PS2 - PLAYSTATION 2";
            case 2: return "PS3 - PLAYSTATION 3";
            case 3: return "PS4 - PLAYSTATION 4";
            case 4: return "PS - VITA";
            case 5: return "XBOX 360";
            case 6: return "XBOX ONE";
            case 7: return "PC - COMPUTADOR/NOTEBOOK";
            case 8: return "NINTENDO WII";
            default: return "";
        }

    }

}
