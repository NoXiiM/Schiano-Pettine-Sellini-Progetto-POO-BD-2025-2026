package controller;

import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import java.util.ArrayList;

public class TavoloController
{
    ArrayList<Tavolo> listaTavoliBJ;

    public TavoloController()
    {
        this.listaTavoliBJ = new ArrayList<>();
    }

    public void popolaLista()
    {
        listaTavoliBJ.add(new Tavolo(1, Gioco.BlackJack, 5));
        listaTavoliBJ.add(new Tavolo(2, Gioco.BlackJack, 4));
        listaTavoliBJ.add(new Tavolo(3, Gioco.BlackJack, 4));
    }

    public int getNumeroPosti(int index)
    {
        return listaTavoliBJ.get(index).getNumeroPosti();
    }
}
