package model.gestionale.utenteEFigli;

import java.time.LocalDate;

/**
 * Il supervisore gestisce ogni aspetto del casinò: clienti, tavoli e altri dipendenti
 */
public class Supervisore extends  Dipendente
{
    /**
     * Istanzia Supervisore
     *
     * @param username                 the username
     * @param nome                     the nome
     * @param cognome                  the cognome
     * @param codiceFiscale            the codice fiscale
     * @param dataDiNascita            the data di nascita
     * @param password                 the password
     * @param identificativoDipendente the identificativo dipendente
     */
    public Supervisore(String username, String nome, String cognome, String codiceFiscale,
                       LocalDate dataDiNascita, String password,
                       String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
    @Override
    public String toString() {
        return username + " " + nome + " " + cognome+ " " + "Supervisore" ;
    }
}
