package controller.mazzo;

import model.giochi.Mano;
import model.giochi.Sabot;

import java.util.ArrayList;

public abstract class ControllerMazzo
{
    private Sabot mazzo;
    private ArrayList<Mano> listaMani = new ArrayList<>();

    public ControllerMazzo(Sabot mazzo)
    {
        this.mazzo = mazzo;
    }

    public void addMano(Mano nuova)
    {
        listaMani.add(nuova);
    }

    public void serviCarta(Mano ricevitore)
    {
        ricevitore.riceviCarta(mazzo.serviCarta());
    }
}
