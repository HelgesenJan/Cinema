package Kontroll;

import java.util.ArrayList;
import java.util.Date;

public class Film {

    private int filmnr;
    private String filmnavn;
    private ArrayList<Visning> visninger = new ArrayList<>();

    public Film(int filmnr, String filmnavn) {
        this.filmnr = filmnr;
        this.filmnavn = filmnavn;

    }

    /**
     * Oppretter en visning og legger den til i Film sin ArrayListe
     * @param visningsNr
     * @param filmen
     * @param kinosalen
     * @param dato
     * @param startTid
     * @param pris
     */
    public void leggTilVisning(int visningsNr, Film filmen, Kinosal kinosalen, Date dato, Date startTid, Double pris) {
        visninger.add(new Visning(visningsNr, filmen, kinosalen, dato, startTid, pris));
    }

    public int getFilmnr() {
        return filmnr;
    }

    public String getFilmnavn() {
        return filmnavn;
    }
}
