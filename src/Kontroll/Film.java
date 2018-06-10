package Kontroll;

import java.util.ArrayList;


public class Film implements Comparable<Film>{

    private int filmnr;
    private String filmnavn;
    private ArrayList<Visning> visninger = new ArrayList<>();

    public Film(int filmnr) {
        this.filmnr = filmnr;
    }

    public Film(int filmnr, String filmnavn) {
        this.filmnr = filmnr;
        this.filmnavn = filmnavn;

    }


    /**
     * Legg til visning
     * @param visning
     */

    public void leggTilVisning(Visning visning) {
        visninger.add(visning);
    }

    /**
     * Fjern en visning
     * @param visning
     */

    public void fjernVisning(Visning visning) {
        this.visninger.remove(visning);
    }

    /**
     * Hent filmnr
     * @return int
     */

    public int getFilmnr() {
        return filmnr;
    }

    /**
     * Hent filmnavn
     * @return int
     */

    public String getFilmnavn() {
        return filmnavn;
    }

    /**
     * Oppdater filmavn
     * @param filmnavn
     */

    public void setFilmnavn(String filmnavn) {
        this.filmnavn = filmnavn;
    }

    /**
     * Returner liste over visninger
     * @return
     */
    public ArrayList<Visning> getVisninger() {
        return visninger;
    }


    /**
     *
     * @param o
     * @return int
     */
    @Override
    public int compareTo(Film o) {
        if(this.filmnr < o.getFilmnr()) {
            return -1;
        } else if(this.filmnr > o.getFilmnr()) {
            return 1;
        } else {
            return 0;
        }
    }
}
