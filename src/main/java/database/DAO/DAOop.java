package database.DAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia delle funzioni di db usate prima di un login effettivo, tutte usate solo in WelcomeController ove
 * non specificato altrimenti
 */
public interface DAOop
{
    /**
     * Funzione che verifica se i dati inseriti sono corretti, e resetta la password dell'account con l'username inserito.
     * La funzione verifica prima se nella tabella clienti esiste una tupla con questi 3 dati combacianti ai parametri
     * passati, in caso positivo viene fatto il reset di password sulla tupla con il determinato username e si restituisce
     * true, altrimenti si va avanti e si esegue lo stesso controllo sui 3 dati per dipendente, allo stesso modo, se il
     * controllo va a buon fine aggiornamento password e return true, sennò return false
     *
     * @param nome     the nome
     * @param cognome  the cognome
     * @param username the username
     * @return true o false a seconda se l'operazione ha avuto successo
     * @throws SQLException the sql exception
     */
    boolean passwordDimenticata(String nome, String cognome, String username) throws SQLException;

    /**
     * Registra un nuovo account di un cliente nel db, usata in ClientWelcomeController
     *
     * @param id            the id
     * @param username      the username
     * @param nome          the nome
     * @param cognome       the cognome
     * @param codiceFiscale the codice fiscale
     * @param data          the data
     * @param password      the password
     * @param saldo         the saldo
     * @throws SQLException the sql exception
     */
    void registrazioneCliente(String id, String username, String nome, String cognome, String codiceFiscale, LocalDate data,
                       String password, int saldo) throws SQLException;

    /**
     * Registra un nuovo account di un dipendente nel db, non è propriamente una funzione che starebbe in DAOop, formalmente
     * dovrebbe stare in DAOopc, ma è stata lasciata qui per tenere le due funzioni di registrazione vicine.
     * Usata in DipendenteWelcomeController, solo un Supervisore può registrare nuovi dipendenti
     *
     * @param id            the id
     * @param username      the username
     * @param nome          the nome
     * @param cognome       the cognome
     * @param codiceFiscale the codice fiscale
     * @param dataDiNascita the data di nascita
     * @param password      the password
     * @param ruolo         the ruolo
     * @throws SQLException the sql exception
     */
    void registrazioneDipendente(String id, String username, String nome, String cognome, String codiceFiscale,
                              LocalDate dataDiNascita, String password, String ruolo) throws SQLException;

    /**
     * Trova il tipo di utente che sta provando a fare il login e se le credenziali sono corrette, sennò restituisce null.
     * Usato anche per il cambio username sia in ClientWelcomeController che in DipendenteWelcomeController
     *
     * @param username the username
     * @param password the password
     * @return restituisce "Cliente" o "Dipendente" o null
     * @throws SQLException the sql exception
     */
    String trovaTabella(String username, String password) throws SQLException;

    /**
     * Carica il cliente che ha effettuato il login, usato anche in ControllerPoker per le ragioni spiegate al "return"
     *
     * @param codiceTessera          the codice tessera
     * @param saldo                  the saldo
     * @param tempoDiGioco           the tempo di gioco
     * @param fichesGiocate          the fiches giocate
     * @param vincitaPercentualeTot  the vincita percentuale tot
     * @param partiteGiocate         the partite giocate
     * @param tipo                   the tipo
     * @param scontoPokerPercentuale the sconto poker percentuale
     * @param sospetto               the sospetto
     * @param dataDiBan              the data di ban
     * @param motiviBan              the motivi ban
     * @param nome                   the nome
     * @param cognome                the cognome
     * @param codiceFiscale          the codice fiscale
     * @param dataDiNascita          the data di nascita
     * @param username               the username
     * @param password               the password
     * @return il return di boolean per il login classico nell'app non viene usato, è utile solo quando un cliente prova
     * a fare login per giocare a poker in una partita iniziata da un altro cliente. Visto che solo la categoria
     * cliente può giocare il controllo non serve farlo in trova tabella, si può fare direttamente sul risultato della
     * query di login Cliente
     * @throws SQLException the sql exception
     */
    boolean loginCliente(String[] codiceTessera, int[] saldo, long[] tempoDiGioco, int[] fichesGiocate,
                      double[] vincitaPercentualeTot, int[] partiteGiocate, String[] tipo, double[] scontoPokerPercentuale,
                      boolean[] sospetto, LocalDate[] dataDiBan, String[] motiviBan, String[] nome, String[] cognome,
                      String[] codiceFiscale, LocalDate[] dataDiNascita, String username, String password) throws SQLException;

    /**
     * Funzione che effettua il login di un dipendente
     *
     * @param identificativo the identificativo
     * @param nome           the nome
     * @param cognome        the cognome
     * @param codiceFiscale  the codice fiscale
     * @param dataDiNascita  the data di nascita
     * @param ruolo          the ruolo
     * @param username       the username
     * @param password       the password
     * @throws SQLException the sql exception
     */
    void loginDipendente(String[] identificativo, String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita,
                         String[] ruolo, String username, String password) throws SQLException;

    /**
     * Funzione che preleva tutti gli usernames già assegnati in maniera tale da poter effettuare controlli quando
     * qualcuno sta provando a fare un'operazione che prevede cambio o creazione di un username
     *
     * @param usernames the usernames
     * @throws SQLException the sql exception
     */
    void usernameUtenti(ArrayList<String> usernames) throws SQLException;

    /**
     * Funzione per il cambio password di un utente
     *
     * @param nuovaPassword the nuova password
     * @param username      the username
     * @param ruolo         the ruolo
     * @throws SQLException the sql exception
     */
    void cambioPassword(String nuovaPassword, String username, String ruolo) throws SQLException;
}
