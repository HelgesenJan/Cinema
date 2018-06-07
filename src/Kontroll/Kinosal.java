package Kontroll;

import java.text.Collator;
import java.util.ArrayList;

public class Kinosal implements Comparable<Kinosal> {

    private final static Collator kollator = Collator.getInstance();

    private int kinosalnr;
    private String kinosalnavn;
    private String kinonavn;

    private ArrayList<Plass> plasser = new ArrayList<>();

    public Kinosal(int kinosalnr) {
        this.kinosalnr = kinosalnr;
    }

    public Kinosal(int kinosalnr, String kinosalnavn, String kinonavn) {
        this.kinosalnr = kinosalnr;
        this.kinosalnavn = kinosalnavn;
        this.kinonavn = kinonavn;
    }

    public void leggTilPlass(int radnr, int setenr) {
        plasser.add(new Plass(radnr,setenr, this));
    }


    public int getKinosalnr() {
        return kinosalnr;
    }

    public String getKinosalnavn() {
        return kinosalnavn;
    }

    public String getKinonavn() {
        return kinonavn;
    }

    @Override
    public String toString() {
        return "Kinosal{" +
                "kinosalnr=" + kinosalnr +
                ", kinosalnavn='" + kinosalnavn + '\'' +
                ", kinonavn='" + kinonavn + '\'' +
                ", plasser=" + plasser +
                '}';
    }

    @Override
    public int compareTo(Kinosal o) {
        if(this.kinosalnr < o.kinosalnr) {
            return -1;
        } else if(this.kinosalnr > o.kinosalnr) {
            return 1;
        } else {
            return 0;
        }
    }
}
