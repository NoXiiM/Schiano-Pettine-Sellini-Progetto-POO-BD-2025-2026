package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.sql.Time;
import java.util.Date;

/*
    da fare:
        metodi:
        convertiInFiches
*/

public abstract class Cliente extends Utente
{
    protected String codiceTesseraGiocatore;
    protected Time tempoDiGioco;
    protected int fichesGiocate;
    protected double vincitaPercentualeTot;

    //attributi da associazioni
    private Ban ban;

    public Cliente(String nome, String cognome, String codiceFiscale,
                   Date dataDiNascita, String password, String codiceTesseraGiocatore)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password);
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
        tempoDiGioco = new Time(0, 0, 0);
        fichesGiocate = 0;
        vincitaPercentualeTot = 0;
        ban = null;
    }
}
