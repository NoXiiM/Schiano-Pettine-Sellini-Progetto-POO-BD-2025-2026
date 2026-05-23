package controller;
import model.gestionale.utenteEFigli.*;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import model.gestionale.*;


public class WelcomeController {

    private Utente currentUser;

//    private ArrayList<Cliente> lista_giocatori= new ArrayList<>();
//    private ArrayList<Dipendente> lista_admin= new ArrayList<>();
    private ArrayList<Utente> lista_utenti= new ArrayList<>();

    public WelcomeController() {
//        inizializzaListaGiocatori(lista_giocatori);
//        inizializzaListaAdmin(lista_admin);
        inizializzaListaUtenti(lista_utenti);
    }

    private void inizializzaListaUtenti(ArrayList<Utente> lista_utenti){
        lista_utenti.add(new Base("mirkos", "Mirko", "Pettine", "PIRLONE", LocalDate.of(2025, 12, 25), "Mii", "A104"));
        lista_utenti.add(new Premium("matteos", "Matteo", "Sellini", "PIRLINO", LocalDate.of(2000, 5, 25), "Maa", "B104"));
        lista_utenti.add(new Supervisore("noxiim", "Christian", "Schiano", "INGEGNERE", LocalDate.of(2000, 5, 25), "Maa", "A"));
    }

    private void inizializzaListaGiocatori(ArrayList<Cliente> lista_giocatori){
        lista_giocatori.add(new Base("mirkos", "Mirko", "Pettine", "PIRLONE", LocalDate.of(2025, 12, 25), "Mii", "A104"));
        lista_giocatori.add(new Premium("matteos", "Matteo", "Sellini", "PIRLINO", LocalDate.of(2000, 5, 25), "Maa", "B104"));
    }

    private void inizializzaListaAdmin(ArrayList<Dipendente> lista_admin){
        lista_admin.add(new Supervisore("noxiim", "Christian", "Schiano", "INGEGNERE", LocalDate.of(2000, 5, 25), "Maa", "A"));
    }

    public Utente login(String username, String password, String userType) throws RuntimeException{
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
                return i;
            }
        }

        throw new RuntimeException("Credenziali errate!");
    }

    public void resetPass(String nome, String cognome, String username) throws RuntimeException{

        if(username.isBlank() || nome.isBlank() || cognome.isBlank()) throw new RuntimeException("Compila tutti i campi!");


        for(Utente i : lista_utenti){
            if(i.getUsername().equals(username) && i.getNome().equals(nome) && i.getCognome().equals(cognome)){
                i.setPassword("P@ssw0rd!");
                JOptionPane.showMessageDialog(null, "Password resettata ! Al prossimo accesso usa: \"P@ssword!\", non dimenticare di modificarla !");
                return;
            }
        }
        throw new RuntimeException("Utente non trovato!");
    }

    //funzione registrati

}
