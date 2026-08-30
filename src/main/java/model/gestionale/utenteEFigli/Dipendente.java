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

    public String getIdentificativoDipendente() {
        return identificativoDipendente;
    }

    public void setIdentificativoDipendente(String identificativoDipendente) {
        this.identificativoDipendente = identificativoDipendente;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Dipendente dip)
        {
            return dip.identificativoDipendente.equals(identificativoDipendente);
        }
        else return false;
    }
}
