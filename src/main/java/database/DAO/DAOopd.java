package database.DAO;

import model.gestionale.Gioco;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia delle funzioni di db usate in caso di login di un dipendente
 */
public interface DAOopd {
    /**
     * Query batch che recupera tutti i dati relativi ai clienti, viene utilizzata in una funzione del
     * DipendenteWelcomeController che viene eseguita solo al login dei supervisori
     *
     * @param username                 the username
     * @param nome                     the nome
     * @param cognome                  the cognome
     * @param codiceFiscale            the codice fiscale
     * @param dataDiNascita            the data di nascita
     * @param password                 the password
     * @param codiceTesseraGiocatore   the codice tessera giocatore
     * @param premium                  the premium
     * @param sconto_premium           the sconto premium
     * @param sospetto                 the sospetto
     * @param tempoDiGiocoInSec        the tempo di gioco in sec
     * @param fichesGiocate            the fiches giocate
     * @param saldo                    the saldo
     * @param partiteGiocate           the partite giocate
     * @param vincitaPercentualeTotale the vincita percentuale totale
     * @param dataBan                  the data ban
     * @param motiviBan                the motivi ban
     * @throws SQLException the sql exception
     */
    void recuperaDatiClienti(ArrayList<String> username, ArrayList<String> nome, ArrayList<String> cognome,
                             ArrayList<String> codiceFiscale, ArrayList<LocalDate> dataDiNascita, ArrayList<String> password,
                             ArrayList<String> codiceTesseraGiocatore,ArrayList<Boolean> premium,
                             ArrayList<Double> sconto_premium,
                             ArrayList<Boolean> sospetto,
                             ArrayList<Long> tempoDiGiocoInSec,
                             ArrayList<Integer> fichesGiocate,
                             ArrayList<Integer> saldo,
                             ArrayList<Integer> partiteGiocate,
                             ArrayList<Double> vincitaPercentualeTotale,
                             ArrayList<LocalDate> dataBan,
                             ArrayList<String> motiviBan)throws SQLException;

    /**
     * Query batch che recupera tutti i dati relativi ai Dipendenti, viene utilizzata in una funzione del
     * DipendenteWelcomeController che viene eseguita solo al login dei supervisori
     *
     * @param idDipendenti  the id dipendenti
     * @param nome          the nome
     * @param cognome       the cognome
     * @param dataDiNascita the data di nascita
     * @param codiceFiscale the codice fiscale
     * @param username      the username
     * @param password      the password
     * @param ruolo         the ruolo
     * @param gioco         the gioco
     * @throws SQLException the sql exception
     */
    void recuperaDatiDipendenti(ArrayList<String> idDipendenti, ArrayList<String> nome, ArrayList<String> cognome,
                                ArrayList<LocalDate> dataDiNascita, ArrayList<String> codiceFiscale,
                                ArrayList<String> username, ArrayList<String> password, ArrayList<String> ruolo,
                                ArrayList<String> gioco)throws SQLException;

    /**
     * Query batch che recupera tutti i dati relativi ai Tavoli, viene utilizzata in una funzione del
     * DipendenteWelcomeController che viene eseguita solo al login dei supervisori
     *
     * @param idTavolo    the id tavolo
     * @param gioco       the gioco
     * @param numeroPosti the numero posti
     * @param idDealer    the id dealer
     * @throws SQLException the sql exception
     */
    void recuperaDatiTavoli(ArrayList<Integer> idTavolo, ArrayList<Gioco> gioco,
                            ArrayList<Integer> numeroPosti, ArrayList<String> idDealer) throws SQLException;

    /**
     * Query che recupera tutti gli id dei supervisori associati a un tavolo, viene utilizzata in una funzione del
     * DipendenteWelcomeController che viene eseguita solo al login dei supervisori. Essendo una relazione molti a molti
     * la query avviene sulla tabella SupervisoreTavolo(idTavolo, idSupervisore)
     *
     * @param idTavolo the id tavolo
     * @return ArrayList di idSupervisore
     * @throws SQLException the sql exception
     */
    ArrayList<String> tavoliSupervisori(int idTavolo) throws SQLException;



    /**
     * Funzione che aggiorna i dati relativi a ban di cliente, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori
     *
     * @param idCliente the id cliente
     * @param dataBan   the data ban
     * @param motivoBan the motivo ban
     * @throws SQLException the sql exception
     */
    void salvataggioBan(String idCliente,LocalDate dataBan, String motivoBan) throws SQLException;

    /**
     * Funzione che elimina dipendente licenziato dal db, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori
     *
     * @param idDipendente the id dipendente
     * @throws SQLException the sql exception
     */
    void eliminaDipendente(String idDipendente) throws SQLException;

    /**
     * Funzione che registra il nuovo tavolo creato nel db, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori. L'idDealer (FK) viene sempre posta inizialmente a null
     *
     * @param idTavolo    the id tavolo
     * @param gioco       the gioco
     * @param numeroPosti the numero posti
     * @throws SQLException the sql exception
     */
    void aggiungiTavolo(int idTavolo, String gioco, int numeroPosti) throws SQLException;

    /**
     * Assegna dipendente generico a tavolo, tramite il parametro ruolo riconosce in che tabella inserire l'idDipendente
     * (relazione dealer o supervisore), usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori
     *
     * @param idDipendente the id dipendente
     * @param ruolo        the ruolo
     * @param idTavolo     the id tavolo
     * @throws SQLException the sql exception
     */
    void assegnaDipendenteATavolo(String idDipendente, String ruolo, int idTavolo) throws SQLException;

    /**
     * Funzione che mette in relazione il dealer con id = idDealer con tutti i giochi nella lista giochi, usa un ciclo for each
     * su giochi, usata in una funzione del DipendenteWelcomeController che può essere usata solo da Supervisori
     *
     * @param idDealer the id dealer
     * @param giochi   the giochi
     * @throws SQLException the sql exception
     */
    void aggiungiGiocoDealer(String idDealer, ArrayList<Gioco> giochi) throws  SQLException;

    /**
     * Funzione che recupera tutte le sessioni svolte da un cliente, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori
     *
     * @param idSessione         the id sessione
     * @param idCliente          the id cliente
     * @param idTavolo           the id tavolo
     * @param durata             the durata
     * @param vincitaPercentuale the vincita percentuale
     * @param partiteSvolte      the partite svolte
     * @throws SQLException the sql exception
     */
    void ottieniSessioniDiCliente(ArrayList<Integer> idSessione, String idCliente, ArrayList<Integer> idTavolo,
                                  ArrayList<Duration> durata, ArrayList<Double> vincitaPercentuale,
                                  ArrayList<Integer> partiteSvolte) throws SQLException;

    /**
     * Funzione che cambia il gioco del tavolo in base al parametro gioco, usata in una funzione del DipendenteWelcomeController
     * che può essere usata solo da Supervisori
     *
     * @param idTavolo the id tavolo
     * @param gioco    the gioco
     * @throws SQLException the sql exception
     */
    void cambiaGiocoTavolo(int idTavolo, String gioco) throws SQLException;

    /**
     * Cancella la relazione tra un supervisore e un tavolo, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Supervisori
     *
     * @param idTavolo      the id tavolo
     * @param idSupervisore the id supervisore
     * @throws SQLException the sql exception
     */
    void eliminaSupervisoreTavolo(int idTavolo, String idSupervisore) throws SQLException;

    /**
     * Elimina il tavolo specificato, usata in una funzione del DipendenteWelcomeController che può essere usata solo da
     * Supervisori
     *
     * @param idTavolo the id tavolo
     * @throws SQLException the sql exception
     */
    void eliminaTavolo(int idTavolo) throws SQLException;

    /**
     * Funzione di cambio username per dipendenti in generale, usata nel DipendenteWelcomeController
     *
     * @param vecchioCodiceTessera per trovare la corretta tupla
     * @param username             aggiornamento che subirà username
     * @param nuovoCodiceTessera   aggiornamento che subirà l'id
     * @throws SQLException the sql exception
     */
    void cambioUsername(String vecchioCodiceTessera, String username, String nuovoCodiceTessera) throws SQLException;

    /**
     * Funzione che prende tutte le sessioni di un tavolo, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Dealer
     *
     * @param idSessione         the id sessione
     * @param idDealer           the id dealer
     * @param idTavolo           the id tavolo
     * @param durata             the durata
     * @param vincitaPercentuale the vincita percentuale
     * @param partiteSvolte      the partite svolte
     * @param username           the username
     * @param sospetto           the sospetto
     * @throws SQLException the sql exception
     */
    void ottieniSessioniDiTavolo(ArrayList<Integer> idSessione, String idDealer, ArrayList<Integer> idTavolo,
                                 ArrayList<Duration> durata, ArrayList<Double> vincitaPercentuale,
                                 ArrayList<Integer> partiteSvolte, ArrayList<String> username,
                                 ArrayList<Boolean> sospetto) throws SQLException;

    /**
     * Funzione che aggiorna la flag sospetto su di un cliente, usata in una funzione del DipendenteWelcomeController che
     * può essere usata solo da Dealer
     *
     * @param username the username
     * @throws SQLException the sql exception
     */
    void updateSospetto(String username) throws SQLException;
}
