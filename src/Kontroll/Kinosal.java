package Kontroll;

import java.util.ArrayList;
import java.util.Iterator;

public class Kinosal implements Comparable<Kinosal> {


    private int kinosalnr;
    private Kino kino;
    private String kinosalnavn;

    private ArrayList<Plass> plasser = new ArrayList<>();

    public Kinosal(int kinosalnr) {
        this.kinosalnr = kinosalnr;
    }

    public Kinosal(int kinosalnr, Kino kino, String kinonavn) {
        this.kinosalnr = kinosalnr;
        this.kino = kino;
        this.kinosalnavn = kinonavn;
    }

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

    public void leggTilPlass(int radnr, int setenr) {
        plasser.add(new Plass(radnr,setenr, this));
    }


    public int getKinosalnr() {
        return this.kinosalnr;
    }

    public Kino getKino() {
        return this.kino;
    }

    public String getKinosalnavn() {
        return this.kinosalnavn;
    }

    public void setPlasser(ArrayList<Plass> plasser) {
        this.plasser = plasser;
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
                ", kino='" + this.kino.getKinonavn() + '\'' +
                ", plasser=" + plass_liste +
                '}';
    }



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
