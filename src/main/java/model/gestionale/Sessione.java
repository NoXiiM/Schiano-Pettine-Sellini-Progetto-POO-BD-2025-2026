package model.gestionale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;

public class Sessione
{
    private double vincitaPercentuale;
    //in più rispetto a uml
    private int partiteSvolte;
    //in più rispetto a uml
    private Timer tempoDiGioco;
    private int secondiPassati;

    //gestione tempo/
    private Instant inizioCronometro;
    private Duration durata;

    //attributi da associazioni
    private Giocatore giocatore;
    private Tavolo tavolo;


    public Sessione(Giocatore giocatore, Tavolo tavolo)
    {
        durata = Duration.ZERO;
        partiteSvolte = 0;
        this.giocatore = giocatore;
        this.tavolo = tavolo;

        secondiPassati = 0;
    }

    //timer
    public void startTimer()
    {
        inizioCronometro = Instant.now();
    }

    public int getTimeSecondi()
    {
        return secondiPassati;
    }

    public Duration getTime()
    {
        return durata;
    }

    public void stopTimer()
    {
        durata = Duration.between(inizioCronometro, Instant.now());
    }

    //giocatore
    public int getSaldoGiocatore()
    {
        return giocatore.getFiches();
    }

    public void decrementaSaldoGiocatore(int val) throws RuntimeException
    {
        if(val <= giocatore.getFiches())
        {
            giocatore.incrementaFiches(-val);
            giocatore.incrementaFichesGiocate(val);
        }
        else throw new RuntimeException("Saldo insufficiente");
    }

    public void incrementaSaldoGiocatore(int val)
    {
        giocatore.incrementaFiches(val);
    }

    public void terminaSessione()//Aggiorna il saldo del giocatore in utente
    {
        giocatore.chiudiSessione();
        stopTimer();
        aggiornaDatiCliente();
    }

    //true = win, false = loss
    public void aggiornaVincitaPercentuale(boolean vittoria)
    {
        partiteSvolte += 1;
        double suc;
        if(vittoria) suc = 100;
        else suc = 0;

        if(partiteSvolte == 1) vincitaPercentuale = suc;
        else vincitaPercentuale = (vincitaPercentuale * (partiteSvolte -1) + suc)/ partiteSvolte;
    }

    public double getVincitaPercentuale()
    {
        return vincitaPercentuale;
    }

    public String stringaPercentuale()
    {
        return vincitaPercentuale + " %";
    }

    public void aggiornaDatiCliente()
    {
        giocatore.getClienteAssociato().aggiornaPercentualeVittoria(vincitaPercentuale, partiteSvolte);
        giocatore.getClienteAssociato().aggiornaTempoDiGioco(durata);
        if(giocatore.getClienteAssociato().convertiPremium())
        {
            giocatore.getClienteAssociato().setPremium(true);
        }
    }

    public int getPostiTavolo()
    {
        return tavolo.getNumeroPosti();
    }
}
