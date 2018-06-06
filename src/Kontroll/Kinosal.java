package Kontroll;

import java.util.ArrayList;

public class Kinosal {

    private int kinosalnr;
    private String salnr;
    private String kinonavn;

    private ArrayList<Plass> plasser = new ArrayList<>();

    public Kinosal(int kinosalnr, String salnr, String kinonavn) {
        this.kinosalnr = kinosalnr;
        this.salnr = salnr;
        this.kinonavn = kinonavn;
    }

    public int getKinosalnr() {
        return kinosalnr;
    }

    public String getSalnr() {
        return salnr;
    }

    public String getKinonavn() {
        return kinonavn;
    }
}
