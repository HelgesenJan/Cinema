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
     * Oppretter en visning og legger den til i Film sin ArrayListe

     */
  //  public void leggTilVisning(int visningsNr, Film filmen, Kinosal kinosalen, Date dato, Date startTid, Double pris) {
     //   visninger.add(new Visning(visningsNr, filmen, kinosalen, dato, startTid, pris));
//    }

    public ArrayList<Visning> getVisninger() {
        return visninger;
    }


    public void leggTilVisning(Visning visning) {
        visninger.add(visning);
    }

    public void fjernVisning(Visning visning) {
        this.visninger.remove(visning);
    }

    public int getFilmnr() {
        return filmnr;
    }

    public String getFilmnavn() {
        return filmnavn;
    }

    public void setFilmnavn(String filmnavn) {
        this.filmnavn = filmnavn;
    }

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
