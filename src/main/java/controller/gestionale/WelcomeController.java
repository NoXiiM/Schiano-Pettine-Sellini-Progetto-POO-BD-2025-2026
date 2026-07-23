package controller.gestionale;
import model.gestionale.utenteEFigli.*;
import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import database.implementazioneDAO.*;
import java.util.ArrayList;

import database.implementazioneDAO.impDAOop;

import model.gestionale.*;


public class WelcomeController {

    private Utente currentUser;

    private ArrayList<Utente> lista_utenti;

    public WelcomeController() {
        this.lista_utenti = new ArrayList<>();

        //inizializzaListaUtenti(lista_utenti);
    }

    protected WelcomeController(ArrayList<Utente> lista_utenti, Utente currentUser) {
        this.lista_utenti = lista_utenti;
        this.currentUser = currentUser;
    }

//    private void inizializzaListaUtenti(ArrayList<Utente> lista_utenti){
//        lista_utenti.add(new Cliente("mirkos", "Mirko", "Pettine", "PIRLONE", LocalDate.of(2025, 12, 25), "Mii", "A104"));
//        lista_utenti.add(new Premium("matteos", "Matteo", "Sellini", "PIRLINO", LocalDate.of(2000, 5, 25), "Maa", "B104"));
//        lista_utenti.add(new Supervisore("noxiim", "Christian", "Schiano", "INGEGNERE", LocalDate.of(2000, 5, 25), "Maa", "A"));
//    }

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

            try{
                db.loginCliente(saldo, tipologia, scontoPercentuale, dataDiBan,
                        nome, cognome, codiceFiscale, dataDiNascita, username, password, identificativo);
                boolean flag = tipologia[0].equals("Premium");
                currentUser= new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);
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

        for (Utente i : lista_utenti) {
            if (i.getUsername().equals(newUser)) throw new RuntimeException("Username non disponibile");
        }

        if(currentUser.getPassword().equals(pass1))
        {
            currentUser.setUsername(newUser);
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

    public ArrayList<Utente> getLista_utenti(){
        return lista_utenti;
    }

    public boolean utenteCliente(){
        if(currentUser instanceof Cliente) return true;
        return false;
    }

}
