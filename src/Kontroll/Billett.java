package Kontroll;

import java.text.Collator;
import java.util.ArrayList;

public class Billett implements Comparable<Billett>{

    private final static Collator kollator = Collator.getInstance();

    private String billettkode;
    private Visning visning;
    private boolean erBetalt;

    private ArrayList<Plass> plasser = new ArrayList<>();

    public Billett(String billettkode) {
        this.billettkode = billettkode;
    }

    public Billett(String billettkode, Visning visning, boolean erBetalt) {
        this.billettkode = billettkode;
        this.visning = visning;
        this.erBetalt = erBetalt;
    }

    public int getAntallPlasser() {
        /*int antall=0;
        for(int i=0; i<plasser.size(); i++) {
            antall++;
        }
        return antall;*/
        return plasser.size();
    }

    /**
     * Legger til en Plass i Billett sin ArrayList over Plasser
     * @param plassen
     */
    public void leggTilPlass(Plass plassen) {
        plasser.add(plassen);
    }

    public boolean harPlass(Plass plass) {
        for (Plass p:this.plasser) {
            if(p.equals(plass)) {
                return true;
            }
        }
        return false;
    }

    public String getBillettkode() {
        return billettkode;
    }


    public void setBillettkode(String billettkode) {
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

    public ArrayList<Plass> getPlasser() {
        return this.plasser;
    }

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
