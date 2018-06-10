package Kontroll;

import java.text.Collator;
import java.util.ArrayList;

public class Billett implements Comparable<Billett>{

    private final static Collator kollator = Collator.getInstance();

    private String billettkode;
    private Visning visning;
    private boolean erBetalt;

    private ArrayList<Plass> plasser = new ArrayList<>();

    /**
     * Konstruktør for dummy Billett, brukes i binærsøk
     * @param billettkode
     */
    public Billett(String billettkode) {
        this.billettkode = billettkode;
    }

    /**
     * Ordinær konstruktør
     * @param billettkode
     * @param visning
     * @param erBetalt
     */

    public Billett(String billettkode, Visning visning, boolean erBetalt) {
        this.billettkode = billettkode;
        this.visning = visning;
        this.erBetalt = erBetalt;
    }

    /**
     * Hent antall plasser
     * @return int
     */

    public int getAntallPlasser() {
        return plasser.size();
    }

    /**
     * Legger til en Plass i Billett sin ArrayList over Plasser
     * @param plassen
     */
    public void leggTilPlass(Plass plassen) {
        plasser.add(plassen);
    }

    /**
     * Hent billettkode
     * @return String
     */

    public String getBillettkode() {
        return billettkode;
    }

    /**
     * Hent visning
     * @return Visning
     */
    public Visning getVisning() {
        return visning;
    }

    /**
     * Returner sann dersom billett er betalt
     * @return boolean
     */
    public boolean erBetalt() {
        return erBetalt;
    }

    /**
     * Oppdater betalingsstatus
     * @param erBetalt
     */
    public void setErBetalt(boolean erBetalt) {
        this.erBetalt = erBetalt;
    }

    /**
     * Hent liste med plasser
     * @return ArrayList<Plass>
     */

    public ArrayList<Plass> getPlasser() {
        return this.plasser;
    }

    /**
     * @return String
     */

    @Override
    public String toString() {
        return "Billett{" +
                "billettkode='" + billettkode + '\'' +
                ", visning=" + visning +
                ", erBetalt=" + erBetalt +
                ", plasser=" + plasser +
                '}';
    }

    @Override
    public int compareTo(Billett o) {
        return kollator.compare(this.billettkode, o.getBillettkode());
    }
}
