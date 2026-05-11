package model;

import java.sql.Time;

public class Sessione
{
    private Time durata;
    private double vincitaPercentuale;

    //attributi da associazioni
    private Giocatore giocatore;
    private Tavolo tavolo;

    public Sessione(Giocatore giocatore, Tavolo tavolo)
    {
        durata = new Time(0, 0, 0);
        vincitaPercentuale = 0;
        this.giocatore = giocatore;
        this.tavolo = tavolo;
    }
}
