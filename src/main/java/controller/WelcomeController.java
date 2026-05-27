package controller;
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
    private ArrayList<Utente> lista_utenti;

    public WelcomeController() {
        this.lista_utenti = new ArrayList<>();
//        inizializzaListaGiocatori(lista_giocatori);
//        inizializzaListaAdmin(lista_admin);
        inizializzaListaUtenti(lista_utenti);
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

    public void registrati(String username, String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String password) throws RuntimeException{
        if(username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");

        for (Utente i : lista_utenti) {
            if (i.getUsername().equals(username)) throw new RuntimeException("Username non disponibile");
        }

        String codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        while (codiceTesseraEsiste(codiceTessera)) {
            codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        }

        lista_utenti.add(new Cliente(username, nome, cognome, codiceFiscale, dataNascita, password, codiceTessera));
    }

    public void depositaSaldoCliente(int deposito, Cliente utente){
        utente.deposita(deposito);
    }

    public boolean prelevaSaldoCliente(int prelievo, Cliente utente){
        if(!utente.preleva(prelievo)){
            return false;
        }
        return true;
    }

    private String generaCodiceTessera(String nome, String cognome, LocalDate dataNascita) {

        String inizialeNome = String.valueOf(nome.charAt(0)).toUpperCase();
        String inizialeCognome = String.valueOf(cognome.charAt(0)).toUpperCase();
        //charAt() restituisce char, char + char= int, String.valueOf() serve a convertire char in String

        String gg = String.format("%02d", dataNascita.getDayOfMonth());
        String mm = String.format("%02d", dataNascita.getMonthValue());
        String aa = String.format("%02d", dataNascita.getYear() % 100);

        String lettere = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        String casuali = String.valueOf(lettere.charAt(random.nextInt(26))) + lettere.charAt(random.nextInt(26)) + lettere.charAt(random.nextInt(26));
        //random.nextInt(26) numero casuale tra 0 e 25. charAt() carattere di lettere a quell'indice

        return inizialeNome + inizialeCognome + gg + mm + aa + casuali;
    }

    private boolean codiceTesseraEsiste(String codice) {
        for (Utente i : lista_utenti) {
            //ho modificato qui perchè se nell'istanceof dopo l'espressione se scrivi un nome diventa variabile
            //di i castato nella classe dopo istanceof -> Cliente c = (Cliente) i;
            if (i instanceof Cliente c) {
                if (c.getCodiceTesseraGiocatore().equals(codice)) return true;
            }
        }
        return false;
    }

    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    public boolean changePass(String oldPass, String newPass1, String newPass2) throws RuntimeException{
        if(oldPass.isBlank() || newPass1.isBlank() || newPass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!newPass1.equals(newPass2)) throw new RuntimeException("Le password non coincidono");

        for(Utente i : lista_utenti){
            if (i.getPassword().equals(oldPass) && i.getUsername().equals(currentUser.getUsername())) {
                i.setPassword(newPass1);
                return true;
            }
        }
        return false;
    }

    public boolean changeUsername(String newUser, String pass1, String pass2) throws RuntimeException{
        if(newUser.isBlank() || pass1.isBlank() || pass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(!pass1.equals(pass2)) throw new RuntimeException("Le password non coincidono");

        for (Utente i : lista_utenti) {
            if (i.getUsername().equals(newUser)) throw new RuntimeException("Username non disponibile");
        }

        for(Utente i : lista_utenti){
            if (i.getPassword().equals(pass1) && i.getUsername().equals(currentUser.getUsername())) {
                i.setUsername(newUser);
                return true;
            }
        }
        return false;
    }

    public int getSaldoUtente(){
        Cliente temp= (Cliente) currentUser;
        return temp.getSaldo();
    }

    public String getUserUtente(){
        return currentUser.getUsername();
    }

    public boolean deleteUser(String username, String pass, String conferma) throws RuntimeException{
        if(username.isBlank() || pass.isBlank() || conferma.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(currentUser.getUsername().equals(username) && currentUser.getPassword().equals(pass) && conferma.equals("CONFERMA")){
            lista_utenti.remove(currentUser);
            currentUser= null;
            return true;
        }
        return false;
    }
}
