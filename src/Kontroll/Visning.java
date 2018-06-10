package Kontroll;

import java.util.Calendar;
import java.util.Date;
import java.text.Collator;
import java.util.ArrayList;


public class Visning implements Comparable<Visning> {

    private int visningsNr;
    private Film film;
    private Kinosal kinosal;
    private Date dato;
    private double pris;

    private final static Calendar kalender = Calendar.getInstance();
    private final static Collator kollator = Collator.getInstance();

    private ArrayList<Billett> billetter = new ArrayList<>();


    /**
     * Konstruktør for dummy visning, brukes i binær søk
     * @param visningsNr
     */

    public Visning(int visningsNr) {
        this.visningsNr = visningsNr;
    }

    /**
     * Ordinær konstruktør
     * @param visningsNr
     * @param film
     * @param kinosal
     * @param dato
     * @param pris
     */

    public Visning(int visningsNr, Film film, Kinosal kinosal, Date dato, double pris) {
        this.visningsNr = visningsNr;
        this.film = film;
        this.kinosal = kinosal;
        this.dato = dato;
        this.pris = pris;
    }


    /**
     * Returnerer sann dersom visningen har billetter
     * @return boolean
     */

    public boolean harBilletter() {
        System.out.println(this.billetter.size());
        return this.billetter.size() > 0;
    }


    /**
     * Legger til en billet i en visning
     * @param billett
     */

    public void leggTilBillett(Billett billett) {
        this.billetter.add(billett);
    }

    /**
     * Fjerner en billet fra visning
     * @param billett
     */
    public void fjernBillett(Billett billett) {
        this.billetter.remove(billett);
    }

    /**
     * Henter liste over billetter i visning
     * @return ArrayList<Billett>
     */

    public ArrayList<Billett> getBilletter() {
        return billetter;
    }

    /**
     *  Returner visningsnr
     * @return visningsnr
     */

    public int getVisningsNr() {
        return visningsNr;
    }


    /**
     * @return film objekt
     */
    public Film getFilm() {
        return this.film;
    }

    /**
     * Oppdater film objekt
     * @param film
     */

    public void setFilm(Film film) {
        this.film = film;
    }

    /**
     *
     * @return Kinosal
     */

    public Kinosal getKinosal() {
        return this.kinosal;
    }

    /**
     * Oppdater kinosal
     * @param kinosal
     */

    public void setKinosal(Kinosal kinosal) {
        this.kinosal = kinosal;
    }

    /**
     * Returner dato
     * @return Date
     */

    public Date getDato() {
        return this.dato;
    }


    /**
     * Sett ny dato
     * @param dato
     */
    public void setDato(Date dato) {
        this.dato = dato;
    }

    /**
     * hent pris
     * @return double
     */
    public double getPris() {
        return pris;
    }

    /**
     * Oppdater pris
     * @param pris
     */

    public void setPris(double pris) {
        this.pris = pris;
    }

    /**
     * Hent ut dato i dag måned format
     * @return String
     */

    public String getDagMnd() {
        //Gi kalender dato
        kalender.setTime(this.dato);

        //Hent ut dag, mnd, tid og minutter
        int dag =  kalender.get(Calendar.DAY_OF_MONTH);
        int mnd = kalender.get(Calendar.MONTH) +1;
        int år = kalender.get(Calendar.YEAR);

        return år + "-" + mnd + "-" + dag;
    }

    /**
     * Hent start klokkeslett format
     * @return String
     */

    public String getStartKlokkeslett(){
        //Gi kalender dato
        kalender.setTime(this.dato);

        int time = kalender.get(Calendar.HOUR_OF_DAY);
        int minutter = kalender.get(Calendar.MINUTE);

        return time + ":" + minutter;
    }

    /**
     * Hent start tid med dato og klokkeslett
     * @return String
     */
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

    /**
     * Finner ledige plasser i en visning
     * @return
     */

    public ArrayList<Plass> finnLedigePlasser() {
        ArrayList<Plass> plasser = new ArrayList<Plass>(this.kinosal.getPlasser());
        ArrayList<Billett> billetter = this.billetter;

        for (Billett billett:billetter) {
            for(Plass plass: billett.getPlasser()) {
                plasser.remove(plass);
            }
        }
        return plasser;
    }


    /**
     *
     * @return String
     */
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

    /**
     * Muligheten til sammenligne film(filmavn), tid(dato) eller verdi(visningsnr)
     * @param o
     * @return int
     */

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
