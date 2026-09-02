package controller.gestionale;
import model.gestionale.utenteEFigli.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import database.implementazioneDAO.*;


/**
 * Controller che gestisce un Utente non ancora definito in un ruolo
 */
public class WelcomeController {

    private Utente currentUser;
    protected final ArrayList<String> usernames;

    /**
     * Istanzia WelcomeController.
     */
    public WelcomeController() {
        usernames= new ArrayList<>();
    }

    /**
     * Istanzia WelcomeController inizializzando alcuni attributi
     *
     * @param currentUser utente corrente
     * @param usernames   lista degli username già presi
     */
    public WelcomeController(Utente currentUser, ArrayList<String> usernames) {
        this.currentUser = currentUser;
        this.usernames = usernames;
    }

    /**
     * Funzione che permette il login di un utente, quindi se le credenziali sono corrette viene istanziata la corretta
     * classe di appartenenza dell'utente e questa istanza viene assegnata a current user, sennò viene lanciata la RuntimeException
     *
     * @param username username
     * @param password password
     * @throws RuntimeException exception lanciata se alcuni campi sono stati lasciati vuoti o se le credenziali sono errate
     * @throws SQLException     the sql exception
     */
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

            currentUser = new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                    identificativo[0], flag, scontoPercentuale[0], sospetto[0], tempoDiGioco[0], fichesGiocate[0],
                    saldo[0], partiteGiocate[0], vincitaPercentualeTot[0], dataDiBan[0], motiviBan[0]);

        } else if(tipo.equals("Dipendente")){

                db.loginDipendente(identificativo, nome, cognome, codiceFiscale, dataDiNascita, tipologia, username, password);

                if(tipologia[0].equals("Dealer")){
                    currentUser= new Dealer(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                            identificativo[0]);
                } else{
                    currentUser= new Supervisore(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password, identificativo[0]);
                }
        }
    }

    /**
     * Funzione per il cambio password dopo aver già effettuato il login
     *
     * @param oldPass  password vecchia
     * @param newPass1 password nuova
     * @param newPass2 conferma password nuova
     * @return true: cambio password avvenuto con successo, false: cambio password fallito
     * @throws RuntimeException the runtime exception
     * @throws SQLException     the sql exception
     */
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

    /**
     * Funzione per il reset della password dopo aver già effettuato il login o ancora prima di aver effettuato il login
     *
     * @param nome     nome
     * @param cognome  cognome
     * @param username username
     * @throws RuntimeException errore lanciato se le credenziali sono errate o se almeno un campo è vuoto
     * @throws SQLException     the sql exception
     */
    public void resetPass(String nome, String cognome, String username) throws RuntimeException, SQLException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        ImpDAOop db = new ImpDAOop();

        if(!db.passwordDimenticata(nome, cognome, username)){
            throw new RuntimeException("Credenziali errate");
        }
        if(currentUser!=null)currentUser.setPassword("P@ssw0rd!"); //Se la password non la ricordiamo in accesso
    }

    /**
     * Get user utente string.
     *
     * @return the string
     */
    public String getUserUtente(){
        return currentUser.getUsername();
    }

    /**
     * Get current user utente.
     *
     * @return the utente
     */
    public Utente getCurrentUser(){
        return currentUser;
    }

    /**
     * Set current user null.
     */
    public void setCurrentUserNull(){
        currentUser= null;
    }

    /**
     * Is utente a cliente boolean.
     *
     * @return the boolean
     */
    public boolean isUtenteACliente(){
        return (currentUser instanceof Cliente);
    }

    /**
     * Is utente a dealer boolean.
     *
     * @return the boolean
     */
    public boolean isUtenteADealer(){
        return(currentUser != null && currentUser instanceof Dealer);
    }

    /**
     * Gets usernames list.
     *
     * @return the usernames list
     */
    public ArrayList<String> getUsernamesList() {
        return usernames;
    }

    /**
     * Funzione che carica/aggiorna la lista degli usernames già presi con i dati del db
     */
    public void aggiornaUsernames() {

        ImpDAOop db = new ImpDAOop();

        try {
            db.usernameUtenti(usernames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Svuota la lista degli usernames già presi
     */
    public void pulisciUsernames() {
        usernames.clear();
    }

    /**
     * Funzione che genera un identificativo per l'utente a partire dall'username e con dei numeri casual.
     * Nota importante: sarebbe stato meglio non fare una funzione del genere e impostare direttamente dal db l'id come serial
     * in maniera tale che l'id non fosse legato a un valore reale che è soggetto a cambiamenti, purtroppo questa scelta legata
     * un po' all'estetica di un codice alfanumerico ci ha portato a intraprendere una strada molto in inefficiente, comunque
     * la gestione di questa chiave primaria è tenuta solida dal fatto che, quando avviene un aggiornamento dell'username
     * di un utente, viene anche generato un nuovo codice a partire dal nuovo username che poi diventerà la nuova PK, questo
     * comporta di dover effettuare degli ON UPDATE CASCADE sul db purtroppo
     *
     * @param username username
     * @return codice generato per l'utente
     */
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
