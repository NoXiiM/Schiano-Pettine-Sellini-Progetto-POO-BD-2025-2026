package controller.gestionale;

import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Utente;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

public class ClientWelcomeController extends WelcomeController {

    public ClientWelcomeController(WelcomeController controller){
        super(controller.getLista_utenti(), controller.getCurrentUser());
    }

    //client
    public void registrati(String username, String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String password) throws RuntimeException{
        if(username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");

        for (Utente i :getLista_utenti()) {
            if (i.getUsername().equals(username)) throw new RuntimeException("Username non disponibile");
        }

        String codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        while (codiceTesseraEsiste(codiceTessera)) {
            codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        }

       getLista_utenti().add(new Cliente(username, nome, cognome, codiceFiscale, dataNascita, password, codiceTessera));
    }

    //client
    //da fare cast Cliente utente
    public void depositaSaldoCliente(int deposito, Cliente utente){
        utente.deposita(deposito);
    }

    //client
    //da fare cast Cliente utente
    public boolean prelevaSaldoCliente(int prelievo, Cliente utente){
        if(!utente.preleva(prelievo)){
            return false;
        }
        return true;
    }

    //client
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

    //client
    private boolean codiceTesseraEsiste(String codice) {
        for (Utente i :getLista_utenti()) {
            //ho modificato qui perchè se nell'istanceof dopo l'espressione se scrivi un nome diventa variabile
            //di i castato nella classe dopo istanceof -> Cliente c = (Cliente) i;
            if (i instanceof Cliente c) {
                if (c.getCodiceTesseraGiocatore().equals(codice)) return true;
            }
        }
        return false;
    }

    //client
    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    //client
    public int getSaldoUtente(){
        Cliente temp= (Cliente) getCurrentUser();
        return temp.getSaldo();
    }

    //solo client, un admin non puo cancellare il profilo, un superadmin puo cancellare altri profili
    public boolean deleteUser(String username, String pass, String conferma) throws RuntimeException{
        if(username.isBlank() || pass.isBlank() || conferma.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if(getCurrentUser().getUsername().equals(username) && getCurrentUser().getPassword().equals(pass) && conferma.equals("CONFERMA")){
           getLista_utenti().remove(getCurrentUser());
            setCurrentUserNull();
            return true;
        }
        return false;
    }

    //client
    public void resetPass(String nome, String cognome, String username) throws RuntimeException{

        if(username.isBlank() || nome.isBlank() || cognome.isBlank()) throw new RuntimeException("Compila tutti i campi!");


        for(Utente i :getLista_utenti()){
            if(i.getUsername().equals(username) && i.getNome().equals(nome) && i.getCognome().equals(cognome)){
                i.setPassword("P@ssw0rd!");
                JOptionPane.showMessageDialog(null, "Password resettata ! Al prossimo accesso usa: \"P@ssword!\", non dimenticare di modificarla !");
                return;
            }
        }
        throw new RuntimeException("Utente non trovato!");
    }

}
