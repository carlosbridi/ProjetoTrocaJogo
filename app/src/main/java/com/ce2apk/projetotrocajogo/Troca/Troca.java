package com.ce2apk.projetotrocajogo.Troca;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by carlosbridi on 18/01/16.
 */
public class Troca implements Serializable {

    private int id; // Id Troca
    private ItensJogoTroca jogoTroca; // Jogo troca do usuário remoto
    private Date dataTroca; // Data da troca
    private StatusTroca statusTroca; // Status da Troca (Analise, Cancelada, Finalizada)

    public Troca(){

    }

    public Troca(int id, ItensJogoTroca jogoTroca, Date dataTroca, StatusTroca statusTroca) {
        this.id = id;
        this.jogoTroca = jogoTroca;
        this.dataTroca = dataTroca;
        this.statusTroca = statusTroca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItensJogoTroca getJogosTroca() {
        return jogoTroca;
    }

    public void setJogosTroca(ItensJogoTroca jogoTroca) {
        this.jogoTroca = jogoTroca;
    }

    public Date getDataTroca() {
        return dataTroca;
    }

    public void setDataTroca(Date dataTroca) {
        this.dataTroca = dataTroca;
    }

    public StatusTroca getStatusTroca() {
        return statusTroca;
    }

    public void setStatusTroca(StatusTroca statusTroca) {
        this.statusTroca = statusTroca;
    }
}
