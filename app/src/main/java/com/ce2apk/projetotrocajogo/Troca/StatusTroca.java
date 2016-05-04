package com.ce2apk.projetotrocajogo.Troca;

/**
 * Created by carlosbridi on 18/01/16.
 */
public enum StatusTroca {

    TROCA_ANALISE {
        @Override
        public String toString() {
            return "A";
        }
    },

    TROCA_ANDAMENTO{
        @Override
        public String toString() {
            return "N";
        }
    },

    TROCA_CANCELADA {
        @Override
        public String toString(){
            return "D";
        }
    },

    TROCA_REJEITADA {
        @Override
        public String toString(){
            return "R";
        }
    },

    TROCA_CONCLUIDA {
        @Override
        public String toString(){
            return "C";
        }
    },




}
