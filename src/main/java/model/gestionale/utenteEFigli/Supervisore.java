package model.gestionale.utenteEFigli;

import java.time.LocalDate;

public class Supervisore extends  Dipendente
{
    public Supervisore(String username, String nome, String cognome, String codiceFiscale,
                       LocalDate dataDiNascita, String password,
                       String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
}
