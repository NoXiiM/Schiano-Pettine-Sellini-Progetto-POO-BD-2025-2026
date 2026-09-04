package model.gestionale;

import model.gestionale.utenteEFigli.Cliente;

/**
 * Giocatore è stata pensata come classe che potesse fare da strato tra i dati di gestione del Cliente e il gioco, quindi
 * è una classe che riprende solo gli attributi di cliente che servono ai fini del gioco, le fiches (conversione 1:1 da saldo)
 * e le fichesGiocate durante la sessione (Giocatore viene creato quando inizia una sessione), poi si mantiene un riferimento
 * al cliente per cui è stata istanziata la classe con clienteAssociato
 */
public class Giocatore
{
    private int fiches;

    //attributi da associazioni
    private final Cliente clienteAssociato;
    private int fichesGiocate;

    /**
     * Istanzia un Giocatore
     *
     * @param clienteAssociato the cliente associato
     * @param fiches            the saldo
     */
    public Giocatore(Cliente clienteAssociato, int fiches)
    {
        this.fiches = fiches;
        this.clienteAssociato = clienteAssociato;
        fichesGiocate = 0;
    }

    /**
     * Gets fiches.
     *
     * @return the fiches
     */
    public int getFiches() {
        return fiches;
    }

    /**
     * Funzione che incrementa le fiches
     *
     * @param fiches the fiches
     */
    public void incrementaFiches(int fiches)
    {
        this.fiches += fiches;
    }

    /**
     * Funzione che aggiorna le statistiche di gioco del cliente associato al termine della sessione
     */
    public void chiudiSessione()
    {
        clienteAssociato.setSaldo(fiches);
        clienteAssociato.incrementaFichesGiocate(fichesGiocate);
    }

    /**
     * Gets cliente associato.
     *
     * @return the cliente associato
     */
    public Cliente getClienteAssociato() {
        return clienteAssociato;
    }

    /**
     * Incrementa fiches giocate.
     *
     * @param val the val
     */
    public void incrementaFichesGiocate(int val)
    {
        fichesGiocate += val;
    }
}
