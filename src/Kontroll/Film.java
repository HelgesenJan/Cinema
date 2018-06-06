package Kontroll;

import java.util.ArrayList;

public class Film {

    private int filmnr;
    private String filmnavn;
    private ArrayList<Visning> visninger = new ArrayList<>();

    public Film(int filmnr, String filmnavn) {
        this.filmnr = filmnr;
        this.filmnavn = filmnavn;
    }


    public int getFilmnr() {
        return filmnr;
    }

    public String getFilmnavn() {
        return filmnavn;
    }
}
