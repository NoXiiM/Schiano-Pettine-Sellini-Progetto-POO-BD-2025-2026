package model.gestionale.utenteEFigli;

import java.time.LocalDate;

public abstract class Utente
    {
        //in più rispetto a uml
        protected String username;
        protected String nome;
        protected String cognome;
        protected String codiceFiscale;
        protected LocalDate dataDiNascita;
        protected String password;

        public Utente(String username, String nome, String cognome, String codiceFiscale,
                      LocalDate dataDiNascita, String password)
        {
            this.username= username;
            this.nome = nome;
            this.cognome = cognome;
            this.codiceFiscale = codiceFiscale;
            this.dataDiNascita = dataDiNascita;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCognome() {
            return cognome;
        }

        public void setCognome(String cognome) {
            this.cognome = cognome;
        }

        public String getCodiceFiscale() {
            return codiceFiscale;
        }

        public void setCodiceFiscale(String codiceFiscale) {
            this.codiceFiscale = codiceFiscale;
        }

        public LocalDate getDataDiNascita() {
            return dataDiNascita;
        }

        public void setDataDiNascita(LocalDate dataDiNascita) {
            this.dataDiNascita = dataDiNascita;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
