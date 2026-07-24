package controller;

import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import java.util.ArrayList;
import java.util.HashMap;

public class TavoloController
{
    ArrayList<Tavolo> listaTavoli;
    ArrayList<String> tavoliNumber;
    HashMap<String,Tavolo> tavoloCorrispondente;
    public TavoloController()
    {
        this.listaTavoli = new ArrayList<>();
        this.tavoliNumber = new ArrayList<>();
        this.tavoloCorrispondente = new HashMap<>();
    }

    public void popolaBlackJack()
    {
        listaTavoli.add(new Tavolo(1, Gioco.BlackJack, 5));
        listaTavoli.add(new Tavolo(2, Gioco.BlackJack, 4));
        listaTavoli.add(new Tavolo(3, Gioco.BlackJack, 4));
    }
    public void popolaSlotMachine()
    {
        //Ancora da definire come prendere i tavoli e interagirvi per determinare a quale ci si riferisce
        Tavolo t = new Tavolo(1, Gioco.SlotMachine, 1);
        listaTavoli.add(t);
        String s = "tavolo"+t.getIdTavolo();
        tavoliNumber.add(s);
        tavoloCorrispondente.put(s,t);

        t = new Tavolo(2, Gioco.SlotMachine, 0);
        listaTavoli.add(t);
        s = "tavolo"+t.getIdTavolo();
        tavoliNumber.add(s);
        tavoloCorrispondente.put(s,t);

        t = new Tavolo(3, Gioco.SlotMachine, 1);
        listaTavoli.add(t);
        s = "tavolo"+t.getIdTavolo();
        tavoliNumber.add(s);
        tavoloCorrispondente.put(s,t);
    }

    public int getNumeroPosti(int index)
    {
        return listaTavoli.get(index).getNumeroPosti();
    }

    public Tavolo getTavolo(int index)
    {
        return listaTavoli.get(index);
    }

    public ArrayList<String> getTavoliNumber() {
        return tavoliNumber;
    }
    public String geTavoloCorrispondente(String s){return tavoloCorrispondente.get(s).toString();}
}
