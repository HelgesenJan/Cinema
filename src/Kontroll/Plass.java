package Kontroll;

public class Plass {

    private int radnr;
    private int setenr;
    private Kinosal kinosal;

    public Plass(int radnr, int setenr, Kinosal kinosal) {
        this.radnr = radnr;
        this.setenr = setenr;
        this.kinosal = kinosal;
    }

    public int getRadnr() {
        return radnr;
    }

    public int getSetenr() {
        return setenr;
    }

    public Kinosal getKinosal() {
        return kinosal;
    }
}
