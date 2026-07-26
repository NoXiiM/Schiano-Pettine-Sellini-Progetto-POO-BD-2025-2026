package controller.gestionale;
import model.gestionale.utenteEFigli.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import database.implementazioneDAO.*;


public class WelcomeController {

    private Utente currentUser;
    private ArrayList<String> usernames;


    public WelcomeController() {
        usernames= new ArrayList<>();
    }

    public WelcomeController(Utente currentUser, ArrayList<String> usernames) {
        this.currentUser = currentUser;
        this.usernames= usernames;
    }

    //client
    public void registrazione(String username, String nome, String cognome, String codiceFiscale,
                           LocalDate dataNascita, String password, int importo, String tipoRegistrazione) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");

        if(tipoRegistrazione.equals("cliente")){
            if (importo < 50) throw new RuntimeException("Deposito minimo obbligatorio di 50 euro");
        }

        //check locale
        for (String user : usernames) {
            if (username.equals(user)){
                throw new RuntimeException("Username non disponibile");
            }
        }

        String codiceTessera = generaCodiceTessera(username);

        try {
            new ImpDAOop().registrazione(codiceTessera, username, nome, cognome, codiceFiscale,
                    dataNascita, password);
        } catch (SQLException e) {
            aggiornaUsernames();
            throw new RuntimeException(e);
        }
        pulisciUsernames();
    }

    public void login(String username, String password) throws RuntimeException, SQLException{
        if(username.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        ImpDAOop db= new ImpDAOop();
        String tipo;

        tipo= db.trovaTabella(username, password);

        String[] tipologia= new String[1];
        LocalDate[] dataDiNascita= new LocalDate[1];
        String[] nome= new String[1];
        String[] cognome= new String[1];
        String[] codiceFiscale= new String[1];
        String[] identificativo= new String[1];

        if(tipo == null) throw new RuntimeException("Credenziali errate!");

        if(tipo.equals("Cliente")){

            int[] saldo= new int[1];
            double[] scontoPercentuale= new double[1];
            LocalDate[] dataDiBan= new LocalDate[1];
            String[] motiviBan = new String[1];
            long[] tempoDiGioco = new long[1];
            int[] fichesGiocate = new int[1];
            double[] vincitaPercentualeTot = new double[1];
            int[] partiteGiocate = new int[1];
            boolean[] sospetto = new boolean[1];


            db.loginCliente(identificativo, saldo, tempoDiGioco, fichesGiocate, vincitaPercentualeTot,
                    partiteGiocate, tipologia, scontoPercentuale, sospetto, dataDiBan,
                    motiviBan, nome, cognome, codiceFiscale, dataDiNascita, username, password);

            boolean flag = tipologia[0].equals("Premium");

            currentUser= new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                    identificativo[0], flag, scontoPercentuale[0], sospetto[0], tempoDiGioco[0], fichesGiocate[0],
                    saldo[0], partiteGiocate[0], dataDiBan[0], motiviBan[0]);

        } else if(tipo.equals("Dipendente")){

                db.loginDipendente(identificativo, nome, cognome, codiceFiscale, dataDiNascita, tipologia, username, password);

                if(tipologia[0].equals("Dealer")){
                    currentUser= new Dealer(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);


                } else{
                    currentUser= new Supervisore(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);
                }
        }
    }

    public boolean changePass(String oldPass, String newPass1, String newPass2) throws RuntimeException, SQLException{
        if(oldPass.isBlank() || newPass1.isBlank() || newPass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!newPass1.equals(newPass2)) throw new RuntimeException("Le password non coincidono");

        ImpDAOopc db1 = new ImpDAOopc();
        ImpDAOop db2 = new ImpDAOop();

        if(currentUser.getPassword().equals(oldPass) && (db2.trovaTabella(currentUser.getUsername(), oldPass) != null)) {
            currentUser.setPassword(newPass1);
            db1.cambioPassword(newPass1, currentUser.getUsername());

            return true;
        }
        else return false;
    }

    public String getUserUtente(){
        return currentUser.getUsername();
    }

    public Utente getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUserNull(){
        currentUser= null;
    }

    public boolean isUtenteACliente(){
        if(currentUser instanceof Cliente) return true;
        return false;
    }

    public ArrayList<String> getUsernamesList() {
        return usernames;
    }

    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    public void pulisciUsernames() {
        usernames.clear();
    }

    public void aggiornaUsernames() {

        ImpDAOop db = new ImpDAOop();

        try {
            db.usernameUtenti(usernames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
