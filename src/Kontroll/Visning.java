package Kontroll;

import java.util.ArrayList;
import java.util.Date;

public class Visning {

    private Film film;
    private Kinosal kinosal;
    private Date dato;
    private Date startTid;
    private double pris;

    private ArrayList<Billett> billetter = new ArrayList<>();

    public Visning(Film film, Kinosal kinosal, Date dato, Date startTid, double pris) {
        this.film = film;
        this.kinosal = kinosal;
        this.dato = dato;
        this.startTid = startTid;
        this.pris = pris;
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
