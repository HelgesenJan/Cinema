package Kontroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
        kinosaler.add(new Kinosal(k))

    }


}
