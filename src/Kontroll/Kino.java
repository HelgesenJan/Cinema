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

    public void leggTilKinosal(Kinosal sal) {
        kinosaler.add(sal);
    }

    public String getKinonavn() {
        return kinonavn;
    }

    @Override
    public int compareTo(Kino o) {
        return kollator.compare(this.kinonavn, o.getKinonavn());
    }
}
