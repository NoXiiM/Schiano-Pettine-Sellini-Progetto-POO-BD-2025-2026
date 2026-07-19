package model.gestionale.utenteEFigli;

import java.time.LocalDate;

public abstract class Dipendente extends Utente
{
    protected String identificativoDipendente;

    public Dipendente(String username, String nome, String cognome, String codiceFiscale,
                      LocalDate dataDiNascita, String password,
                      String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password);
        this.identificativoDipendente = identificativoDipendente;
    }
}
