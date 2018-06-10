package Kontroll;

import java.text.Collator;
import java.util.ArrayList;

public class Kino implements Comparable<Kino>{

    private final static Collator kollator = Collator.getInstance();

    private String kinonavn;
    private ArrayList<Kinosal> kinosaler = new ArrayList<>();

    public Kino(String kinonavn) {
        this.kinonavn = kinonavn;
    }

    /**
     * Legg til kinosal
     * @param sal
     */

    public void leggTilKinosal(Kinosal sal) {
        kinosaler.add(sal);
    }

    /**
     * Hent kinonavn
     * @return String
     */

    public String getKinonavn() {
        return kinonavn;
    }

    /**
     * Hent kinosal liste
     * @return ArrayList<Kinosal>
     */

    public ArrayList<Kinosal> getKinosaler() {
        return kinosaler;
    }

    /**
     *
     * @param o
     * @return int
     */

    @Override
    public int compareTo(Kino o) {
        return kollator.compare(this.kinonavn, o.getKinonavn());
    }

    /**
     *
     * @return String
     */

    @Override
    public String toString() {
        return "Kino{" +
                "kinonavn='" + kinonavn + '\'' +
                ", kinosaler=" + kinosaler +
                '}';
    }
}
