package model.gestionale.utenteEFigli;

import java.util.Date;

public abstract class Utente
    {
        protected String nome;
        protected String cognome;
        protected String codiceFiscale;
        protected Date dataDiNascita;
        protected String password;

        public Utente(String nome, String cognome, String codiceFiscale,
                      Date dataDiNascita, String password)
        {
            this.nome = nome;
            this.cognome = cognome;
            this.codiceFiscale = codiceFiscale;
            this.dataDiNascita = dataDiNascita;
            this.password = password;
        }
    }
