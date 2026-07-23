package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Cliente extends Utente
{
    private boolean premium;
    private double sconto_premium;
    protected int saldo;
    protected String codiceTesseraGiocatore;
    protected Time tempoDiGioco;
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
        tempoDiGioco = new Time(0, 0, 0);
        fichesGiocate = 0;
        saldo= 0;
        ban = null;
        partiteGiocate = 0;
    }

    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore, boolean premium,
                   double sconto_premium, boolean sospetto, LocalTime tempoDiGioco, int fichesGiocate, int saldo,
                   int partiteGiocate, LocalDate dataBan, String motiviBan){

        this(username, nome, cognome, codiceFiscale, dataDiNascita, password, codiceTesseraGiocatore);
        setPremium(premium, sconto_premium);
        this.sospetto = sospetto;
        //TODO soluzione temporanea, potrebbe servire portare tutto a localTime
        this.tempoDiGioco = new Time(tempoDiGioco.getSecond()*1000);
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

    public Time getTempoDiGioco() {
        return tempoDiGioco;
    }

    public void setTempoDiGioco(Time tempoDiGioco) {
        this.tempoDiGioco = tempoDiGioco;
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

    public void aggiornaTempoDiGioco(Time tempo)
    {
        tempoDiGioco.setHours(tempoDiGioco.getHours() + tempo.getHours());
        tempoDiGioco.setMinutes(tempoDiGioco.getMinutes() + tempo.getMinutes());
        tempoDiGioco.setSeconds(tempoDiGioco.getSeconds() + tempo.getSeconds());
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
        int quarantottoh = 172800000;
        return fichesGiocate >= 10000 && tempoDiGioco.getTime() >= quarantottoh;
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
