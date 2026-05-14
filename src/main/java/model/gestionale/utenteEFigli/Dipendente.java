package model.gestionale.utenteEFigli;

import java.util.Date;

public abstract class Dipendente extends Utente
{
    protected String identificativoDipendente;

    public Dipendente(String nome, String cognome, String codiceFiscale,
                      Date dataDiNascita, String password,
                      String identificativoDipendente)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password);
        this.identificativoDipendente = identificativoDipendente;
    }
}
