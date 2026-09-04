package model.gestionale.utenteEFigli;

import java.time.LocalDate;

/**
 * Dipendente è padre di Dealer e Supervisore
 */
public abstract class Dipendente extends Utente
{
    /**
     * Id.
     */
    protected String identificativoDipendente;

    /**
     * Istanzia Dipendente
     *
     * @param username                 the username
     * @param nome                     the nome
     * @param cognome                  the cognome
     * @param codiceFiscale            the codice fiscale
     * @param dataDiNascita            the data di nascita
     * @param password                 the password
     * @param identificativoDipendente the identificativo dipendente
     */
    public Dipendente(String username, String nome, String cognome, String codiceFiscale,
                      LocalDate dataDiNascita, String password,
                      String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password);
        this.identificativoDipendente = identificativoDipendente;
    }

    /**
     * Gets identificativo dipendente.
     *
     * @return the identificativo dipendente
     */
    public String getIdentificativoDipendente() {
        return identificativoDipendente;
    }

    /**
     * Sets identificativo dipendente.
     *
     * @param identificativoDipendente the identificativo dipendente
     */
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
