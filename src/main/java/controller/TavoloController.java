package controller;

import database.implementazioneDAO.ImpDAOopc;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import java.sql.SQLException;
import java.util.ArrayList;

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
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.Blackjack, idTavoli, numeroPosti, idDealer);

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
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.SlotMachine, idTavoli, numeroPosti, idDealer);
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

    public void popolaPoker() throws SQLException
    {
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.Poker, idTavoli, numeroPosti, idDealer);

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            if(idDealerCorrente != null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.Blackjack, numeroPosti.get(i)));
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

    public int pagaTavoloPoker(int id, Double sconto)
    {
        Tavolo tavoloScelto = getTavoloWithId(id);

        int somma = 20 + tavoloScelto.getNumeroPosti() * 10;

        return (int)(somma - (somma * sconto));
    }
}
