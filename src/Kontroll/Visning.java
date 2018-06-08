package Kontroll;

import javax.swing.text.html.HTMLDocument;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;

public class Visning implements Comparable<Visning> {

    private int visningsNr;
    private Film film;
    private Kinosal kinosal;
    private Date dato;
    private double pris;

    private final static Calendar kalender = Calendar.getInstance();
    private final static Collator kollator = Collator.getInstance();

    private ArrayList<Billett> billetter = new ArrayList<>();

    public Visning(int visningsNr) {
        this.visningsNr = visningsNr;
    }

    public Visning(int visningsNr, Film film, Kinosal kinosal, Date dato, double pris) {
        this.visningsNr = visningsNr;
        this.film = film;
        this.kinosal = kinosal;
        this.dato = dato;
        this.pris = pris;
    }


    public int getIkkeBetalte() {
        int antall = 0;

        for(int i=0; i<billetter.size(); i++) {
            if(!billetter.get(i).isErBetalt()) {
                antall ++;
            }
        }
        return antall;
    }

    /**
     * Legger til en Billett i Visning sin ArrayList over Billetter
     * @param billettkode
     * @param erBetalt
     */
    public void leggTilBillett(String billettkode, boolean erBetalt) {
        billetter.add(new Billett(billettkode, this, erBetalt));
    }

    public void leggTilBillett(Billett billett) {
        billetter.add(billett);
    }

    public ArrayList<Billett> getBilletter() {
        return billetter;
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

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }



    public String getStartTid() {

        //Gi kalender dato
        kalender.setTime(this.dato);

        //Hent ut dag, mnd, tid og minutter
        int dag =  kalender.get(Calendar.DAY_OF_MONTH);
        int mnd = kalender.get(Calendar.MONTH);
        int år = kalender.get(Calendar.YEAR);

        int time = kalender.get(Calendar.HOUR_OF_DAY);
        int minutter = kalender.get(Calendar.MINUTE);

        return " Kl: " + time + ":" + minutter + " - " + dag + "." + mnd + "." + år;
    }

    public ArrayList<Plass> finnLedigePlasser() {
        ArrayList<Plass> plasser = kinosal.getPlasser();
        for(Plass plass:plasser) {
            for(Billett billett:this.billetter) {
                if(billett.harPlass(plass)) {
                    plasser.remove(plass);
                }
            }
        }
        return plasser;
    }



    @Override
    public String toString() {
        return "Visning{" +
                "visningsNr=" + visningsNr +
                ", film=" + film.getFilmnavn() +
                ", kinosal=" + kinosal.getKinosalnavn() +
                ", dato=" + dato +
                ", pris=" + pris +
                '}';
    }

    @Override
    public int compareTo(Visning o) {
        //Hent ut sortering fra kontroll
        Sortering sort = Kontroll.sortering;
        //Detekter at en dummy visning er brukt, bytt til verdi modus for binær søk.
        if(o.getFilm() == null) {
            sort = Sortering.VERDI;
        }
        switch (sort) {
            case ALFABETISK:
                //Sortere alfabetisk
                return kollator.compare(this.film.getFilmnavn(), o.getFilm().getFilmnavn());
            case TID:
                //Sorter etter tid
                return this.dato.compareTo(o.getDato());
                //Sortere etter visningsnummer (dummy visning)
            case VERDI:
                if(this.visningsNr < o.getVisningsNr()) {
                    return -1;
                } else if(this.visningsNr > o.getVisningsNr()) {
                    return 1;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }
}
