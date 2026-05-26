package model.gestionale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;

public class Sessione
{
    private Time durata;
    private double vincitaPercentuale;

    //attributi da associazioni
    private Giocatore giocatore;
    private Tavolo tavolo;

    private Timer tempoDiGioco;
    private int secondiPassati;

    public Sessione(Giocatore giocatore, Tavolo tavolo)
    {
        durata = new Time(0, 0, 0);
        vincitaPercentuale = 0;
        this.giocatore = giocatore;
        this.tavolo = tavolo;

        secondiPassati = 0;
    }

    //timer
    public void startTimer()
    {
        //il delay è contato dalla classe in millisecondi
        tempoDiGioco = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondiPassati++;
            }
        });

        tempoDiGioco.start();
    }

    public int getTimeSecondi()
    {
        return secondiPassati;
    }

    public Time getTime()
    {
        return durata;
    }

    public void stopTimer()
    {
        tempoDiGioco.stop();

        durata.setTime(secondiPassati*1000);
    }

    //giocatore
    public int getSaldoGiocatore()
    {
        return giocatore.getFiches();
    }

    public void decrementaSaldoGiocatore(int val) throws RuntimeException
    {
        if(val <= giocatore.getFiches()) giocatore.incrementaFiches(-val);
        else throw new RuntimeException("Saldo insufficiente");
    }

    public void incrementaSaldoGiocatore(int val)
    {
        giocatore.incrementaFiches(val);
    }
}
