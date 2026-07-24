package controller.gestionale;
import model.gestionale.utenteEFigli.*;

import java.sql.SQLException;
import java.time.LocalDate;
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

    public void login(String username, String password) throws RuntimeException, SQLException{
        if(username.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        impDAOop db= new impDAOop();
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
            return;

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
    }

    public boolean changePass(String oldPass, String newPass1, String newPass2) throws RuntimeException, SQLException{
        if(oldPass.isBlank() || newPass1.isBlank() || newPass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!newPass1.equals(newPass2)) throw new RuntimeException("Le password non coincidono");

        impDAOopc db1 = new impDAOopc();
        impDAOop db2 = new impDAOop();

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

    public boolean utenteCliente(){
        if(currentUser instanceof Cliente) return true;
        return false;
    }

    public ArrayList<String> getUsernamesList() {
        return usernames;
    }
}
