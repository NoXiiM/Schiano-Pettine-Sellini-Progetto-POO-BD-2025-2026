package controller;

import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Dealer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class TavoloController
{
    ArrayList<Tavolo> listaTavoli;
    ArrayList<String> tavoliNumber;
    public TavoloController()
    {
        this.listaTavoli = new ArrayList<>();
        this.tavoliNumber = new ArrayList<>();
    }

    public void popolaBlackJack() throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoli(Gioco.Blackjack, idTavoli, numeroPosti, idDealer);

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            if(idDealerCorrente != null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.Blackjack, numeroPosti.get(i)));
            }
        }
    }

    public void popolaSlotMachine() throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoli(Gioco.SlotMachine, idTavoli, numeroPosti, idDealer);
        //System.out.println(idTavoli.size());

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            //System.out.println(idDealer.get(i).equals("null"));

            if(idDealerCorrente == null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.SlotMachine, numeroPosti.get(i)));
            }
        }
    }

    public int getNumeroPosti(int index)
    {
        return listaTavoli.get(index).getNumeroPosti();
    }

    public int getIdFromList(String idTavolo)
    {
        int id = Integer.parseInt(idTavolo.replaceAll("[^0-9]", ""));

        return id;
    }

    public Tavolo getTavolo(int index)
    {
        return listaTavoli.get(index);
    }

    public Tavolo getTavoloWithId(int id){
        for(Tavolo i : listaTavoli)
        {
            if(i.getIdTavolo() == id) return i;
        }

        return null;
    }

    public ArrayList<String> getTavoliId()
    {
        ArrayList<String> info = new ArrayList<>();

        for(Tavolo i : listaTavoli)
        {
            info.add("tavolo " + i.getIdTavolo());
        }

        return info;
    }
}
