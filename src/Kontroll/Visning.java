package Kontroll;

import java.util.ArrayList;
import java.util.Date;

public class Visning {

    private int visningsNr;
    private Film film;
    private Kinosal kinosal;
    private Date dato;
    private Date startTid;
    private double pris;

    private ArrayList<Billett> billetter = new ArrayList<>();

    public Visning(int visningsNr, Film film, Kinosal kinosal, Date dato, Date startTid, double pris) {
        this.visningsNr = visningsNr;
        this.film = film;
        this.kinosal = kinosal;
        this.dato = dato;
        this.startTid = startTid;
        this.pris = pris;
    }

    /**
     * Legger til en Billett i Visning sin ArrayList over Billetter
     * @param billettkode
     * @param erBetalt
     */
    public void leggTilBillett(int billettkode, boolean erBetalt) {
        billetter.add(new Billett(billettkode, this, erBetalt));
    }


    public int getVisningsNr() {
        return visningsNr;
    }

    public void setVisningsNr(int visningsNr) {
        this.visningsNr = visningsNr;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Kinosal getKinosal() {
        return kinosal;
    }

    public void setKinosal(Kinosal kinosal) {
        this.kinosal = kinosal;
    }

    public Date getDato() {
        return dato;
    }

    public void setDato(Date dato) {
        this.dato = dato;
    }

    public Date getStartTid() {
        return startTid;
    }

    public void setStartTid(Date startTid) {
        this.startTid = startTid;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }
}
