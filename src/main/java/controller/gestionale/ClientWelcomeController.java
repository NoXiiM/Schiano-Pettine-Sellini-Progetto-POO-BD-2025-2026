package controller.gestionale;

import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Utente;
import database.implementazioneDAO.impDAOop;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

public class ClientWelcomeController extends WelcomeController {

    private ArrayList<String> usernames;
    private ArrayList<String> codiciTessera;
    private Cliente cliente;

    public ClientWelcomeController(WelcomeController controller){
        super(controller.getLista_utenti(), controller.getCurrentUser());
        cliente= (Cliente) getCurrentUser();
    }

    //client
    public void registrati(String username, String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String password, int importo) throws RuntimeException{
        if(username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");
        if (importo < 50) throw new RuntimeException("Deposito minimo obbligatorio di 50 euro");

//        for (Utente i :getLista_utenti()) {
//            if (i.getUsername().equals(username)) throw new RuntimeException("Username non disponibile");
//        }

        impDAOop db= new impDAOop();
        usernames= new ArrayList<>();
        codiciTessera= new ArrayList<>();


        //li prendo cosi se piu username non sono disp non ce bisogno di fare query ogni volta
        try {
            db.usernameTessereUtenti(usernames, codiciTessera);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //check locale
        for(String user : usernames){
            if(username.equals(user)) throw new RuntimeException("Username non disponibile");
        }

        String codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        while (codiceTesseraEsiste(codiceTessera)) {
            codiceTessera = generaCodiceTessera(nome, cognome, dataNascita);
        }

        pulisciUsernamesTessere();

        try{
            db.registrazione(codiceTessera, username, nome, cognome, codiceFiscale,
                    dataNascita, password, importo);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

       getLista_utenti().add(new Cliente(username, nome, cognome, codiceFiscale, dataNascita, password, codiceTessera));
    }

    //client
    public void depositaSaldoCliente(int deposito){
        cliente.deposita(deposito);
    }

    //client
    public boolean prelevaSaldoCliente(int prelievo){
        if(!cliente.preleva(prelievo)){
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
        for (String i : codiciTessera) {

            if(codice.equals(i)){
                return true;
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
        return cliente.getSaldo();
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

    public void pulisciUsernamesTessere(){
        usernames.clear();
        codiciTessera.clear();
    }

    public boolean isBanned(){
        return cliente.getBan() != null;
    }
}
