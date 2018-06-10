package Kontroll;

import java.util.ArrayList;
import java.util.Iterator;

public class Kinosal implements Comparable<Kinosal> {


    private int kinosalnr;
    private Kino kino;
    private String kinosalnavn;

    private ArrayList<Plass> plasser = new ArrayList<>();
    //private ArrayList<Visning> visninger = new ArrayList<>();

    /**
     * Konstruktør for oppretting av dummy kinosal. Brukes i binærsøk
     * @param kinosalnr
     */

    public Kinosal(int kinosalnr) {
        this.kinosalnr = kinosalnr;
    }

    /**
     * Ordinær konstruktør
     * @param kinosalnr
     * @param kino
     * @param kinonavn
     */

    public Kinosal(int kinosalnr, Kino kino, String kinonavn) {
        this.kinosalnr = kinosalnr;
        this.kino = kino;
        this.kinosalnavn = kinonavn;
    }

    /**
     * Finn en plass i kinosalen basert på radnr og setenr
     * @param radnr
     * @param setenr
     * @return Plass
     */

    public Plass finnPlass(int radnr, int setenr) {
        Iterator itr = plasser.iterator();
        while (itr.hasNext()) {
            Plass plass = (Plass )itr.next();
            if(plass.getRadnr() == radnr && plass.getSetenr() == setenr) {
                return plass;
            }
        }
        return null;
    }

    /**
     * Finn antall plasser i en kinosal
     * @return int
     */

    public int getAntallPlasser() {
        int antall = 0;

        for(int i=0; i<plasser.size(); i++) {
            antall++;
        }
        return antall;
    }

    /**
     * Legg til en plass i kinosal
     * @param radnr
     * @param setenr
     */

    public void leggTilPlass(int radnr, int setenr) {
        plasser.add(new Plass(radnr,setenr, this));
    }


    /**
     * Hent kinosalnr
     * @return int
     */

    public int getKinosalnr() {
        return this.kinosalnr;
    }

    /**
     * Hent en kinosalens kino
     * @return Kino
     */

    public Kino getKino() {
        return this.kino;
    }

    /**
     * Hent kinosal navn
     * @return String
     */

    public String getKinosalnavn() {
        return this.kinosalnavn;
    }

    /**
     * Hent plasse arrayList
     * @param plasser
     */

    public void setPlasser(ArrayList<Plass> plasser) {
        this.plasser = plasser;
    }

    /**
     * Hent arraylist med plasser
     * @return ArrayList
     */

    public ArrayList<Plass> getPlasser() {
        return this.plasser;
    }

    /**
     * @return String
     */

    @Override
    public String toString() {

        String plass_liste = "[";
        for(Plass plass: plasser) {
            plass_liste += plass.toString() + ",";
        }
        plass_liste+="]";

        return "Kinosal{" +
                "kinosalnr=" + this.kinosalnr +
                ", kino='" + this.kino.getKinonavn() + '\'' +
                ", plasser=" + plass_liste +
                '}';
    }


    /**
     * return int
     * @param o
     * @return
     */

    @Override
    public int compareTo(Kinosal o) {
        if(this.kinosalnr < o.getKinosalnr()) {
            return -1;
        } else if(this.kinosalnr > o.getKinosalnr()) {
            return 1;
        } else {
            return 0;
        }
    }
}
