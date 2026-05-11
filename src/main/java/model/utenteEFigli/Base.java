package model.utenteEFigli;

import java.util.Date;

public class Base extends Cliente
{
    private boolean sospetto;

    public Base(String nome, String cognome, String codiceFiscale,
                Date dataDiNascita, String password,
                String codiceTesseraGiocatore)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        sospetto = false;
    }
}
