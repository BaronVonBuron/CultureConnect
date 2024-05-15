package com.example.cultureconnect.Projekt;

import java.time.LocalDate;
import java.util.Date;

public class ProjektAktivitet {
    private LocalDate startDato;
    private LocalDate slutDato;
    private String aktivitet;

    public ProjektAktivitet(LocalDate startDato, LocalDate slutDato, String aktivitet) {
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.aktivitet = aktivitet;
    }

    public Date getStartDatoAsUtilDate() {
        Date date = new Date();
        date.setTime(this.startDato.toEpochDay());
        return date;
    }

    public Date getEndDatoAsUtilDate() {
        Date date = new Date();
        date.setTime(this.slutDato.toEpochDay());
        return date;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    public String getAktivitet() {
        return aktivitet;
    }

    public void setAktivitet(String aktivitet) {
        this.aktivitet = aktivitet;
    }

    @Override
    public String toString() {
        return startDato.toString() +" | "+ slutDato.toString() +" | "+ aktivitet;
    }
}
