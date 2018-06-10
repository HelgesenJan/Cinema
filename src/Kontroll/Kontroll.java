package Kontroll;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Kontroll {

    // Enum. Soterer etter tid eller alfabetisk. Hvis den er en verdi, så sorterer den etter nummeret
    public static Sortering sortering = Sortering.VERDI;

    //Singleton
    private static Kontroll INSTANSE = null;

    //Database konfigurasjon
    private String db_navn = "kino";
    private String db_bruker = "Case";
    private String db_passord = "Esac";

    //Database objekter
    private Connection db;
    private ResultSet resultat;
    private Statement stmt;

    //ArrayLister med objekter
    private ArrayList<Kino> kinoer = new ArrayList<>();
    private ArrayList<Kinosal> kinosaler = new ArrayList<>();
    private ArrayList<Film> filmer = new ArrayList<>();
    private ArrayList<Bruker> brukere = new ArrayList<>();
    private ArrayList<Visning> visninger = new ArrayList<>();
    private ArrayList<Billett> billetter = new ArrayList<>();


    /**
     * Kontroll konstruktøren laster databasen og setter standard sortering (alfabetisk)
     * @throws SQLException
     */
    public Kontroll() throws SQLException {
        lastDatabase();
        sortering = Sortering.ALFABETISK;
    }

    /**
     * Genererer en tilfeldig billettkode
     * Sjekk om billettkoden er duplikat
     * @return String
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


        //Sjekk om billettkode er i bruk
        //Se om man  finner en billet med den billetkoden som er generer
        Billett billett = finnBillett(billettKode);

        if(billett == null) {
            //Billettkoden finnes ikke, returner generert kode.
            return billettKode;
        } else {
            System.out.println("billettkode må lages på ny");
            //generer en ny billettkode fordi den allerede finnes
            return genererBillettkode();
        }
    }

    /**
     * Henter ut antall ubetalte billetter fra slettinger.dat
     * Her brukes leseForbindelse
     * Brukes i rapportdelen for å telle ubetalte billetter
     * @param visningen
     * @return
     */

    public int finnAntallUbetalteBilletter(Visning visningen) {
        int antall = 0;
        try {
            BufferedReader fil = leseForbindelse("slettinger.dat");
            String linje = fil.readLine();
            while(linje != null) {
                String[] split1 = linje.split(", film");
                String del1 = split1[0];
                String[] split2 = del1.split("visningsNr=");
                String del2 = split2[1];
                System.out.println(del2 + ", " + linje);
                int funnetNummer = Integer.parseInt(del2);

                if(visningen.getVisningsNr() == funnetNummer) {
                    antall++;
                }
                linje = fil.readLine();
            }
            return antall;
        } catch(IOException e) {
            return antall;
        }
    }

    /**
     * Fjerner ubetalte billetter og setter det inn i "slettinger.dat"
     * @param visning
     * @throws IOException
     */

    public void fjernUbetalteBilletter(Visning visning) throws IOException {

        /*
            Opprett filer "slettinger.dat" dersom den ikke finnes
         */

        BufferedWriter fil = new BufferedWriter(new FileWriter("slettinger.dat", true));

        ArrayList<Billett> fjernes = new ArrayList<Billett>();

        //Gå gjennom bilettlista og sjekk om de er betalt
        for(Billett billett: this.billetter) {
            if (!billett.erBetalt() && billett.getVisning().equals(visning)) {
                //visning.fjernBillett(billett);
                //billetter.remove(billett);
                fil.append( billett.toString());
                fil.newLine();

                fjernes.add(billett);
                System.out.println(billett.getBillettkode() + " er fjernet.");
            }
        }
        //Fjern billett(er)
        for(Billett billett: fjernes) {
            this.billetter.remove(billett);
            visning.fjernBillett(billett);
        }
        //Steng fil
        fil.close();
    }

    /**
     * Regner ut statistikk for en film.
     * @param i
     * @return Object[][]
     */
    public Object[][] statistikkFilm(int i) {
        int rader = filmer.get(i).getVisninger().size();
        Object[][] tabellInnhold = new Object[rader][4];

        int teller = 0;
        for(int n=0; n<filmer.get(i).getVisninger().size(); n++) {

            int antallBilletter = 0;
            int antallPlasser = 0;
            int kapasitet = filmer.get(i).getVisninger().get(n).getKinosal().getAntallPlasser();


            for(int a=0; a<filmer.get(i).getVisninger().get(n).getBilletter().size(); a++) {
                antallPlasser += filmer.get(i).getVisninger().get(n).getBilletter().get(a).getAntallPlasser();
                antallBilletter++;
            }

            int prosent = (antallPlasser*100) / kapasitet;

            tabellInnhold[teller][0] = antallBilletter + "/" + antallPlasser;
            tabellInnhold[teller][1] = prosent + "%";
            tabellInnhold[teller][2] = finnAntallUbetalteBilletter(filmer.get(i).getVisninger().get(n));
            tabellInnhold[teller][3] = filmer.get(i).getVisninger().get(n).getStartTid();
            teller++;
        }
        return tabellInnhold;
    }

    /**
     * Hent ut statistikk over kinosal
     * @param i
     * @return Object[][]
     */

    public Object[][] statistikkKinosal(int i) {
        int rader = visninger.size();
        Object[][] tabellInnhold = new Object[rader][2];
        int kapasitet = kinosaler.get(i).getPlasser().size();
        int teller = 0;

        for(int f=0; f<filmer.size(); f++) {
            boolean erVist = false;
            int antallVisninger = 0;
            double iProsent = 0;
            double kumulativProsent = 0;
            double totaltProsent = 0;
            double antallPlasser = 0;

            for(int v=0; v<filmer.get(f).getVisninger().size(); v++) {
                if(filmer.get(f).getVisninger().get(v).getKinosal().equals(kinosaler.get(i))) {
                    antallPlasser = 0;
                    erVist = true;
                    antallVisninger++;
                    System.out.println("Antall visninger for " + filmer.get(f).getFilmnavn() + " så langt: " + antallVisninger);

                    for (int b=0; b<filmer.get(f).getVisninger().get(v).getBilletter().size(); b++) {
                        antallPlasser += filmer.get(f).getVisninger().get(v).getBilletter().get(b).getAntallPlasser();

                    }

                    iProsent = antallPlasser/kapasitet;
                    kumulativProsent += iProsent;


                }
            }


            if(erVist) {
                kumulativProsent *= 100;
                totaltProsent = kumulativProsent / antallVisninger;
                System.out.println("Samlet prosent: " + kumulativProsent);
                tabellInnhold[teller][0] = filmer.get(f).getFilmnavn();
                tabellInnhold[teller][1] = String.format("%.0f", totaltProsent) + "%";
                teller++;
            }

        }
        return tabellInnhold;
    }

    /**
     * Lager en Object-liste over Visninger, som skal vises i tabellen for billettbestilling
     * @param kino
     * @param betjent
     * @return Object[][]
     */

    public Object[][] lagVisningTabellListe(Kino kino, boolean betjent) {
        ArrayList<Visning> visninger = filtrerVisninger(kino);
        int rader = visninger.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][5];
        for(int i=0; i<visninger.size(); i++) {

            Visning visning = visninger.get(i);
            if((visning.erhalvtimeFørStart() && !betjent) || (visning.erKommende() && betjent)) {
                tabellInnhold[teller][0] = visninger.get(i).getFilm().getFilmnavn();
                tabellInnhold[teller][1] = visninger.get(i).getStartTid();
                tabellInnhold[teller][2] = visninger.get(i).getKinosal().getKinosalnavn();
                tabellInnhold[teller][3] = visninger.get(i).getPris();
                tabellInnhold[teller][4] = visninger.get(i).getVisningsNr();
                teller++;
            }
        }

        return tabellInnhold;
    }

    /**
     * Lager en Object-liste over filmer i visninger, som skal vises i tabellen for filmer i rapportering
     * @return Object[][]
     */
    public Object[][] lagFilmTabellListe() {
        int rader = visninger.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][1];
        for(int i=0; i<filmer.size(); i++) {
            tabellInnhold[teller][0] = filmer.get(i).getFilmnavn();
            teller++;
        }

        return tabellInnhold;
    }

    /**
     * Lager en Object-liste over statistikker i rapporter
     * @return Object[][]
     */

    public Object[][] lagVisningerIkkeBestiltListe() {
        int rader = this.visninger.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][6];
        for(int i=0; i<visninger.size(); i++) {

            Visning visning = visninger.get(i);
            if(!visning.harBilletter()) {
                tabellInnhold[teller][0] = visning.getKinosal().getKino().getKinonavn();
                tabellInnhold[teller][1] = visning.getFilm().getFilmnavn();
                tabellInnhold[teller][2] = visning.getKinosal().getKinosalnavn();
                tabellInnhold[teller][3] = visning.getStartTid();
                tabellInnhold[teller][4] = visning.getPris();
                tabellInnhold[teller][5] = visning.getVisningsNr();
                teller++;
            }
        }
        return tabellInnhold;
    }

    /**
     * Returnerer en liste over kinosal og dens kino
     * @return Object[][]
     */

    public Object[][] lagKinosalKinoTabellListe() {
        int rader = kinosaler.size();
        int teller = 0;
        Object[][] tabellInnhold = new Object[rader][1];
        for(int i=0; i<kinosaler.size(); i++) {
            tabellInnhold[teller][0] = kinosaler.get(i).getKino().getKinonavn() + " - " + kinosaler.get(i).getKinosalnavn();
            teller++;
        }

        return tabellInnhold;
    }

    /**
     * Filtrer visninger,
     * Filtrerer etter kino og sorterer etter definert sortering
     * basert på "Sortering" attributtet (se Visning.compareTo)
     * @param kino
     * @return
     */

    public ArrayList<Visning> filtrerVisninger(Kino kino) {
        ArrayList<Visning> visninger = new ArrayList<>();
        Iterator itr = this.visninger.iterator();
        while (itr.hasNext()) {
            Visning visning = (Visning) itr.next();
            System.out.println(visning.toString());
            if(visning.getKinosal().getKino().getKinonavn().equals(kino.getKinonavn())) {
                visninger.add(visning);
            }
        }
        //Kjør sortering
        Collections.sort(visninger);
        return visninger;
    }

    /**
     * Opprett en nybillett
     * @param billett
     */

    public void nyBillett(Billett billett) {
        billett.getVisning().leggTilBillett(billett);
        this.billetter.add(billett);
    }

    /**
     * Opprett en ny visning
     * @param kinonavn
     * @param salnavn
     * @param pris
     * @param tittel
     * @param dagmnd
     * @param starttid
     * @throws ParseException
     */

    public void nyVisning(String kinonavn, String salnavn, Double pris, String tittel, String dagmnd, String starttid) throws ParseException {

        Kinosal sal = this.finnSal(kinonavn, salnavn);

        if(sal== null) {
            System.out.println("kukhode");
        }
        Film film = this.finnFilm(tittel);
        Date dato = this.lagDato(dagmnd, starttid);

        int visningsnr = this.visninger.size()+1;
        Visning visning = new Visning(visningsnr,film,sal,dato,pris);

        film.leggTilVisning(visning);
        this.visninger.add(visning);

    }

    /**
     * Finn en bruker ved å oppgi brukernavn
     * @param brukernavn
     * @return Bruker
     */

    public Bruker finnBruker(String brukernavn) {
        for (Bruker bruker:this.brukere) {
            if(bruker.getBrukernavn().equals(brukernavn)) {
                return bruker;
            }
        }
        return null;
    }

    /**
     * Finn en kino basert på kinonavn
     * @param kinonavn
     * @return Kino
     */

    public Kino finnKino(String kinonavn) {
        Collections.sort(kinoer);
        Kino dummy = new Kino(kinonavn);
        int indeks = Collections.binarySearch(kinoer, dummy);
        if(indeks < 0) {
            return null;
        }
        return kinoer.get(indeks);
    }

    /**
     * Finn en kinosal basert på kinosalnr
     * @param kinosalnr
     * @return Kinosal
     */

    public Kinosal finnKinosal(int kinosalnr) {
        Collections.sort(kinosaler);
        Kinosal dummy = new Kinosal(kinosalnr);
        int indeks = Collections.binarySearch(kinosaler,dummy);
        if(indeks < 0) {
            return null;
        }
        return kinosaler.get(indeks);
    }

    /**
     * Finn en film basert på filmnr
     * @param filmnr
     * @return Film
     */

    public Film finnFilm(int filmnr) {
        Collections.sort(filmer);
        Film dummy =new Film(filmnr);
        int indeks = Collections.binarySearch(filmer, dummy);
        if(indeks < 0) {
            return null;
        }
        return filmer.get(indeks);
    }

    /**
     * Finn en film basert på filmnavn
     * @param filmnavn
     * @return Film
     */

    public Film finnFilm(String filmnavn) {
        Collections.sort(filmer);
        for (Film film:this.filmer) {
            if(film.getFilmnavn().equals(filmnavn)) {
                return film;
            }
        }
        return null;
    }

    /**
     * Finn en visning basert på visningsnr
     * @param visningsnr
     * @return Visning
     */

    public Visning finnVisning(int visningsnr) {

        Sortering gml_sort = this.sortering;
        this.sortering = Sortering.VERDI;

        Collections.sort(visninger);
        Visning dummy = new Visning(visningsnr);
        int indeks = Collections.binarySearch(visninger, dummy);

        //Sett gammel sortering
        this.sortering = gml_sort;

        if(indeks < 0) {
            return null;
        }
        return visninger.get(indeks);
    }

    /**
     * Finn en billett basert på billettkoden
     * @param billettkode
     * @return Billett
     */

    public Billett finnBillett(String billettkode) {
        Collections.sort(billetter);
        Billett dummy = new Billett(billettkode);
        int indeks = Collections.binarySearch(billetter, dummy);
        if(indeks < 0) {
            return null;
        }
        return billetter.get(indeks);
    }

    /**
     * Finn en sal ved å oppgi kinonavn og salnavn
     * Denne har vi fordi saler kan ha likt navn på flere kinoer
     * @param kinonavn
     * @param salnavn
     * @return Kinosal
     */
    public Kinosal finnSal(String kinonavn, String salnavn) {

        for(Kinosal sal:this.getKinosaler()) {
            if(sal.getKino().getKinonavn().equals(kinonavn)
                    && sal.getKinosalnavn().equals(salnavn)) {
                System.out.println("sann");
                return sal;
            }
        }
        return null;
    }

    /**
     * Hent en liste over  kinoer
     * @return ArrayList<Kino>
     */

    public ArrayList<Kino> getKinoer() {
        return kinoer;
    }

    /**
     * Hent en liste over kinosaler
     * @return ArrayList<Kinosal>
     */

    public ArrayList<Kinosal> getKinosaler() {
        return kinosaler;
    }

    /**
     * Hent en liste over filmer
     * @return ArrayList<Film>
     */

    public ArrayList<Film> getFilmer() {
        return filmer;
    }

    /**
     * Hent en liste over visninger
     * @return ArrayList<Visning>
     */

    public ArrayList<Visning> getVisninger() {
        return visninger;
    }

    /**
     * Opprett et dato objekt pasert på 2 strenger: dato og starttid(klokkeslett)
     * @param dato
     * @param startid
     * @return Date
     * @throws ParseException
     */
    public Date lagDato(String dato, String startid) throws ParseException {
        SimpleDateFormat datoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dato_str = dato + " " + startid + ":00";
        return datoFormat.parse(dato_str);
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
     * Henter en BufferedReader til en fil, for å kunne lese den.
     * @param filnavn
     * @return BufferedReader
     */

    public BufferedReader leseForbindelse(String filnavn) {
        try {
            FileReader filForbindelse = new FileReader(filnavn);
            BufferedReader leser = new BufferedReader(filForbindelse);
            return leser;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Last inn data fra tabellene i databasen, og putt dem inn i sine respektive arraylister
     * @throws SQLException
     */

    public void lastDatabase() throws SQLException {

        sortering = Sortering.VERDI;
        //Opprett forbindelse til database
        opprettDBForbindelse();

        //Hent ut brukere
        ResultSet brukere =  runDBQuery("SELECT l_brukernavn, l_pinkode, l_erPlanlegger FROM tbllogin");

        while(brukere.next()) {
            String brukernavn = brukere.getString("l_brukernavn");
            String pinkode = brukere.getString("l_pinkode");
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
            //Date dato = visninger.getDate("v_dato");
            double pris = visninger.getDouble("v_pris");

            //Bygg dato objekt for både dato og starttid
            String dato_str = visninger.getString("v_dato") + " " + visninger.getString("v_starttid");
            SimpleDateFormat datoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dato = null;

            try {
                dato = datoFormat.parse(dato_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Film film = finnFilm(filmnr);
            Kinosal kinosal = finnKinosal(kinosalnr);
            Visning visning = new Visning(visningsnr, film, kinosal, dato,pris);

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
        sortering = Sortering.TID;
        db.close();
    }

    /**
     * Lagre data fra ArrayLister til sine respektive tabeller i databasen.
     * @throws SQLException
     */

    public void lagreDatabase() throws SQLException {

        //Opprett DB forbindelse
        opprettDBForbindelse();

        //Forsøk å sette inn data i database
        try {
            //Kjør truncate på tabellene
            runDBEndring("SET FOREIGN_KEY_CHECKS=0");
            runDBEndring("TRUNCATE TABLE tblplassbillett");
            runDBEndring("TRUNCATE TABLE tblfilm");
            runDBEndring("TRUNCATE TABLE tblbillett");
            runDBEndring("TRUNCATE TABLE tblvisning");
            runDBEndring("SET FOREIGN_KEY_CHECKS=1");

            //Legg til filmer
            for (Film film : this.filmer) {
                runDBEndring("INSERT INTO tblfilm(f_filmnr, f_filmnavn) VALUES('" +
                        film.getFilmnr() + "', '" +
                        film.getFilmnavn() + "')");
            }

            //Legg til visning
            for (Visning visning : this.visninger) {
                runDBEndring("INSERT INTO tblvisning(v_visningnr, v_filmnr, v_kinosalnr, v_dato, v_starttid, v_pris) VALUES('" +
                        visning.getVisningsNr() + "','" +
                        visning.getFilm().getFilmnr() + "','" +
                        visning.getKinosal().getKinosalnr() + "','" +
                        visning.getDagMnd() + "','" +
                        visning.getStartKlokkeslett() + "','" +
                        visning.getPris() + "')");
            }

            //for billetter
            for (Billett billett : this.billetter) {
                runDBEndring("INSERT INTO tblbillett(b_billettkode, b_visningsnr, b_erBetalt) VALUES('" +
                        billett.getBillettkode() + "','" +
                        billett.getVisning().getVisningsNr() + "','" +
                        (billett.erBetalt() ? 1 : 0) + "')"); //Kommentar: erBetalt blir 1 og !erBetalt blir 0
                for (Plass plass : billett.getPlasser()) {
                    runDBEndring("INSERT INTO tblplassbillett(pb_radnr, pb_setenr, pb_kinosalnr, pb_billettkode) VALUES('" +
                            plass.getRadnr() + "','" +
                            plass.getSetenr() + "','" +
                            plass.getKinosal().getKinosalnr() + "','" +
                            billett.getBillettkode() + "')");
                }
            }
            //Kjør commit
            db.commit();
            db.close();
        }catch(SQLException e) {
            db.rollback();
            db.close();
            throw e;
        }
        //

    }

    /**
     * Kjøre en INSERT/UPDATE spørring i databasen
     * @param sql
     * @throws SQLException
     */

    public void runDBEndring(String sql) throws SQLException {
        //Kjør spørring
        stmt = db.createStatement();
        stmt.executeUpdate(sql);

    }

    /**
     * Kjør en SELECT spørring fra databasen
     * @param sql
     * @return
     */

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

    /**
     * Opprett database forbindelse
     * @throws SQLException
     */

    public void opprettDBForbindelse() throws SQLException {
        db = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db_navn,db_bruker,db_passord);
        //Set autocommit false, slik at vi kan commite manuelt når alle spørringene er utført
        db.setAutoCommit(false);
        System.out.println("Dabase er koblet til!");

    }

    /**
     * Hent en instans av denne klassen (Singleton)
     * @return
     * @throws SQLException
     */

    public static Kontroll getInstance() throws SQLException {
        if(INSTANSE == null) INSTANSE = new Kontroll(); //Opprett ny instanse
        return INSTANSE;
    }






}
