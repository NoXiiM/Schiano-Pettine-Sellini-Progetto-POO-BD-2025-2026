package controller.gestionale;
import model.gestionale.utenteEFigli.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

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
                    saldo[0], partiteGiocate[0], vincitaPercentualeTot[0], dataDiBan[0], motiviBan[0]);

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

        ImpDAOop db = new ImpDAOop();

        String ruolo = (db.trovaTabella(currentUser.getUsername(), oldPass));

        if(currentUser.getPassword().equals(oldPass) && ruolo!=null) {
            currentUser.setPassword(newPass1);
            db.cambioPassword(newPass1, currentUser.getUsername(),ruolo);

            return true;
        }
         else return false;
    }

    public void resetPass(String nome, String cognome, String username) throws RuntimeException, SQLException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        ImpDAOop db = new ImpDAOop();

        if(! db.passwordDimenticata(nome, cognome, username)){
            throw new RuntimeException("Credenziali errate");
        }
        if(currentUser!=null)currentUser.setPassword("P@ssw0rd!"); //Se la password non la ricordiamo in accesso


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
        return (currentUser instanceof Cliente);
    }

    public boolean isUtenteADealer(){
        return(currentUser != null && currentUser instanceof Dealer);
    }

    public ArrayList<String> getUsernamesList() {
        return usernames;
    }

    public void aggiornaUsernames() {

        ImpDAOop db = new ImpDAOop();

        try {
            db.usernameUtenti(usernames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void pulisciUsernames() {
        usernames.clear();
    }

    public String generaCodiceTessera(String username)
    {
        Random random = new Random();
        String numero = String.format("%03d", random.nextInt(0, 1000));
        int taglio = random.nextInt(0, username.length());

        String prefissoDipendente= "";

        if(currentUser instanceof Dealer) prefissoDipendente= "DE";
        if(currentUser instanceof Supervisore) prefissoDipendente= "SU";

        return prefissoDipendente + username.substring(0, taglio) + numero + username.substring(taglio);
    }
}
