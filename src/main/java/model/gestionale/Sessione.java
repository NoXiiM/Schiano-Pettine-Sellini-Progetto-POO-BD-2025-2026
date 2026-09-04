package model.gestionale;

import model.gestionale.utenteEFigli.Cliente;

import java.time.Duration;
import java.time.Instant;

/**
 * Sessione è la relazione che lega un cliente/giocatore a un tavolo, la sessione registra tutte le statistiche di gioco
 * utili al casinò per valutare il profilo del cliente
 */
public class Sessione
{
    private double vincitaPercentuale;
    //in più rispetto a uml
    private int partiteSvolte;

    //gestione tempo/
    private Instant inizioCronometro;
    private Duration durataSessione;

    //attributi da associazioni
    private Giocatore giocatore;
    private Tavolo tavolo;

    private final int idTavolo;
    private int idSessione;

    /**
     * Istanzia una nuova sessione di un giocatore a un tavolo, gli attributi delle statistiche di gioco vengono inizializzate
     * a un loro default
     *
     * @param giocatore the giocatore
     * @param tavolo    the tavolo
     */
    public Sessione(Giocatore giocatore, Tavolo tavolo)
    {
        durataSessione = Duration.ZERO;
        partiteSvolte = 0;
        this.giocatore = giocatore;
        this.tavolo = tavolo;
        idTavolo = tavolo.getIdTavolo();
    }

    /**
     * Costruttore per istanziare sessioni già esistenti con dati presi dal db
     *
     * @param idSessione         the id sessione
     * @param idTavolo           the id tavolo
     * @param durata             the durata
     * @param vincitaPercentuale the vincita percentuale
     * @param partiteSvolte      the partite svolte
     */
    public Sessione(int idSessione, int idTavolo, Duration durata,
                    double vincitaPercentuale, int partiteSvolte)
    {
        this.idSessione = idSessione;
        this.idTavolo = idTavolo;
        durataSessione = durata;
        this.vincitaPercentuale = vincitaPercentuale;
        this.partiteSvolte = partiteSvolte;
    }

    /**
     * Funzione che segna il punto di partenza del cronometraggio
     */
//timer
    public void startTimer()
    {
        inizioCronometro = Instant.now();
    }

    /**
     * Gets durata sessione.
     *
     * @return the durata sessione
     */
    public Duration getDurataSessione()
    {
        return durataSessione;
    }

    /**
     * Funzione che assegna a durataSessione il tempo di distacco tra ora e quello a cui è stata chiamata startTimer
     */
    public void stopTimer()
    {
        durataSessione = Duration.between(inizioCronometro, Instant.now());
    }

    /**
     * Gets saldo giocatore.
     *
     * @return the saldo giocatore
     */
//giocatore
    public int getSaldoGiocatore()
    {
        return giocatore.getFiches();
    }

    /**
     * Decrementa il saldo del giocatore
     *
     * @param val the val
     * @throws RuntimeException lancia errore se val è maggiore delle fiches del giocatore, puntata non valida
     */
    public void decrementaSaldoGiocatore(int val) throws RuntimeException
    {
        if(val <= giocatore.getFiches())
        {
            giocatore.incrementaFiches(-val);
            giocatore.incrementaFichesGiocate(val);
        }
        else throw new RuntimeException("Saldo insufficiente");
    }

    /**
     * Incrementa il saldo del giocatore.
     *
     * @param val the val
     */
    public void incrementaSaldoGiocatore(int val)
    {
        giocatore.incrementaFiches(val);
    }

    /**
     * Funzione usata per terminare la sessione di gioco: il cliente associato viene aggiornato coi dati del giocatore,
     * viene registrato il tempo passato e poi si aggiornano altri dati derivati in cliente
     *
     * @return aggiornaDatiCliente ritorna true se il cliente è stato promosso a premium, false se no
     */
    public boolean terminaSessione()//Aggiorna il saldo del giocatore in utente
    {
        giocatore.chiudiSessione();
        stopTimer();
        return aggiornaDatiCliente();
    }

    /**
     * Funzione che aggiorna la vincita percentuale registrata nella sessione
     *
     * @param vittoria true = win, false = loss
     */
    public void aggiornaVincitaPercentuale(boolean vittoria)
    {
        partiteSvolte += 1;
        double suc;
        if(vittoria) suc = 100;
        else suc = 0;

        if(partiteSvolte == 1) vincitaPercentuale = suc;
        else vincitaPercentuale = (vincitaPercentuale * (partiteSvolte -1) + suc)/ partiteSvolte;
    }

    /**
     * Gets vincita percentuale.
     *
     * @return the vincita percentuale
     */
    public double getVincitaPercentuale()
    {
        return vincitaPercentuale;
    }

    /**
     * Aggiorna dati del cliente
     *
     * @return true: il cliente è diventato premium, false: il cliente non è diventato premium
     */
    private boolean aggiornaDatiCliente()
    {
        Cliente clienteAssociato = giocatore.getClienteAssociato();

        clienteAssociato.aggiornaPercentualeVittoria(vincitaPercentuale, partiteSvolte);
        clienteAssociato.aggiornaTempoDiGioco(durataSessione);
        if(clienteAssociato.convertiPremium())
        {
            clienteAssociato.setPremium(true);
            return true;
        }
        return false;
    }

    /**
     * Gets posti tavolo.
     *
     * @return the posti tavolo
     */
    public int getPostiTavolo()
    {
        return tavolo.getNumeroPosti();
    }

    /**
     * Get tavolo.
     *
     * @return the tavolo
     */
    public Tavolo getTavolo(){
        return tavolo;
    }

    /**
     * Gets partite svolte.
     *
     * @return the partite svolte
     */
    public int getPartiteSvolte() {
        return partiteSvolte;
    }

    @Override
    public String toString() {
        return "id Sessione: " + idSessione + " id Tavolo: " + idTavolo;
//                idSessione idTavolo durataSes,
//         vincitaPercentuale partiteSvolte
    }

    /**
     * Funzione che costruisce stringa formattata con i dati relativi a sessione
     *
     * @return messaggio formattato
     */
    public String infoSessione()
    {
        return "durata: " + durataSessione.toHours() + ":" +
                String.format("%02d", durataSessione.toMinutes() % 60) + ":" + String.format("%02d", durataSessione.toSeconds() % 60) +
                "\nvincita percentuale: " + vincitaPercentuale + "\npartite svolte " + partiteSvolte;
    }

    /**
     * Gets id tavolo.
     *
     * @return the id tavolo
     */
    public int getIdTavolo() {
        return idTavolo;
    }

    /**
     * Gets id sessione.
     *
     * @return the id sessione
     */
    public int getIdSessione() {
        return idSessione;
    }
}
