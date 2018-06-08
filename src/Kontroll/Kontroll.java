package Kontroll;

import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.util.Collections.sort;

public class Kontroll {

    public static int sortering = 0;

    //Singleton
    private static  Kontroll INSTANSE = null;

    //Database konfigurasjon
    private String db_navn = "kino";
    private String db_bruker = "Case";
    private String db_passord = "Esac";

    //Database objekter
    private Connection db;
    private ResultSet resultat;
    private Statement stmt;

    //Lister med objekter
    private  ArrayList<Kino> kinoer = new ArrayList<>();
    private ArrayList<Kinosal> kinosaler = new ArrayList<>();
    private ArrayList<Film> filmer = new ArrayList<>();
    private ArrayList<Bruker> brukere = new ArrayList<>();
    private ArrayList<Visning> visninger = new ArrayList<>();
    private ArrayList<Billett> billetter = new ArrayList<>();


    public Kontroll() throws SQLException {
        lastDatabase();

        Kino kino = finnKino("Tiara");
        System.out.println(kino);
        sortering = 1;
        ArrayList<Visning> visninger = filtrerVisninger(kino);
        System.out.println(visninger.size());
        for(Visning v : visninger) {
            System.out.println(v.toString());
        }
    }


    public Kino finnKino(String kinonavn) {
        Kino dummy = new Kino(kinonavn);
        int indeks = Collections.binarySearch(kinoer, dummy);
        return kinoer.get(indeks);
    }

    public Kinosal finnKinosal(int kinosalnr) {
        Kinosal dummy = new Kinosal(kinosalnr);
        int indeks = Collections.binarySearch(kinosaler,dummy);
        return kinosaler.get(indeks);
    }

    public Film finnFilm(int filmnr) {
        Film dummy =new Film(filmnr);
        int indeks = Collections.binarySearch(filmer, dummy);
        return filmer.get(indeks);
    }

    public Visning finnVisning(int visningsnr) {
        Visning dummy = new Visning(visningsnr);
        int indeks = Collections.binarySearch(visninger, dummy);
        return visninger.get(indeks);
    }

    public Billett finnBillett(String billettkode) {
        Billett dummy = new Billett(billettkode);
        int indeks = Collections.binarySearch(billetter, dummy);
        if(indeks < 0) {
            return null;
        }
        return billetter.get(indeks);
    }

    public ArrayList<Visning> filtrerVisninger(Kino kino) {
        ArrayList<Visning> visninger = new ArrayList<>();

        Iterator itr = this.visninger.iterator();
        while (itr.hasNext()) {
            Visning visning = (Visning) itr.next();

            if(visning.getKinosal().getKino().getKinonavn().equals(kino.getKinonavn())) {
                visninger.add(visning);
            }
        }

       Collections.sort(visninger);
       return visninger;
    }



    public void lastDatabase() throws SQLException {

        sortering = -1;
        //Opprett forbindelse til database
        opprettDBForbindelse();

        //Hent ut brukere
        ResultSet brukere =  runDBQuery("SELECT l_brukernavn, l_pinkode, l_erPlanlegger FROM tbllogin");
        System.out.println("test");
        while(brukere.next()) {
            String brukernavn = brukere.getString("l_brukernavn");
            int pinkode = brukere.getInt("l_pinkode");
            boolean erPlanlegger = brukere.getBoolean("l_erPlanlegger");

            Bruker bruker = new Bruker(brukernavn,pinkode,erPlanlegger);
            System.out.println(bruker.toString());
            this.brukere.add(bruker);
        }

       //Hent ut kinoer
       /*

        */

        ResultSet kinoer = runDBQuery("SELECT k_kinonavn FROM kino.tblkinosal GROUP BY k_kinonavn");

        while (kinoer.next()) {
            String kinonavn = kinoer.getString("k_kinonavn");
            Kino kino = new Kino(kinonavn);
            System.out.println(kino.toString());
            this.kinoer.add(kino);
        }

       //Hent ut kinosal result set
       ResultSet kinosaler =  runDBQuery("SELECT k_kinosalnr, k_kinonavn, k_kinosalnavn FROM tblkinosal");

       //Loop gjennom kinosal resultatet
       while (kinosaler.next()) {

           //Opprett kinosal objekt
           int salnr = kinosaler.getInt("k_kinosalnr");
           String kinonavn = kinosaler.getString("k_kinonavn");
           String kinosalnavn = kinosaler.getString("k_kinosalnavn");

           Kino kino = finnKino(kinonavn);
           Kinosal sal = new Kinosal(salnr, kino, kinosalnavn);
           kino.leggTilKinosal(sal);
           //Legg til sal
           this.kinosaler.add(sal);
       }

       //Hent ut plasser til kinosaler
       ResultSet plasser =  runDBQuery("SELECT p_radnr, p_setenr, p_kinosalnr FROM tblplass");

       while(plasser.next()) {
           int radnr = plasser.getInt("p_radnr");
           int setenr = plasser.getInt("p_setenr");
           int kinosalnr = plasser.getInt("p_kinosalnr");

           Kinosal sal = finnKinosal(kinosalnr);
           sal.leggTilPlass(radnr, setenr);
       }

       //Hent ut filmene
        ResultSet filmer =  runDBQuery("SELECT f_filmnr, f_filmnavn FROM tblfilm");
        while (filmer.next()) {

            int filmnr = filmer.getInt("f_filmnr");
            String filmnavn = filmer.getString("f_filmnavn");

            Film film = new Film(filmnr, filmnavn);
            System.out.println(film.toString());
            this.filmer.add(film);
        }

        //Hent ut visningene
        ResultSet visninger =  runDBQuery("SELECT v_visningnr, v_filmnr,v_kinosalnr, v_dato,v_starttid, v_pris  FROM tblvisning");

        while(visninger.next()) {
            int visningsnr = visninger.getInt("v_visningnr");
            int filmnr = visninger.getInt("v_filmnr");
            int kinosalnr = visninger.getInt("v_kinosalnr");
            Date dato = visninger.getDate("v_dato");
            Time starttid = visninger.getTime("v_starttid");
            double pris = visninger.getDouble("v_pris");

            Film film = finnFilm(filmnr);
            Kinosal kinosal = finnKinosal(kinosalnr);
            Visning visning = new Visning(visningsnr, film, kinosal, dato,starttid,pris);

            film.leggTilVisning(visning);
            this.visninger.add(visning);
        }

        //Hent ut billetter
        ResultSet billetter =  runDBQuery("SELECT b_billettkode, b_visningsnr, b_erBetalt  FROM tblbillett");

        while(billetter.next()) {
            String billettkode = billetter.getString("b_billettkode");
            int visningsnr = billetter.getInt("b_visningsnr");
            boolean erBetalt = billetter.getBoolean("b_erBetalt");

            Visning visning = finnVisning(visningsnr);
            Billett billett = new Billett(billettkode,visning,erBetalt);
            System.out.println(billett.toString());
            visning.leggTilBillett(billett);
            this.billetter.add(billett);
        }

        //Hent ut plassene til billettene
        ResultSet plassbillett =  runDBQuery("SELECT pb_radnr, pb_setenr, pb_kinosalnr, pb_billettkode  FROM tblplassbillett");

        while (plassbillett.next()) {
            int radnr = plassbillett.getInt("pb_radnr");
            int setenr = plassbillett.getInt("pb_setenr");
            int kinosalnr = plassbillett.getInt("pb_kinosalnr");
            String billettkode = plassbillett.getString("pb_billettkode");

            Kinosal kinosal = finnKinosal(kinosalnr);
            Billett billett  = finnBillett(billettkode);
            Plass plass = kinosal.finnPlass(radnr, setenr);
            //Legg til plass
            billett.leggTilPlass(plass);
        }
        sortering = 0;
    }

    public void slettPlass(int verdi) {
        try {
            for (int i = 0; i < filmer.size(); i++){
                filmer.remove(verdi);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Kan ikke endre film");
        }

    }



    public ResultSet runDBQuery(String sql) {
        resultat = null; //Nullstill resultat
        try {
            //Kjør spørring
            stmt = db.createStatement();
            return  stmt.executeQuery(sql);
        } catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    public ArrayList<Kino> getKinoer() {
        return kinoer;
    }

    public ArrayList<Kinosal> getKinosaler() {
        return kinosaler;
    }

    public ArrayList<Film> getFilmer() {
        return filmer;
    }

    public ArrayList<Bruker> getBrukere() {
        return brukere;
    }

    public ArrayList<Visning> getVisninger() {
        return visninger;
    }

    public ArrayList<Billett> getBilletter() {
        return billetter;
    }



    public void opprettDBForbindelse() {

        try {
            db = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db_navn,db_bruker,db_passord);
            //Set autocommit false, slik at vi kan commite manuelt når alle spørringene er utført
            db.setAutoCommit(false);
            System.out.println("Dabase er koblet til!");
        } catch(Exception e) {
            //Klarte ikke å åpne
            System.out.println(e); //debug

        }
    }

    public static Kontroll getInstance() throws SQLException {
        if(INSTANSE == null) INSTANSE = new Kontroll(); //Opprett ny instanse
        return INSTANSE;
    }

    /**
     * Lager en Object-liste over Visninger, som skal vises i tabellen for billettbestilling
     * @return
     */
    public Object[][] lagVisningTabellListe() {
        int rader = visninger.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][5];
        for(int i=0; i<visninger.size(); i++) {
            tabellInnhold[teller][0] = visninger.get(i).getFilm().getFilmnavn();
            tabellInnhold[teller][1] = (visninger.get(i).getDato() + ", " + visninger.get(i).getStartTid());
            tabellInnhold[teller][2] = visninger.get(i).getKinosal().getKinosalnavn();
            tabellInnhold[teller][3] = visninger.get(i).getPris();
            tabellInnhold[teller][4] = visninger.get(i).getVisningsNr();
            teller++;
        }

        return tabellInnhold;
    }


    /**
     * Lager en Object-liste over filmer i visninger, som skal vises i tabellen for filmer i rapportering
     * @return
     */
    public Object[][] lagFilmTabellListe() {
        int rader = visninger.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][1];
        for(int i=0; i<visninger.size(); i++) {
            tabellInnhold[teller][0] = visninger.get(i).getFilm().getFilmnavn();
            teller++;
        }

        return tabellInnhold;
    }

    /**
     * Lager en Object-liste over statistikker i rapporter
     */

    public Object[][] lagKinosalTabellListe() {
        int rader = kinosaler.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][1];
        for(int i=0; i<kinosaler.size(); i++) {
            tabellInnhold[teller][0] = kinosaler.get(i).getKinosalnavn();
            teller++;
        }

        return tabellInnhold;
    }

    /**
     * Genererer en tilfeldig billettkode
     * @return
     */
    public String genererBillettkode() {
        // Bruker et random-objekt for å trekke en tilfeldig posisjon fra bokstaver- og siffer-listene.
        // Bygger dermed en billettkode bestående av fire karakterer og to sifre

        char[] bokstaver = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] siffer = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        Random tilfeldigPosisjon = new Random();
        String billettKode = "";

        for(int b=0; b<4; b++) {
            int posisjon = tilfeldigPosisjon.nextInt(25) + 0;
            billettKode = billettKode + bokstaver[posisjon];
        }

        for(int s=0; s<2; s++) {
            int posisjon = tilfeldigPosisjon.nextInt(9) + 0;
            billettKode = billettKode + siffer[posisjon];
        }

        return billettKode;
    }




    /**
     * Legger til en Film i Kontroll sin ArrayListe over Filmer
     * @param filmnr
     * @param filmNavn
     */
    public void leggTilFilm(int filmnr, String filmNavn) {
        filmer.add(new Film(filmnr, filmNavn));
    }

    /**
     * Legger til en Kinoasal i Kontroll sin ArrayList over Kinosaler
     * @param kinosalnr
     * @param kinosalnavn
     * @param kinonavn
     */
    //public void leggTilKinosal(int kinosalnr, Kontroll.Kino kinosalnavn, String kinonavn) {
      //  kinosaler.add(new Kinosal(kinosalnr, kinosalnavn, kinonavn));

    //}


    /**
     * Legger til en Bruker i Kontroll sin ArrayList over brukere
     * @param brukernavn
     * @param pin
     * @param erPlanlegger
     */
    public void leggTilBruker(String brukernavn, int pin, boolean erPlanlegger) {
        brukere.add(new Bruker(brukernavn, pin, erPlanlegger));
    }


}
