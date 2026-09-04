package model.giochi.Carte;

import model.gestionale.Gioco;

import java.util.ArrayList;

/**
 * Mano generica per qualsiasi gioco di carte
 */
public abstract class Mano
{
    /**
     * The Gioco.
     */
    protected Gioco gioco;
    /**
     * The Larghezza mano.
     */
    protected Integer larghezzaMano;
    /**
     * Lista delle carte nella mano
     */
    protected ArrayList<Carta> listaMano;
    /**
     * The Puntata.
     */
    protected int puntata;

    /**
     * Istanzia una nuova mano
     *
     * @param gioco the gioco
     */
    public Mano(Gioco gioco) {
        if(gioco == Gioco.Poker)
        {
            larghezzaMano = 5;
        }
        if(gioco == Gioco.Blackjack)
        {
            larghezzaMano = null;
        }
        this.gioco = gioco;
        puntata = 0;
        listaMano = new ArrayList<>();
    }

    /**
     * Gets carta.
     *
     * @param indice the indice
     * @return the carta
     */
    public Carta getCarta(int indice)
    {
        return listaMano.get(indice);
    }

    /**
     * Funzione per ricevere una nuova carta alla mano
     *
     * @param nuova carta aggiunta in listaMano
     */
    public void riceviCarta(Carta nuova)
    {
        //aggiungere controllo su dimensione mano
        //controllo ogni pescata
        listaMano.add(nuova);
    }

    /**
     * Gets gioco.
     *
     * @return the gioco
     */
    public Gioco getGioco() {
        return gioco;
    }

    /**
     * Gets lista mano.
     *
     * @return the lista mano
     */
    public ArrayList<Carta> getListaMano() {
        return listaMano;
    }

    /**
     * Get dimensione mano int.
     *
     * @return the int
     */
    public int getDimensioneMano(){return listaMano.size();}

    /**
     * Sets puntata.
     *
     * @param puntata the puntata
     */
    public void setPuntata(int puntata) {
        this.puntata = puntata;
    }

    /**
     * Incrementa puntata.
     *
     * @param val the val
     */
    public void incrementaPuntata(int val){puntata += val;}

    /**
     * Gets puntata.
     *
     * @return the puntata
     */
    public int getPuntata() {
        return puntata;
    }

    /**
     * Add carta.
     *
     * @param carta the carta
     */
    public void addCarta(Carta carta)
    {
        listaMano.add(carta);
    }
}
