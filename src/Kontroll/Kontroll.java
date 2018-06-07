package Kontroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Kontroll {

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
    private ArrayList<Kinosal> kinosaler = new ArrayList<>();
    private ArrayList<Bruker> brukere = new ArrayList<>();
    private ArrayList<Film> filmer = new ArrayList<>();
    private ArrayList<Visning> visninger = new ArrayList<Visning>();



    public Kontroll() {

        // PLACEHOLDER LISTER FOR TESTDATA
        ArrayList<Film> filmer = new ArrayList<Film>();
        filmer.add(new Film(1,"Rosen"));
        filmer.add(new Film(2,"Tulipanen"));
        filmer.add(new Film(3,"Solsikken"));

        ArrayList<Kinosal> kinosaler = new ArrayList<Kinosal>();
        ArrayList<Visning> visninger = new ArrayList<Visning>();

    }


    public Kinosal finnKinosal(int kinosalnr) {
        Kinosal dummy = new Kinosal(kinosalnr);
        int indeks = Collections.binarySearch(kinosaler,dummy);
        return kinosaler.get(indeks);
    }


    public void lastDatabase() throws SQLException {

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

       //Hent ut kinosal result set
       ResultSet kinosaler =  runDBQuery("SELECT k_kinosalnr, k_kinonavn, k_kinosalnavn FROM tblkinosal");

       //Loop gjennom kinosal resultatet
       while (kinosaler.next()) {

           //Opprett kinosal objekt
           int salnr = kinosaler.getInt("k_kinosalnr");
           String kinonavn = kinosaler.getString("k_kinonavn");
           String kinosalnavn = kinosaler.getString("k_kinosalnavn");

           Kinosal sal = new Kinosal(salnr, kinonavn, kinosalnavn);

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

    public static Kontroll getInstance() {
        if(INSTANSE == null) INSTANSE = new Kontroll(); //Opprett ny instanse
        return INSTANSE;
    }

    /**
     * Legger til en Film i Kontroll sin ArrayListe over Filmer
     * @param filmnr
     * @param filmNavn
     */
    public void leggTilFilm(int filmnr, String filmNavn) {
        filmer.add(new Film(filmnr, filmNavn));
    }

    public void leggTilKino(int kinosalnr, String salnr, String kinonavn) {
        // kinosaler.add(new Kinosal(k))
        //
    }


}
