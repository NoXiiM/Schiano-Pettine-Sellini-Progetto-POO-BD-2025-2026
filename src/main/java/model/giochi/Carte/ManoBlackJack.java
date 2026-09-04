package model.giochi.Carte;

import model.gestionale.Gioco;

/**
 * Mano di black jack
 */
public class ManoBlackJack extends Mano
{
    private HandStateBJ flag;
    private int sideBet;

    /**
     * Istanzia una nuova mano di black jack
     */
    public ManoBlackJack(){
        super(Gioco.Blackjack);
        this.flag = HandStateBJ.normale;
    }

    /**
     * Gets flag.
     *
     * @return the flag
     */
    public HandStateBJ getFlag() {
        return flag;
    }

    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    public void setFlag(HandStateBJ flag) {
        this.flag = flag;
    }

    /**
     * Sets side bet.
     *
     * @param sideBet the side bet
     */
    public void setSideBet(int sideBet) {
        this.sideBet = sideBet;
    }

    /**
     * Gets side bet.
     *
     * @return the side bet
     */
    public int getSideBet() {
        return sideBet;
    }

    /**
     * Funzione che raddoppia la puntata
     */
    public void raddoppio()
    {
        puntata *= 2;
    }

    /**
     * Funzione che rimuove l'ultima carta dalla lista mano e la ritorna
     *
     * @return ultima carta che stava in lista mano
     */
    public Carta traslaCarta()
    {
        Carta temp = listaMano.getLast();
        listaMano.removeLast();

        return temp;
    }
}
