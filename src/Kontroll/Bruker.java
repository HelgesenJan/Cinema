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

    public boolean riktigPin(String pin) {
        if(this.pin.equals(pin)) {
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

    public String getPin() {
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
