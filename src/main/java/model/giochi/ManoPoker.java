package model.giochi;

import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Collections;

public class ManoPoker extends Mano
{
    private boolean folded;
    private ArrayList<Integer> carteSelezionate;
    private int[] valoreCombo;

    public ManoPoker(Gioco gioco)
    {
        super(gioco);

        carteSelezionate = new ArrayList<>();
        folded = false;

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

    public void rimuoviCarte()
    {
        carteSelezionate.sort(Collections.reverseOrder());

        for(int i : carteSelezionate)
        {
            listaMano.remove(i);
        }
    }
}
