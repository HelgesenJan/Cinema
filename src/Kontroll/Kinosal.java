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
        return this.kinosalnr;
    }

    public String getKinosalnavn() {
        return this.kinosalnavn;
    }

    public String getKinonavn() {
        return this.kinonavn;
    }

    @Override
    public String toString() {

        String plass_liste = "[";
        for(Plass plass: plasser) {
            plass_liste += plass.toString() + ",";
        }
        plass_liste+="]";

        return "Kinosal{" +
                "kinosalnr=" + this.kinosalnr +
                ", kinosalnavn='" + this.kinosalnavn + '\'' +
                ", kinonavn='" + this.kinonavn + '\'' +
                ", plasser=" + plass_liste +
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
