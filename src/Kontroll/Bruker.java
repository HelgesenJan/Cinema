package Kontroll;

public class Bruker {

    private String brukernavn;
    private String pin;
    private boolean erPlanlegger;

    public Bruker(String brukernavn, String pin, boolean erPlanlegger) {
        this.brukernavn = brukernavn;
        this.pin = pin;
        this.erPlanlegger = erPlanlegger;
    }

    /**
     * Sjekk om pin til denne bruker er riktig
     * @param pin
     * @return boolean
     */

    public boolean riktigPin(String pin) {
        if(this.pin.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sann dersom planlegger
     * @return boolean
     */
    public boolean erPlanlegger() {
        return erPlanlegger;
    }

    /**
     * Hent brukernavn
     * @return String
     */

    public String getBrukernavn() {
        return brukernavn;
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Bruker{" +
                "brukernavn='" + brukernavn + '\'' +
                ", pin=" + pin +
                ", erPlanlegger=" + erPlanlegger +
                '}';
    }
}
