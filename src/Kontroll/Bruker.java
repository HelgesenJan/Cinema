package Kontroll;

public class Bruker {

    private String brukernavn;
    private int pin;
    private boolean erPlanlegger;

    public Bruker(String brukernavn, int pin, boolean erPlanlegger) {
        this.brukernavn = brukernavn;
        this.pin = pin;
        this.erPlanlegger = erPlanlegger;
    }

    public boolean passordRiktig(String brukernavn, int pin) {
        if(this.brukernavn == brukernavn && this.pin == pin) {
            return true;
        } else {
            return false;
        }
    }

    public boolean erPlanlegger() {
        return erPlanlegger;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public int getPin() {
        return pin;
    }

    public boolean isErPlanlegger() {
        return erPlanlegger;
    }

    @Override
    public String toString() {
        return "Bruker{" +
                "brukernavn='" + brukernavn + '\'' +
                ", pin=" + pin +
                ", erPlanlegger=" + erPlanlegger +
                '}';
    }
}
