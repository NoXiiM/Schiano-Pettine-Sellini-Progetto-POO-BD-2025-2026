package database.DAO;

import model.gestionale.Gioco;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Interfaccia delle funzioni di db usate in caso di login di un cliente, tutte usate solo in ClientWelcomeController ove
 * non specificato altrimenti
 */
public interface DAOopc
{
    /**
     * Svolge update nella tabella cliente nell'id corretto per tutti questi parametri.
     * Campi che mancano perchè semplicemente non possono cambiare: nome, cognome, codiceFiscale, dataDiNascita
     *
     * @param codiceTessera          filtro per aggiornamento
     * @param saldo                  the saldo
     * @param tempoDiGioco           the tempo di gioco
     * @param fichesGiocate          the fiches giocate
     * @param vincitaPercentualeTot  the vincita percentuale tot
     * @param partiteGiocate         the partite giocate
     * @param tipo                   the tipo
     * @param scontoPokerPercentuale the sconto poker percentuale
     * @param sospetto               the sospetto
     * @throws SQLException the sql exception
     */
    void salvataggioCliente(String codiceTessera, int saldo, Duration tempoDiGioco, int fichesGiocate,
                            double vincitaPercentualeTot, int partiteGiocate, String tipo, double scontoPokerPercentuale,
                            boolean sospetto) throws SQLException;

    /**
     * Salva sessione, non c'è bisogno di creare l'id di sessione in java, è serial in db
     *
     * @param idCliente          the id cliente
     * @param idTavolo           the id tavolo
     * @param durata             the durata
     * @param vincitaPercentuale the vincita percentuale
     * @param partiteSvolte      the partite svolte
     * @throws SQLException the sql exception
     */
    void salvaSessione(String idCliente, int idTavolo, Duration durata, double vincitaPercentuale, int partiteSvolte)
            throws SQLException;

    /**
     * Funzione di cambio username
     *
     * @param vecchioCodiceTessera the vecchio codice tessera
     * @param username             the username
     * @param nuovoCodiceTessera   the nuovo codice tessera
     * @throws SQLException the sql exception
     */
    void cambioUsername(String vecchioCodiceTessera, String username, String nuovoCodiceTessera) throws SQLException;

    /**
     * Funzione che esegue query per prendere i tavoli dal database in base al gioco, usata solo in 3 funzioni di
     * TavoloController (il nome di queste funzioni è "popola" + nome del gioco)
     *
     * @param gioco       filtro
     * @param idTavolo    the id tavolo
     * @param numeroPosti the numero posti
     * @param idDealer    the id dealer
     * @throws SQLException the sql exception
     */
    void caricaTavoliGioco(Gioco gioco, ArrayList<Integer> idTavolo, ArrayList<Integer> numeroPosti,
                           ArrayList<String> idDealer) throws SQLException;


    /**
     * Funzione per l'eliminazione dell'account di un cliente
     *
     * @param codiceTessera the codice tessera
     * @throws SQLException the sql exception
     */
    void cancellaCliente(String codiceTessera) throws SQLException;
}
