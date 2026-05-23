package model.gestionale.utenteEFigli;

import java.time.LocalDate;
import java.util.Date;

public class Supervisore extends  Dipendente
{
    /*
    da fare:
        metodi:
        accediStorico
        bannaGiocatore
    */


    public Supervisore(String username, String nome, String cognome, String codiceFiscale,
                       LocalDate dataDiNascita, String password,
                       String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
}
