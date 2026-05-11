package model.utenteEFigli;

import java.util.Date;

public class Premium extends Cliente
{
    private double scontoPokerPercentuale;

    public Premium(String nome, String cognome, String codiceFiscale,
                   Date dataDiNascita, String password,
                   String codiceTesseraGiocatore, double scontoPokerPercentuale)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        this.scontoPokerPercentuale = scontoPokerPercentuale;
    }

    public Premium(String nome, String cognome, String codiceFiscale,
                   Date dataDiNascita, String password,
                   String codiceTesseraGiocatore)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        scontoPokerPercentuale = 0.5;
    }
}
