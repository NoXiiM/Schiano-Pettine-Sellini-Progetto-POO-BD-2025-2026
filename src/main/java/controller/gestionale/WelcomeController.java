package controller.gestionale;
import model.gestionale.utenteEFigli.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

import model.gestionale.*;


public class WelcomeController {

    private Utente currentUser;

//    private ArrayList<Cliente> lista_giocatori= new ArrayList<>();
//    private ArrayList<Dipendente> lista_admin= new ArrayList<>();
    //TODO: molte cose in questo file andranno cambiate con l'implementazione del database
    private ArrayList<Utente> lista_utenti;

    public WelcomeController() {
        this.lista_utenti = new ArrayList<>();
//        inizializzaListaGiocatori(lista_giocatori);
//        inizializzaListaAdmin(lista_admin);
        inizializzaListaUtenti(lista_utenti);
    }

    protected WelcomeController(ArrayList<Utente> lista_utenti, Utente currentUser) {
        this.lista_utenti = lista_utenti;
        this.currentUser = currentUser;
    }

    private void inizializzaListaUtenti(ArrayList<Utente> lista_utenti){
        lista_utenti.add(new Cliente("mirkos", "Mirko", "Pettine", "PIRLONE", LocalDate.of(2025, 12, 25), "Mii", "A104"));
        lista_utenti.add(new Premium("matteos", "Matteo", "Sellini", "PIRLINO", LocalDate.of(2000, 5, 25), "Maa", "B104"));
        lista_utenti.add(new Supervisore("noxiim", "Christian", "Schiano", "INGEGNERE", LocalDate.of(2000, 5, 25), "Maa", "A"));
    }

//    private void inizializzaListaGiocatori(ArrayList<Cliente> lista_giocatori){
//        lista_giocatori.add(new Base("mirkos", "Mirko", "Pettine", "PIRLONE", LocalDate.of(2025, 12, 25), "Mii", "A104"));
//        lista_giocatori.add(new Premium("matteos", "Matteo", "Sellini", "PIRLINO", LocalDate.of(2000, 5, 25), "Maa", "B104"));
//    }
//
//    private void inizializzaListaAdmin(ArrayList<Dipendente> lista_admin){
//        lista_admin.add(new Supervisore("noxiim", "Christian", "Schiano", "INGEGNERE", LocalDate.of(2000, 5, 25), "Maa", "A"));
//    }

    public Utente login(String username, String password) throws RuntimeException{
        if(username.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");
         // JOptionPane.showMessageDialog(null, userType, "Errore", JOptionPane.ERROR_MESSAGE); // --> Per verificare se effettivamente restituisce Admin/Giocatore

//        if(userType.equals("Admin")){
//            for(Dipendente i : lista_admin){
//                if(i.getUsername().equals(username) && i.getPassword().equals(password)){
//                    return i;
//                }
//            }
//        } else {
//            for(Cliente i : lista_giocatori) {
//                if (i.getUsername().equals(username) && i.getPassword().equals(password)) {
//                    return i;
//                }
//            }
//        }

        for(Utente i : lista_utenti){
            if (i.getUsername().equals(username) && i.getPassword().equals(password)) {
                currentUser= i;
                return i;
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

}
