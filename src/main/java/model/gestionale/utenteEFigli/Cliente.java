package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

/**
 * Il cliente può giocare e gestire il suo account
 */
public class Cliente extends Utente
{
    private boolean premium;
    private double sconto_premium;
    /**
     * Saldo.
     */
    protected int saldo;
    /**
     * Id
     */
    protected String codiceTesseraGiocatore;
    /**
     * Tempo di gioco.
     */
    protected Duration tempoDiGioco;
    /**
     * Registra tutte le fiches che il giocatore abbia mai puntato in tutta la sua storia al casinò
     */
    protected int fichesGiocate;
    /**
     * Vincita percentuale totale calcolata su ogni gioco che abbia mai giocato
     */
    protected double vincitaPercentualeTot;
    /**
     * Partite giocate
     */
//in più rispetto a uml
    protected int partiteGiocate;
    private boolean sospetto;

    //attributi da associazioni
    private Ban ban;

    /**
     * Istanza nuovo Cliente, alla fine non abbiamo usato da nessuna parte questo costruttore
     *
     * @deprecated
     *
     * @param username               username
     * @param nome                   nome
     * @param cognome                cognome
     * @param codiceFiscale          codice fiscale
     * @param dataDiNascita          data di nascita
     * @param password               password
     * @param codiceTesseraGiocatore codice tessera giocatore
     */
    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password);

        this.sconto_premium= 0;
        this.sospetto= false;
        this.premium= false;
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
        tempoDiGioco = Duration.ZERO;
        fichesGiocate = 0;
        saldo= 0;
        ban = null;
        partiteGiocate = 0;
    }

    /**
     * Istanza nuovo Cliente
     *
     * @param username               the username
     * @param nome                   the nome
     * @param cognome                the cognome
     * @param codiceFiscale          the codice fiscale
     * @param dataDiNascita          the data di nascita
     * @param password               the password
     * @param codiceTesseraGiocatore the codice tessera giocatore
     * @param premium                the premium
     * @param sconto_premium         the sconto premium
     * @param sospetto               the sospetto
     * @param tempoDiGiocoInSec      the tempo di gioco in sec
     * @param fichesGiocate          the fiches giocate
     * @param saldo                  the saldo
     * @param partiteGiocate         the partite giocate
     * @param vincitaPercentualeTot  the vincita percentuale tot
     * @param dataBan                se la data di ban non è null, il giocatore è stato bannato, quindi si istanzia un ban
     *                               con la data e il motivo rispettivo
     * @param motiviBan              the motivi ban
     */
    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore, boolean premium,
                   double sconto_premium, boolean sospetto, long tempoDiGiocoInSec, int fichesGiocate, int saldo,
                   int partiteGiocate, double vincitaPercentualeTot, LocalDate dataBan, String motiviBan){

        this(username, nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        setPremium(premium, sconto_premium);
        this.sospetto = sospetto;
        this.tempoDiGioco = Duration.ofSeconds(tempoDiGiocoInSec);
        this.fichesGiocate = fichesGiocate;
        setSaldo(saldo);
        this.partiteGiocate = partiteGiocate;
        this.vincitaPercentualeTot = vincitaPercentualeTot;
        if(dataBan != null)
        {
            ban = new Ban(dataBan, motiviBan);
        }
    }

    /**
     * Gets codice tessera giocatore.
     *
     * @return the codice tessera giocatore
     */
    public String getCodiceTesseraGiocatore() {
        return codiceTesseraGiocatore;
    }

    /**
     * Gets tempo di gioco.
     *
     * @return the tempo di gioco
     */
    public Duration getTempoDiGioco() {
        return tempoDiGioco;
    }

    /**
     * Gets fiches giocate.
     *
     * @return the fiches giocate
     */
    public int getFichesGiocate() {
        return fichesGiocate;
    }

    /**
     * Incrementa fiches giocate.
     *
     * @param fichesGiocate the fiches giocate
     */
    public void incrementaFichesGiocate(int fichesGiocate) {
        this.fichesGiocate += fichesGiocate;
    }

    /**
     * Gets vincita percentuale tot.
     *
     * @return the vincita percentuale tot
     */
    public double getVincitaPercentualeTot() {
        return vincitaPercentualeTot;
    }

    /**
     * Gets ban.
     *
     * @return the ban
     */
    public Ban getBan() {
        return ban;
    }

    /**
     * Gets saldo.
     *
     * @return the saldo
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * Sets saldo.
     *
     * @param saldo the saldo
     */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    /**
     * Funzione per depositare soldi nel proprio account.
     *
     * @param deposito deposito
     * @throws RuntimeException errore lanciato se deposito negativo, è stato nascosto un pizzico di gioco d'azzardo anche
     * nel messaggio d'errore
     */
    public void deposita(int deposito) throws RuntimeException
    {
        Random random = new Random();
        if(deposito < 0) throw new RuntimeException((random.nextInt(4) == 3) ? "sei serio?" : "non puoi depositare in negativo");
        this.saldo += deposito;
    }

    /**
     * Funzione per prelevare soldi nel proprio account.
     *
     * @param prelievo prelievo
     * @return true: valore di prelievo valido, false: valore noon valido
     * @throws RuntimeException errore lanciato se prelievo negativo
     */
    public boolean preleva(int prelievo) throws RuntimeException
    {
        if(prelievo < 0) throw new RuntimeException("non puoi prelevare in negativo");
        if(prelievo <= saldo){
            saldo -= prelievo;
            return true;
        } else{
            return false;
        }
    }


    /**
     * Funzione che aggiorna percentuale vittoria del cliente e nel caso soddisfi le condizioni lo setta come sospetto
     *
     * @param vittoriaPercentualeSessione vittoria percentuale della sessione terminata
     * @param partiteGiocate              partite giocate nella sessione terminata
     */
    public void aggiornaPercentualeVittoria(double vittoriaPercentualeSessione, int partiteGiocate)
    {
        if(this.partiteGiocate == 0)
        {
            vincitaPercentualeTot = vittoriaPercentualeSessione;
            this.partiteGiocate = partiteGiocate;
        }
        else
        {
            vincitaPercentualeTot = (vincitaPercentualeTot*this.partiteGiocate);
            this.partiteGiocate += partiteGiocate;
            vincitaPercentualeTot = (vincitaPercentualeTot + vittoriaPercentualeSessione*partiteGiocate)/this.partiteGiocate;
        }

        if(this.partiteGiocate > 50 && vincitaPercentualeTot > 50)
        {
            sospetto = true;
        }
    }

    /**
     * Funzione che aggiorna tempo di gioco.
     *
     * @param tempoDaAggiungere tempo da aggiungere
     */
    public void aggiornaTempoDiGioco(Duration tempoDaAggiungere)
    {
        tempoDiGioco = tempoDiGioco.plus(tempoDaAggiungere);
    }

    @Override
    public String toString() {
        return username + " " + nome + " " + cognome + " " + "sospetto: " + sospetto;
    }

    /**
     * Crea ban relativo al giocatore
     *
     * @param motivo motivo
     */
    public void creaBan(String motivo){
        this.ban = new Ban(motivo);
    }

    /**
     * Get motivo ban string.
     *
     * @return the string
     */
    public String getMotivoBan(){
        if (ban == null) return null;
        return ban.getMotivi();
    }

    /**
     * Gets data ban.
     *
     * @return the data ban
     */
    public LocalDate getDataBan()
    {
        if(ban == null) return null;
        return ban.getDataDiBan();
    }

    /**
     * Se il cliente ha più di 48 ore di gioco e più di 10000 fiches giocate allora diventa premium
     *
     * @return true: il cliente può essere passato a Premium; false: non può
     */
    public boolean convertiPremium()
    {
        Duration quarantottoh = Duration.ofHours(48);

        return !premium && fichesGiocate >= 10000 && tempoDiGioco.compareTo(quarantottoh) >= 0;
    }

    /**
     * Is premium boolean.
     *
     * @return the boolean
     */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Sets premium.
     *
     * @param premium the premium
     */
    public void setPremium(boolean premium) {
        this.premium = premium;
        this.sconto_premium= 0.5;
    }

    /**
     * Sets premium.
     *
     * @param premium        the premium
     * @param sconto_premium the sconto premium
     */
    public void setPremium(boolean premium, double sconto_premium) {
        this.premium = premium;
        this.sconto_premium= sconto_premium;
    }

    /**
     * Gets sconto premium.
     *
     * @return the sconto premium
     */
    public double getSconto_premium() {
        return sconto_premium;
    }

    /**
     * Gets partite giocate.
     *
     * @return the partite giocate
     */
    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    /**
     * Is sospetto boolean.
     *
     * @return the boolean
     */
    public boolean isSospetto() {
        return sospetto;
    }

    /**
     * Sets codice tessera giocatore.
     *
     * @param codiceTesseraGiocatore the codice tessera giocatore
     */
    public void setCodiceTesseraGiocatore(String codiceTesseraGiocatore) {
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
    }

    /**
     * Funzione per il decremento del saldo del cliente, inteso come puntata nei giochi
     *
     * @param value valore decremento
     * @throws RuntimeException lancia errore se il saldo è insufficiente
     */
    public void decrementaSaldoCliente(int value) throws RuntimeException
    {
        if(value > saldo) throw new RuntimeException("saldo insufficiente");
        else
        {
            saldo -= value;
        }
    }
}
