package model.gestionale.utenteEFigli;

import model.gestionale.Ban;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

public class Cliente extends Utente
{
    protected int saldo;
    protected String codiceTesseraGiocatore;
    protected Time tempoDiGioco;
    protected int fichesGiocate;
    protected double vincitaPercentualeTot;
    //in più rispetto a uml
    protected int partiteGiocate;
    //TODO: creare cliente base?
    private boolean sospetto;

    //attributi da associazioni
    private Ban ban;

    public Cliente(String username, String nome, String cognome, String codiceFiscale,
                   LocalDate dataDiNascita, String password, String codiceTesseraGiocatore)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password);

        this.sospetto= false;
        this.codiceTesseraGiocatore = codiceTesseraGiocatore;
        tempoDiGioco = new Time(0, 0, 0);
        fichesGiocate = 0;
        saldo= 0;
        ban = null;
        partiteGiocate = 0;
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

    public void setBan(Ban ban) {
        this.ban = ban;
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
}
