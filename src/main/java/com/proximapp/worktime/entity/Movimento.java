package com.proximapp.worktime.entity;

import java.util.Date;

public class Movimento {
    private String dataEntrata;
    private long idUtente;
    private boolean entrata;

    public boolean isEntrata() {
        return entrata;
    }

    public void setEntrata(boolean entrata) {
        this.entrata = entrata;
    }

    public long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }


    public String getDataEntrata() {
        return dataEntrata;
    }

    public void setDataEntrata(String dataEntrata) {
        this.dataEntrata = dataEntrata;
    }
}