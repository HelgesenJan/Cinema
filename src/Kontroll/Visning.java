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

    public boolean harBilletter() {
        System.out.println(this.billetter.size());
        return this.billetter.size() > 0;
    }

    public int getAntallBilletter() {
        int antall = 0;
        for(int i=0; i<billetter.size(); i++) {
            antall++;
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

    public void fjernBillett(Billett billett) {
        billetter.remove(billett);
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

    public String getDagMnd() {
        //Gi kalender dato
        kalender.setTime(this.dato);

        //Hent ut dag, mnd, tid og minutter
        int dag =  kalender.get(Calendar.DAY_OF_MONTH);
        int mnd = kalender.get(Calendar.MONTH) +1;
        int år = kalender.get(Calendar.YEAR);

        return år + "-" + mnd + "-" + dag;
    }


    public String getStartKlokkeslett(){
        //Gi kalender dato
        kalender.setTime(this.dato);

        int time = kalender.get(Calendar.HOUR_OF_DAY);
        int minutter = kalender.get(Calendar.MINUTE);

        return time + ":" + minutter;
    }

    public String getStartTid() {

        //Gi kalender dato
        kalender.setTime(this.dato);

        //Hent ut dag, mnd, tid og minutter
        int dag =  kalender.get(Calendar.DAY_OF_MONTH);
        int mnd = kalender.get(Calendar.MONTH) +1;
        int år = kalender.get(Calendar.YEAR);

        int time = kalender.get(Calendar.HOUR_OF_DAY);
        int minutter = kalender.get(Calendar.MINUTE);

        return " Kl: " + time + ":" + minutter + " - " + dag + "." + mnd + "." + år;
    }

    /**
     * Returnerer sann dersom visning ikke har "gått" enda
     * @return boolean
     */

    public boolean erKommende() {
        return new Date().before(this.dato);
    }

    /**
     * Returnerer sann dersom en visning har gått, passert starttidspunktet.
     * @return boolean
     */

    public boolean harGått() {
        return new Date().after(this.dato);
    }

    /**
     * Er sann dersom det er en halvtime før filmen starter
     * @return
     */

    public boolean erhalvtimeFørStart() {
        Date dato_halvtimefør = new Date();
        //Bruk kalender til å hente ut dato objekt en halvtime frem i tid
        kalender.setTime(dato_halvtimefør);
        kalender.add(Calendar.MINUTE, 30); //Legg til 30 minutt
        dato_halvtimefør = kalender.getTime();
        //Sammenlign med visningsdato og returner boolean
        return dato_halvtimefør.before(this.dato);
    }


    public ArrayList<Plass> finnLedigePlasser() {

        ArrayList<Plass> plasser = kinosal.getPlasser();
        ArrayList<Billett> billetter = this.billetter;

        Iterator itr_plass = plasser.iterator();
        Iterator itr_billett = billetter.iterator();

        while(itr_plass.hasNext()) {
            Plass plass = (Plass) itr_plass.next();
            while (itr_billett.hasNext()) {
                Billett billett = (Billett) itr_billett.next();
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
