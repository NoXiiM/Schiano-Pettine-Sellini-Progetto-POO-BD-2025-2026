package controller.gestionale;

import database.implementazioneDAO.ImpDAOopc;
import model.gestionale.Giocatore;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import database.implementazioneDAO.ImpDAOop;

import javax.swing.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

/**
 * Controller che gestisce un Cliente
 */
public class ClientWelcomeController extends WelcomeController {

    private final Cliente cliente;
    private Sessione sessione;

    /**
     * Costruttore che istanzia un nuovo ClientWelcomeController a partire dal WelcomeController, questo comporta l'esistenza
     * di 2 controller, questo tipo di logica comporta il dover gestire la pulizia dei dati di WelcomeController quando un
     * utente effettua il logout
     *
     * @param controller WelcomeController
     */
    public ClientWelcomeController(WelcomeController controller) {
        super(controller.getCurrentUser(), controller.getUsernamesList());
        cliente = (Cliente) getCurrentUser();
    }

    /**
     * Costruttore che istanzia un nuovo ClientWelcomeController a partire da solo un'istanza di Cliente, è usato per quando
     * i giocatori effettuano il login per giocare a poker, quindi basta solo avere i dati del cliente
     *
     * @param cliente cliente
     */
    public ClientWelcomeController(Cliente cliente)
    {
        this.cliente = cliente;
    }

    /**
     * Funzione che permette la registrazione di un cliente
     *
     * @param username      username
     * @param nome          nome
     * @param cognome       cognome
     * @param codiceFiscale codice fiscale
     * @param dataNascita   data nascita
     * @param password      password
     * @param importo       importo
     * @throws RuntimeException lanciato se almeno uno dei campi è vuoto, se l'età non è almeno 18 anni, se il deposito non
     * è almeno di 50 euro, se l'username non è disponibile
     */
//client
    public void registrazioneCliente(String username, String nome, String cognome, String codiceFiscale,
                                     LocalDate dataNascita, String password, int importo) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");
        if (importo < 50) throw new RuntimeException("Deposito minimo obbligatorio di 50 euro");

        //check locale
        for (String user : usernames) {
            if (username.equals(user)){
                throw new RuntimeException("Username non disponibile");
            }
        }

        String codiceTessera = generaCodiceTessera(username);

        try {
            new ImpDAOop().registrazioneCliente(codiceTessera, username, nome, cognome, codiceFiscale,
                    dataNascita, password, importo);
        } catch (SQLException e) {
            aggiornaUsernames();
            throw new RuntimeException(e);
        }

        pulisciUsernames();
    }

    /**
     * Funzione che incrementa il saldo del cliente
     *
     * @param deposito cifra depositata
     * @throws RuntimeException se il deposito è un numero negativo
     */
//client
    public void depositaSaldoCliente(int deposito) throws RuntimeException
    {
        cliente.deposita(deposito);
    }

    /**
     * Funzione che decrementa il saldo del cliente
     *
     * @param prelievo the prelievo
     * @return true: prelievo ha avuto successo, false: prelievo fallito
     * @throws RuntimeException se il prelievo è un numero negativo
     */
//client
    public boolean prelevaSaldoCliente(int prelievo) throws RuntimeException {
        return cliente.preleva(prelievo);
    }

    //client
    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    /**
     * Funzione per cambiare username per i clienti, si occupa anche di assegnare il nuovo codice al cliente generato a
     * partire dall'username
     *
     * @param newUser nuovo username
     * @param pass1   password
     * @param pass2   conferma password
     * @return true: cambio username effettuato con successo, false: cambio username fallito
     * @throws RuntimeException errore lanciato se le 2 password non coincidono, se le password coincidono ma non sono corrette,
     * se l'username è già stato preso
     * @throws SQLException     the sql exception
     */
    public boolean changeUsername(String newUser, String pass1, String pass2) throws RuntimeException, SQLException{
        if(newUser.isBlank() || pass1.isBlank() || pass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        ImpDAOop db_fetch_user= new ImpDAOop();

        if(!pass1.equals(pass2)) throw new RuntimeException("Le 2 password non coincidono");
        if(db_fetch_user.trovaTabella(cliente.getUsername(), pass1) == null) throw new RuntimeException("password errata");

        ImpDAOopc db= new ImpDAOopc();

        db_fetch_user.usernameUtenti(usernames);

        for(String i : usernames){
            if(i.equals(newUser)) throw new RuntimeException("username già preso");
        }

        String newCodiceTessera= generaCodiceTessera(newUser);

        db.cambioUsername(cliente.getCodiceTesseraGiocatore(), newUser, newCodiceTessera);
        cliente.setUsername(newUser);
        cliente.setCodiceTesseraGiocatore(newCodiceTessera);
        return true;
    }

    /**
     * Funzione per la cancellazione di un account Cliente
     *
     * @param username username
     * @param pass     password
     * @param conferma parola di conferma
     * @return true: cancellazione account effettuata con successo, false: cancellazione account fallita
     * @throws RuntimeException errore lanciato se almeno un campo non è compilato
     * @throws SQLException     the sql exception
     */
//solo client, un admin non puo cancellare il profilo, un superadmin puo cancellare altri profili
    public boolean deleteUser(String username, String pass, String conferma) throws RuntimeException, SQLException {
        if (username.isBlank() || pass.isBlank() || conferma.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        ImpDAOopc db = new ImpDAOopc();

        if(username.equals(cliente.getUsername()) && pass.equals(cliente.getPassword()) && conferma.equals("CONFERMA"))
        {
            db.cancellaCliente(cliente.getCodiceTesseraGiocatore());

            setCurrentUserNull();
            return true;
        }

        return false;
    }

    /**
     * Is banned boolean.
     *
     * @return the boolean
     */
    public boolean isBanned() {
        return cliente.getBan() != null;
    }

    /**
     * Viene aperta una nuova sessione di gioco con il giocatoreCorrente e il tavolo selezionato, con la creazione della
     * sessione parte il timer ed è come se si fosse aperto un canale di ascolto per l'aggiornamento dei vari dati di gioco
     *
     * @param tavoloSelezionato tavolo selezionato dalle GUI SelezionaTavolo
     */
    public void creaNuovaSessioneDiGioco(Tavolo tavoloSelezionato)
    {
        Giocatore giocatoreCorrente = new Giocatore(cliente, cliente.getSaldo());
        sessione = new Sessione(giocatoreCorrente, tavoloSelezionato);
        sessione.startTimer();
    }

    /**
     * Get saldo giocatore int.
     *
     * @return the int
     */
    public int getSaldoGiocatore(){
        return sessione.getSaldoGiocatore();
    }

    /**
     * Decrementa saldo giocatore.
     *
     * @param creditoInserito the credito inserito
     * @throws RuntimeException the runtime exception
     */
    public void decrementaSaldoGiocatore(int creditoInserito) throws RuntimeException{
        sessione.decrementaSaldoGiocatore(creditoInserito);
    }

    /**
     * Incrementa saldo giocatore.
     *
     * @param creditoInserito the credito inserito
     */
    public void incrementaSaldoGiocatore(int creditoInserito){
        sessione.incrementaSaldoGiocatore(creditoInserito);
    }

    /**
     * Al termine della sessione si controlla se un cliente ha soddisfatto i requisiti per diventare un cliente premium,
     * in caso affermativo viene mostrato il messaggio a schermo, poi salvaDatiClienteUscitaDaGioco si occupa dei salvataggi
     * nel db
     *
     * @throws SQLException the sql exception
     */
    public void terminaSessione() throws SQLException{
        if(sessione.terminaSessione())
            JOptionPane.showMessageDialog(null, getClienteUsername() + " sei diventato un cliente di livello premium!",
                    "promozione a premium", JOptionPane.INFORMATION_MESSAGE);
        salvaDatiClienteUscitaDaGioco();
    }

    /**
     * Funzione che si occupa di salvare i dati del cliente e della sessione al termine della sessione di gioco
     *
     * @throws SQLException the sql exception
     */
    public void salvaDatiClienteUscitaDaGioco() throws SQLException{
        ImpDAOopc db= new ImpDAOopc();

        db.salvaSessione(cliente.getCodiceTesseraGiocatore(), sessione.getTavolo().getIdTavolo(),
                sessione.getDurataSessione(), sessione.getVincitaPercentuale(), sessione.getPartiteSvolte());

        String tipologiaCliente= (cliente.isPremium()) ? "Premium" : "Base";

        db.salvataggioCliente(cliente.getCodiceTesseraGiocatore(), cliente.getSaldo(), cliente.getTempoDiGioco(),
                cliente.getFichesGiocate(), cliente.getVincitaPercentualeTot(), cliente.getPartiteGiocate(), tipologiaCliente,
                cliente.getSconto_premium(), cliente.isSospetto());
    }

    /**
     * Funzione che si occupa di salvare solo i dati di cliente quando si esce dalla zona di gestione account
     *
     * @throws SQLException the sql exception
     */
    public void salvaDatiClienteUscitaDaGestione() throws SQLException{
        ImpDAOopc db= new ImpDAOopc();

        String tipologiaCliente= (cliente.isPremium()) ? "Premium" : "Base";

        db.salvataggioCliente(cliente.getCodiceTesseraGiocatore(), cliente.getSaldo(), cliente.getTempoDiGioco(),
                cliente.getFichesGiocate(), cliente.getVincitaPercentualeTot(), cliente.getPartiteGiocate(), tipologiaCliente,
                cliente.getSconto_premium(), cliente.isSospetto());
    }

    /**
     * Get saldo cliente int.
     *
     * @return the int
     */
    public int getSaldoCliente(){
        return cliente.getSaldo();
    }

    /**
     * Aggiorna vincita percentuale.
     *
     * @param v the v
     */
    public void aggiornaVincitaPercentuale(boolean v){
        sessione.aggiornaVincitaPercentuale(v);
    }

    /**
     * Get posti tavolo int.
     *
     * @return the int
     */
    public int getPostiTavolo(){
        return sessione.getPostiTavolo();
    }

    /**
     * Get time duration.
     *
     * @return the duration
     */
    public Duration getTime(){
        return sessione.getDurataSessione();
    }

    /**
     * Gets cliente username.
     *
     * @return the cliente username
     */
    public String getClienteUsername() {
        return cliente.getUsername();
    }

    /**
     * Get tavolo corrente tavolo.
     *
     * @return the tavolo
     */
    public Tavolo getTavoloCorrente(){return sessione.getTavolo();}

    /**
     * Get sconto cliente double.
     *
     * @return the double
     */
    public double getScontoCliente(){return cliente.getSconto_premium();}

    /**
     * Decrementa saldo cliente.
     *
     * @param value the value
     * @throws RuntimeException the runtime exception
     */
    public void decrementaSaldoCliente(int value) throws RuntimeException
    {
        cliente.decrementaSaldoCliente(value);
    }
}

