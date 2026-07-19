package model.gestionale.utenteEFigli;

import java.time.LocalDate;

public class Premium extends Cliente
{
    private double scontoPokerPercentuale;

    public Premium(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password,
                   String codiceTesseraGiocatore)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        scontoPokerPercentuale = 0.5;
    }
}
