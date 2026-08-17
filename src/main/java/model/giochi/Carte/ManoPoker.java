package model.giochi.Carte;

import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Collections;

public class ManoPoker extends Mano
{
    private boolean folded;
    //carte selezionate in rimischia carte
    private ArrayList<Integer> carteSelezionate;
    private int[] valoreCombo;
    private Integer sidePot;
    private int puntataTotalePartita;
    private boolean allIn;

    public ManoPoker(Gioco gioco)
    {
        super(gioco);

        carteSelezionate = new ArrayList<>();
        folded = false;
        allIn = false;
        sidePot = null;
        puntataTotalePartita = 0;

        valoreCombo = new int[2];
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public void aumentaPuntata(int valore)
    {
        puntata += valore;
    }

    public ArrayList<Integer> getCarteSelezionate() {
        return carteSelezionate;
    }

    public void setCarteSelezionate(ArrayList<Integer> carteSelezionate) {
        this.carteSelezionate = carteSelezionate;
    }

    public int[] getValoreCombo() {
        return valoreCombo;
    }

    public void setValoreCombo(int[] valoreCombo) {
        this.valoreCombo = valoreCombo;
    }

    public Integer getSidePot() {
        return sidePot;
    }

    public void setSidePot(Integer sidePot) {
        this.sidePot = sidePot;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public int getPuntataTotalePartita() {
        return puntataTotalePartita;
    }

    public void incrementaPuntataTotalePartita(int valore) {
        puntataTotalePartita += valore;
    }

    public void rimuoviCarte()
    {
        carteSelezionate.sort(Collections.reverseOrder());

        for(int i : carteSelezionate)
        {
            listaMano.remove(i);
        }
    }
}
