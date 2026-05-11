package model.utenteEFigli;

import java.util.Date;

public class Supervisore extends  Dipendente
{
    /*
    da fare:
        metodi:
        accediStorico
        bannaGiocatore
    */


    public Supervisore(String nome, String cognome, String codiceFiscale,
                       Date dataDiNascita, String password,
                       String identificativoDipendente)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
}
