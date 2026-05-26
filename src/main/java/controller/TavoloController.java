package controller;

import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import java.util.ArrayList;

public class TavoloController
{
    ArrayList<Tavolo> listaTavoli;

    public TavoloController()
    {
        this.listaTavoli = new ArrayList<>();
    }

    public void popolaBlackJack()
    {
        listaTavoli.add(new Tavolo(1, Gioco.BlackJack, 5));
        listaTavoli.add(new Tavolo(2, Gioco.BlackJack, 4));
        listaTavoli.add(new Tavolo(3, Gioco.BlackJack, 4));
    }
    public void popolaSlotMachine()
    {
        listaTavoli.add(new Tavolo(1, Gioco.SlotMachine, 1));
        listaTavoli.add(new Tavolo(2, Gioco.SlotMachine, 0));
        listaTavoli.add(new Tavolo(3, Gioco.SlotMachine, 1));
    }

    public int getNumeroPosti(int index)
    {
        return listaTavoli.get(index).getNumeroPosti();
    }

    public Tavolo getTavolo(int index)
    {
        return listaTavoli.get(index);
    }
}
