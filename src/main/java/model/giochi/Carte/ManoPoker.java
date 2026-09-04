package model.giochi.Carte;

import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Mano di poker
 */
public class ManoPoker extends Mano
{
    private boolean folded;
    //carte selezionate in rimischia carte
    private ArrayList<Integer> carteSelezionate;
    private int[] valoreCombo;
    private Integer sidePot;
    private int puntataTotalePartita;
    private boolean allIn;

    /**
     * Istanzia una nuova mano di poker, valore combo è un array di 6 interi perché registra il valore della combo della
     * mano + fino a 5 tie-breaker possibili
     */
    public ManoPoker()
    {
        super(Gioco.Poker);

        carteSelezionate = new ArrayList<>();
        folded = false;
        allIn = false;
        sidePot = null;
        puntataTotalePartita = 0;

        valoreCombo = new int[6];
    }

    /**
     * Is folded boolean.
     *
     * @return the boolean
     */
    public boolean isFolded() {
        return folded;
    }

    /**
     * Sets folded.
     *
     * @param folded the folded
     */
    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    /**
     * Gets carte selezionate.
     *
     * @return the carte selezionate
     */
    public ArrayList<Integer> getCarteSelezionate() {
        return carteSelezionate;
    }

    /**
     * Sets carte selezionate.
     *
     * @param carteSelezionate the carte selezionate
     */
    public void setCarteSelezionate(ArrayList<Integer> carteSelezionate) {
        this.carteSelezionate = carteSelezionate;
    }

    /**
     * Get valore combo int [ ].
     *
     * @return the int [ ]
     */
    public int[] getValoreCombo() {
        return valoreCombo;
    }

    /**
     * Sets valore combo.
     *
     * @param valoreCombo the valore combo
     */
    public void setValoreCombo(int[] valoreCombo) {
        this.valoreCombo = valoreCombo;
    }

    /**
     * Gets side pot.
     *
     * @return the side pot
     */
    public Integer getSidePot() {
        return sidePot;
    }

    /**
     * Sets side pot.
     *
     * @param sidePot the side pot
     */
    public void setSidePot(Integer sidePot) {
        this.sidePot = sidePot;
    }

    /**
     * Is all in boolean.
     *
     * @return the boolean
     */
    public boolean isAllIn() {
        return allIn;
    }

    /**
     * Sets all in.
     *
     * @param allIn the all in
     */
    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    /**
     * Gets puntata totale partita.
     *
     * @return the puntata totale partita
     */
    public int getPuntataTotalePartita() {
        return puntataTotalePartita;
    }

    /**
     * Incrementa puntata totale partita.
     *
     * @param valore the valore
     */
    public void incrementaPuntataTotalePartita(int valore) {
        puntataTotalePartita += valore;
    }

    /**
     * Funzione per rimuovere le carte selezionate in fase di rimischiata dalla mano di poker, carteSelezionate va ordinato
     * dall'indice più alto a quello più basso in maniera tale che non ci sia uno sfasamento di indici con i remove
     */
    public void rimuoviCarte()
    {
        carteSelezionate.sort(Collections.reverseOrder());

        for(int i : carteSelezionate)
        {
            listaMano.remove(i);
        }
    }
}
