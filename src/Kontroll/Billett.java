package Kontroll;

import java.util.ArrayList;

public class Billett {

    private int billettkode;
    private Visning visning;
    private boolean erBetalt;

    private ArrayList<Plass> plasser = new ArrayList<>();

    public Billett(int billettkode, Visning visning, boolean erBetalt) {
        this.billettkode = billettkode;
        this.visning = visning;
        this.erBetalt = erBetalt;
    }

    /**
     * Legger til en Plass i Billett sin ArrayList over Plasser
     * @param plassen
     */
    public void leggTilPlass(Plass plassen) {
        plasser.add(plassen);
    }

    public int getBillettkode() {
        return billettkode;
    }

    public void setBillettkode(int billettkode) {
        this.billettkode = billettkode;
    }

    public Visning getVisning() {
        return visning;
    }

    public void setVisning(Visning visning) {
        this.visning = visning;
    }

    public boolean isErBetalt() {
        return erBetalt;
    }

    public void setErBetalt(boolean erBetalt) {
        this.erBetalt = erBetalt;
    }
}
