package model.gestionale.utenteEFigli;

import java.time.LocalDate;
import java.util.Date;

public class Base extends Cliente
{
    private boolean sospetto;

    public Base(String username, String nome, String cognome, String codiceFiscale,
                LocalDate dataDiNascita, String password,
                String codiceTesseraGiocatore)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        sospetto = false;
    }
}
