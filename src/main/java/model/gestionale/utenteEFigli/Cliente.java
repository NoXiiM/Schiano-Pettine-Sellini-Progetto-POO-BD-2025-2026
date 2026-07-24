package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Cliente extends Utente
{
    private boolean premium;
    private double sconto_premium;
    protected int saldo;
    protected String codiceTesseraGiocatore;
    protected Duration tempoDiGioco;
    protected int fichesGiocate;
    protected double vincitaPercentualeTot;
    //in più rispetto a uml
    protected int partiteGiocate;
    private boolean sospetto;

    //attributi da associazioni
    private Ban ban;

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

    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore, boolean premium,
                   double sconto_premium, boolean sospetto, long tempoDiGiocoInSec, int fichesGiocate, int saldo,
                   int partiteGiocate, LocalDate dataBan, String motiviBan){

        this(username, nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        setPremium(premium, sconto_premium);
        this.sospetto = sospetto;
        this.tempoDiGioco = Duration.ofSeconds(tempoDiGiocoInSec);
        this.fichesGiocate = fichesGiocate;
        setSaldo(saldo);
        this.partiteGiocate = partiteGiocate;
        ban = new Ban(dataBan, motiviBan);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodiceTesseraGiocatore() {
        return codiceTesseraGiocatore;
    }

    public Duration getTempoDiGioco() {
        return tempoDiGioco;
    }

    public int getFichesGiocate() {
        return fichesGiocate;
    }

    public void incrementaFichesGiocate(int fichesGiocate) {
        this.fichesGiocate += fichesGiocate;
    }

    public double getVincitaPercentualeTot() {
        return vincitaPercentualeTot;
    }

    public Ban getBan() {
        return ban;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void deposita(int deposito){
        this.saldo += deposito;
    }

    public boolean preleva(int prelievo){
        if(prelievo <= saldo){
            saldo -= prelievo;
            return true;
        } else{
            return false;
        }
    }


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

        if(partiteGiocate > 50 && vincitaPercentualeTot > 50)
        {
            sospetto = true;
        }
    }

    public void aggiornaTempoDiGioco(Duration tempo)
    {
        tempoDiGioco = tempoDiGioco.plus(tempo);
    }

    @Override
    public String toString() {
        return username + " " + nome + " " + cognome + " " + "sospetto: " + sospetto;
    }

    public void creaBan(String motivo){
        this.ban = new Ban(motivo);
    }

    public String getMotivoBan(){
        if (ban == null) return null;
        return ban.getMotivi();
    }

    public boolean convertiPremium()
    {
        Duration quarantottoh = Duration.ofHours(48);

        return fichesGiocate >= 10000 && tempoDiGioco.compareTo(quarantottoh) >= 0;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
        this.sconto_premium= 0.5;
    }

    public void setPremium(boolean premium, double sconto_premium) {
        this.premium = premium;
        this.sconto_premium= sconto_premium;
    }

    public double getSconto_premium() {
        return sconto_premium;
    }

}
