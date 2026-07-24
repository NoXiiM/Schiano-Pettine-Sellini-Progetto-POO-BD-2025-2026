package controller.gestionale;
import model.gestionale.utenteEFigli.*;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.time.LocalDate;
import java.util.ArrayList;

import database.implementazioneDAO.*;


public class WelcomeController {

    private Utente currentUser;
    private ArrayList<String> usernames;

    private ArrayList<Utente> lista_utenti;

    public WelcomeController() {
        usernames= new ArrayList<>();
    }

    public WelcomeController(Utente currentUser, ArrayList<String> usernames) {
        this.currentUser = currentUser;
        this.usernames= usernames;
    }

    public void login(String username, String password) throws RuntimeException{
        if(username.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        impDAOop db= new impDAOop();
        String tipo;

        try{
            tipo= db.trovaTabella(username, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String[] tipologia= new String[1];
        LocalDate[] dataDiNascita= new LocalDate[1];
        String[] nome= new String[1];
        String[] cognome= new String[1];
        String[] codiceFiscale= new String[1];
        String[] identificativo= new String[1];

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

            try{
                db.loginCliente(identificativo, saldo, tempoDiGioco, fichesGiocate, vincitaPercentualeTot,
                        partiteGiocate, tipologia, scontoPercentuale, sospetto, dataDiBan,
                        motiviBan, nome, cognome, codiceFiscale, dataDiNascita, username, password);
                boolean flag = tipologia[0].equals("Premium");
                currentUser= new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                        identificativo[0], flag, scontoPercentuale[0], sospetto[0], tempoDiGioco[0], fichesGiocate[0],
                        saldo[0], partiteGiocate[0], dataDiBan[0], motiviBan[0]);
                return;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if(tipo.equals("Dipendente")){

            try{
                db.loginDipendente(identificativo, nome, cognome, codiceFiscale, dataDiNascita, tipologia, username, password);

                if(tipologia[0].equals("Dealer")){
                    currentUser= new Dealer(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);


                } else{
                    currentUser= new Supervisore(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);
                }
                return;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Credenziali errate!");
    }

    public boolean changePass(String oldPass, String newPass1, String newPass2) throws RuntimeException{
        if(oldPass.isBlank() || newPass1.isBlank() || newPass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!newPass1.equals(newPass2)) throw new RuntimeException("Le password non coincidono");

        if(currentUser.getPassword().equals(oldPass)) return true;
        else return false;
    }

    public boolean changeUsername(String newUser, String pass1, String pass2) throws RuntimeException{
        if(newUser.isBlank() || pass1.isBlank() || pass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!pass1.equals(pass2)) throw new RuntimeException("Le password non coincidono");

        impDAOop db_fetch_user= new impDAOop();

        impDAOopc db= new impDAOopc();
        ArrayList<String> usernames= new ArrayList<>();

        try{
            db_fetch_user.usernameUtenti(usernames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(String i : usernames){
            if(i.equals(newUser)) throw new RuntimeException();
        }

        try{
            db.cambioUsername(currentUser.);
        }
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

    public ArrayList<Utente> getLista_utenti(){
        return lista_utenti;
    }

    public boolean utenteCliente(){
        if(currentUser instanceof Cliente) return true;
        return false;
    }

    public ArrayList<String> getUsernamesList() {
        return usernames;
    }
}
