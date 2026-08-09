package controller.poker;

import controller.blackjack.DeckOut;
import controller.mazzo.ControllerMazzo;
import database.implementazioneDAO.ImpDAOop;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.Mano;
import model.giochi.ManoPoker;
import model.giochi.Sabot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ControllerPoker extends ControllerMazzo
{
    private int puntataAttuale;
    private int pot;
    private int ante;
    private boolean almenoUnGiro;

    public ControllerPoker(int nmani)
    {
        super(1, nmani, Gioco.Poker);

        mazzo.inizializzaSabotPoker(nmani);
        mazzo.mischiaMazzo();

        puntataAttuale = 0;
        pot = 0;
        ante = 0;
    }

    @Override
    public void reinizializzaMazzo() {
        mazzo = new Sabot(mazzo.getNumeroDiMazzi(), mazzo.getGioco());
        mazzo.inizializzaSabotPoker(listaMani.size());
        mazzo.mischiaMazzo();
    }

    public void serviCarte() throws DeckOut
    {
        for(Mano i : listaMani)
        {
            for(int j = 0; j < 5; j++) serviCarta(i);
        }
    }

    public Cliente caricaPlayer(String username, String password) throws SQLException
    {
        ImpDAOop db = new ImpDAOop();

        String[] codiceTessera = new String[1];
        int[] saldo = new int[1];
        long[] tempoDiGioco = new long[1];
        int[] fichesGiocate = new int[1];
        double[] vincitaPercentualeTot = new double[1];
        int[] partiteGiocate = new int[1];
        String[] tipo = new String[1];
        double[] scontoPokerPercentuale = new double[1];
        boolean[] sospetto = new boolean[1];
        LocalDate[] dataDiBan = new LocalDate[1];
        String[] motiviBan = new String[1];
        String[] nome = new String[1];
        String[] cognome = new String[1];
        String[] codiceFiscale = new String[1];
        LocalDate[] dataDiNascita = new LocalDate[1];

        if(db.loginCliente(codiceTessera, saldo, tempoDiGioco, fichesGiocate, vincitaPercentualeTot,
                partiteGiocate, tipo, scontoPokerPercentuale, sospetto, dataDiBan, motiviBan, nome,
                cognome, codiceFiscale, dataDiNascita, username, password))
        {
            boolean flag = tipo[0].equals("Premium");

            return new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                    codiceTessera[0], flag, scontoPokerPercentuale[0], sospetto[0], tempoDiGioco[0], fichesGiocate[0],
                    saldo[0], partiteGiocate[0], dataDiBan[0], motiviBan[0]);
        }
        return null;
    }

    public void rimischiataMano(int index)
    {
        ManoPoker mano = (ManoPoker) listaMani.get(index);

        mano.rimuoviCarte();

        while(mano.getListaMano().size() < 5)
        {
            serviCarta(mano);
        }
    }

    public int getPuntataAttuale() {
        return puntataAttuale;
    }

    public void setPuntataAttuale(int puntataAttuale) {
        this.puntataAttuale = puntataAttuale;
    }

    //se rimane un solo giocatore ne restituisce la posizione
    public Integer setFolded(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        int contatore = 0;
        Integer indiceVincitore = null;

        temp.setFolded(true);

        for(int i = 0; i < listaMani.size(); i++)
        {
            ManoPoker j = (ManoPoker) listaMani.get(i);
            if(!j.isFolded())
            {
                contatore++;
                indiceVincitore = i;
            }
        }

        if(contatore == 1) return indiceVincitore;
        else return null;
    }

    public boolean controlloStessePuntate()
    {
        ArrayList<Integer> listaPuntate = new ArrayList<>();

        for(Mano i : listaMani)
        {
            ManoPoker temp = (ManoPoker) i;

            if(!temp.isFolded()) listaPuntate.add(temp.getPuntata());
        }

        int val = listaPuntate.get(0);

        for(Integer i : listaPuntate)
        {
            if(val != i) return false;
        }

        return true;
    }

    public void resetPuntateMani()
    {
        for(Mano i : listaMani)
        {
            i.setPuntata(0);
        }
    }

    public boolean getFolded(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);

        return temp.isFolded();
    }

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getPot() {
        return pot;
    }

    public void incrementaPot(int incremento) {
        pot += incremento;
    }

    public void resetPot()
    {
        pot = 0;
    }

    public boolean isAlmenoUnGiro() {
        return almenoUnGiro;
    }

    public void setAlmenoUnGiro(boolean almenoUnGiro) {
        this.almenoUnGiro = almenoUnGiro;
    }
}
