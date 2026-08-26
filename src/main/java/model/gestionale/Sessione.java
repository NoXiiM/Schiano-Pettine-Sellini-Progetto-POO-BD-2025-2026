package model.gestionale;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

public class Sessione
{
    private double vincitaPercentuale;
    //in più rispetto a uml
    private int partiteSvolte;

    //gestione tempo/
    private Instant inizioCronometro;
    private Duration durataSessione;

    //attributi da associazioni
    private Giocatore giocatore;
    private Tavolo tavolo;

    private int idTavolo;
    private int idSessione;

    public Sessione(Giocatore giocatore, Tavolo tavolo)
    {
        durataSessione = Duration.ZERO;
        partiteSvolte = 0;
        this.giocatore = giocatore;
        this.tavolo = tavolo;
        idTavolo = tavolo.getIdTavolo();
    }

    public Sessione(int idSessione, int idTavolo, Duration durata,
                    double vincitaPercentuale, int partiteSvolte)
    {
        this.idSessione = idSessione;
        this.idTavolo = idTavolo;
        durataSessione = durata;
        this.vincitaPercentuale = vincitaPercentuale;
        this.partiteSvolte = partiteSvolte;
    }

    //timer
    public void startTimer()
    {
        inizioCronometro = Instant.now();
    }

    public Duration getDurataSessione()
    {
        return durataSessione;
    }

    public void stopTimer()
    {
        durataSessione = Duration.between(inizioCronometro, Instant.now());
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

    public boolean terminaSessione()//Aggiorna il saldo del giocatore in utente
    {
        giocatore.chiudiSessione();
        stopTimer();
        return aggiornaDatiCliente();
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

    public boolean aggiornaDatiCliente()
    {
        giocatore.getClienteAssociato().aggiornaPercentualeVittoria(vincitaPercentuale, partiteSvolte);
        giocatore.getClienteAssociato().aggiornaTempoDiGioco(durataSessione);
        if(giocatore.getClienteAssociato().convertiPremium())
        {
            giocatore.getClienteAssociato().setPremium(true);
            return true;
        }
        return false;
    }

    public int getPostiTavolo()
    {
        return tavolo.getNumeroPosti();
    }

    public Tavolo getTavolo(){
        return tavolo;
    }

    public int getPartiteSvolte() {
        return partiteSvolte;
    }

    @Override
    public String toString() {
        return "id Sessione: " + idSessione + " id Tavolo: " + idTavolo;
//                idSessione idTavolo durataSes,
//         vincitaPercentuale partiteSvolte
    }

    public String infoSessione()
    {
        return "durata: " + durataSessione.toHours() + ":" +
                String.format("%02d", durataSessione.toMinutes()) + ":" + String.format("%02d", durataSessione.toSeconds()) +
                "\nvincita percentuale: " + vincitaPercentuale + "\npartite svolte " + partiteSvolte;
    }

    public int getIdTavolo() {
        return idTavolo;
    }

    public int getIdSessione() {
        return idSessione;
    }
}
