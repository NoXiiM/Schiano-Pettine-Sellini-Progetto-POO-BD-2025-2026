package model.gestionale.utenteEFigli;

import java.time.LocalDate;

/**
 * Utente è padre di Cliente e Supervisore
 */
public abstract class Utente
    {
        /**
         * The Username.
         */
//in più rispetto a uml
        protected String username;
        /**
         * The Nome.
         */
        protected String nome;
        /**
         * The Cognome.
         */
        protected String cognome;
        /**
         * The Codice fiscale.
         */
        protected String codiceFiscale;
        /**
         * The Data di nascita.
         */
        protected LocalDate dataDiNascita;
        /**
         * The Password.
         */
        protected String password;

        /**
         * Istanzia Utente
         *
         * @param username      the username
         * @param nome          the nome
         * @param cognome       the cognome
         * @param codiceFiscale the codice fiscale
         * @param dataDiNascita the data di nascita
         * @param password      the password
         */
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

        /**
         * Gets username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Sets username.
         *
         * @param username the username
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Gets nome.
         *
         * @return the nome
         */
        public String getNome() {
            return nome;
        }

        /**
         * Sets nome.
         *
         * @param nome the nome
         */
        public void setNome(String nome) {
            this.nome = nome;
        }

        /**
         * Gets cognome.
         *
         * @return the cognome
         */
        public String getCognome() {
            return cognome;
        }

        /**
         * Sets cognome.
         *
         * @param cognome the cognome
         */
        public void setCognome(String cognome) {
            this.cognome = cognome;
        }

        /**
         * Gets codice fiscale.
         *
         * @return the codice fiscale
         */
        public String getCodiceFiscale() {
            return codiceFiscale;
        }

        /**
         * Sets codice fiscale.
         *
         * @param codiceFiscale the codice fiscale
         */
        public void setCodiceFiscale(String codiceFiscale) {
            this.codiceFiscale = codiceFiscale;
        }

        /**
         * Gets data di nascita.
         *
         * @return the data di nascita
         */
        public LocalDate getDataDiNascita() {
            return dataDiNascita;
        }

        /**
         * Sets data di nascita.
         *
         * @param dataDiNascita the data di nascita
         */
        public void setDataDiNascita(LocalDate dataDiNascita) {
            this.dataDiNascita = dataDiNascita;
        }

        /**
         * Gets password.
         *
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Sets password.
         *
         * @param password the password
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }
