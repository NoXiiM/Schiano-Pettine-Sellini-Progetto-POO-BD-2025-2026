package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

/*
    da fare:
        metodi:
        convertiInFiches
*/

public abstract class Cliente extends Utente
{
    protected int saldo;
    protected String codiceTesseraGiocatore;
    protected Time tempoDiGioco;
    protected int fichesGiocate;
    protected double vincitaPercentualeTot;

    //attributi da associazioni
    private Ban ban;

    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password);
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
        tempoDiGioco = new Time(0, 0, 0);
        fichesGiocate = 0;
        saldo= 0;
        vincitaPercentualeTot = 0;
        ban = null;
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

    public void setCodiceTesseraGiocatore(String codiceTesseraGiocatore) {
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
    }

    public Time getTempoDiGioco() {
        return tempoDiGioco;
    }

    public void setTempoDiGioco(Time tempoDiGioco) {
        this.tempoDiGioco = tempoDiGioco;
    }

    public int getFichesGiocate() {
        return fichesGiocate;
    }

    public void setFichesGiocate(int fichesGiocate) {
        this.fichesGiocate = fichesGiocate;
    }

    public double getVincitaPercentualeTot() {
        return vincitaPercentualeTot;
    }

    public void setVincitaPercentualeTot(double vincitaPercentualeTot) {
        this.vincitaPercentualeTot = vincitaPercentualeTot;
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

    public void setBan(Ban ban) {
        this.ban = ban;
    }
}
