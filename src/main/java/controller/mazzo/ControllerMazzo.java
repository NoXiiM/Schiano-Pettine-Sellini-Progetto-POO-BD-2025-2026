package controller.mazzo;

import controller.blackjack.DeckOut;
import model.gestionale.Gioco;
import model.giochi.Carte.*;

import java.util.ArrayList;

/**
 * Controller realizzato per gestire genericamente la logica di tutti i giochi di carte
 */
public abstract class ControllerMazzo
{
    /**
     * Un Sabot è composto da molteplici mazzi, per comodità l'attributo lo chiamo mazzo
     */
    protected Sabot mazzo;
    /**
     * Lista delle mani dei giocatori
     */
    protected ArrayList<Mano> listaMani = new ArrayList<>();

    /**
     * Instantiates a new Controller mazzo.
     *
     * @param nmazzi quanti mazzi contiene il sabot
     * @param nmani  numero di giocatori/mani
     * @param gioco  gioco per il quale è istanziato il controller
     */
    public ControllerMazzo(int nmazzi, int nmani, Gioco gioco)
    {
        //System.out.println(nmazzi);
        mazzo = new Sabot(nmazzi, gioco);

        for(int i = 0; i < nmani; i++)
        {
            this.addMano(creaMano(gioco));
        }
    }

    /**
     * Verifica se la cutting card è stata raggiunta e quindi se bisogna rimischiare il mazzo, controllo effettuato
     * solitamente a fine round
     *
     * @return true: cutting card raggiunta, false: non raggiunta
     */
    public boolean controlloCuttingCard()
    {
        return mazzo.controlloRimischiaMazzo();
    }

    /**
     * Funzione per resettare mazzo
     */
    public abstract void reinizializzaMazzo();

    /**
     * Funzione per istanziare mano di un gioco
     *
     * @param gioco the gioco
     * @return mano istanziata
     */
    protected Mano creaMano(Gioco gioco)
    {
        if(gioco.equals(Gioco.Blackjack)) return new ManoBlackJack();
        if(gioco.equals(Gioco.Poker)) return new ManoPoker();
        return null;
    }

    /**
     * Aggiunge mano a lista mano
     *
     * @param nuova nuova mano
     */
    protected void addMano(Mano nuova)
    {
        listaMani.add(nuova);
    }

    /**
     * Serve una carta dal mazzo al giocatore
     *
     * @param ricevitore mano giocatore
     * @throws DeckOut the deck out
     */
    public void serviCarta(Mano ricevitore) throws DeckOut
    {
        try {
            ricevitore.riceviCarta(mazzo.serviCartaDaMazzo());
        } catch (DeckOut e) {
            reinizializzaMazzo();
            throw new DeckOut(e.getMessage());
        }
    }

    /**
     * Funzione generica che associa un valore intero al numero della carta
     *
     * @param carta the carta
     * @return valore numero
     */
//funzione di mapping delle carte generica
    protected static int getValoreNumero(Carta carta) {
        Numero valCarta = carta.getNumero();

        if(valCarta.equals(Numero.uno)) return 1;
        if(valCarta.equals(Numero.due)) return 2;
        if(valCarta.equals(Numero.tre)) return 3;
        if(valCarta.equals(Numero.quattro)) return 4;
        if(valCarta.equals(Numero.cinque)) return 5;
        if(valCarta.equals(Numero.sei)) return 6;
        if(valCarta.equals(Numero.sette)) return 7;
        if(valCarta.equals(Numero.otto)) return 8;
        if(valCarta.equals(Numero.nove)) return 9;
        if(valCarta.equals(Numero.dieci)) return 10;
        if(valCarta.equals(Numero.jack)) return 11;
        if(valCarta.equals(Numero.queen)) return 12;
        if(valCarta.equals(Numero.king)) return 13;
        return -1;
    }

    /**
     * Funzione che calcola il path dell'immagine di una determinata carta in base al seme e al numero della carta
     *
     * @param imano  indice mano
     * @param icarta indice carta
     * @return path immagine rispettiva
     */
//funzione display card per giocatori
    public String displayCard(int imano, int icarta)
    {
        String path = "/Carte2/";
        int num = 0;

        Carta carta = listaMani.get(imano).getCarta(icarta);
        Seme seme = listaMani.get(imano).getCarta(icarta).getSeme();


        switch(seme)
        {
            case Seme.cuore:
                break;
            case Seme.picche:
                num = 14;
                break;
            case Seme.quadro:
                num = 28;
                break;
            case Seme.fiore:
                num = 42;
                break;
        }

        num += ControllerMazzo.getValoreNumero(carta);
        String numString = String.format("%02d", num);

        path += numString + "_kerenel_Cards.png";

        return path;
    }

    /**
     * Ritorna la mano a un certo indice da listaMani
     *
     * @param index indice
     * @return mano
     */
    public Mano getMano(int index)
    {
        return listaMani.get(index);
    }


    /**
     * Funzione che effettua un reset delle mani generico, si cancella il vecchio Arraylist di Mano
     *
     * @param gioco gioco interessato
     */
    public void resettaMani(Gioco gioco)
    {
        int nmani = listaMani.size();
        listaMani = new ArrayList<>();

        for(int i = 0; i < nmani; i++)
        {
            this.addMano(creaMano(gioco));
        }
    }
}
