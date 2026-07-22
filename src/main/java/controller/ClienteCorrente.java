package controller;

import model.gestionale.Giocatore;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;

import java.sql.Time;

public class ClienteCorrente
{
    Cliente clienteCorrente;
    Sessione sessioneCorrente;
    public ClienteCorrente(Cliente clienteCorrente) {
        this.clienteCorrente = clienteCorrente;
    }

    public Cliente getClienteCorrente() {
        return clienteCorrente;
    }

    public int getSaldoCorrente()
    {
        return clienteCorrente.getSaldo();
    }

    public void creaNuovaSessioneDiGioco(Tavolo tavoloSelezionato)
    {
        Giocatore giocatoreCorrente = new Giocatore(clienteCorrente, clienteCorrente.getSaldo());
        sessioneCorrente = new Sessione(giocatoreCorrente, tavoloSelezionato);
        sessioneCorrente.startTimer();
    }

    public int getSaldoGiocatore(){ return sessioneCorrente.getSaldoGiocatore();}
    public void decrementaSaldoGiocatore(int creditoInserito) throws RuntimeException{sessioneCorrente.decrementaSaldoGiocatore(creditoInserito);}
    public void incrementaSaldoGiocatore(int creditoInserito){sessioneCorrente.incrementaSaldoGiocatore(creditoInserito);}
    //public void aggiornaDatiCliente(){sessioneCorrente.aggiornaDatiCliente();}
    public void terminaSessione(){sessioneCorrente.terminaSessione();}
    public void aggiornaVincitaPercentuale(boolean v){ sessioneCorrente.aggiornaVincitaPercentuale(v);}
    //public void startTimer(){sessioneCorrente.startTimer();}
    public void stopTimer(){sessioneCorrente.stopTimer();}
    public int getPostiTavolo(){return sessioneCorrente.getPostiTavolo();}
    public int getTimeSecondi(){return sessioneCorrente.getTimeSecondi();}
    public Time getTime(){return sessioneCorrente.getTime();}
    public String stringaPercentuale(){return sessioneCorrente.stringaPercentuale();}
    public boolean isBanned(){return clienteCorrente.getBan() != null;}}
