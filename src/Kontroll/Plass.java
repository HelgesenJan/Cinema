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

    /**
     * Returner radnr
     * @return int
     */

    public int getRadnr() {
        return radnr;
    }

    /**
     * Returner setenr
     * @return
     */

    public int getSetenr() {
        return setenr;
    }

    /**
     * Hent kinosal
     * @return Kinosal
     */

    public Kinosal getKinosal() {
        return kinosal;
    }

    /**
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plass plass = (Plass) o;

        if (getRadnr() != plass.getRadnr()) return false;
        if (getSetenr() != plass.getSetenr()) return false;
        return getKinosal() != null ? getKinosal().equals(plass.getKinosal()) : plass.getKinosal() == null;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        int result = getRadnr();
        result = 31 * result + getSetenr();
        result = 31 * result + (getKinosal() != null ? getKinosal().hashCode() : 0);
        return result;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Plass{" +
                "radnr=" + radnr +
                ", setenr=" + setenr +
                '}';
    }
}
