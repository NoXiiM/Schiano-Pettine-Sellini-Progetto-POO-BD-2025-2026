package controller.slotMachine;

import controller.ClienteCorrente;
import model.giochi.NonCarte.Simboli;
import model.giochi.NonCarte.SlotMachine;

import static model.giochi.NonCarte.Simboli.*;

public class slotMachineController {
    private SlotMachine slotMachine;
    private ClienteCorrente clienteCorrente;

    public slotMachineController(ClienteCorrente s){
        slotMachine = new SlotMachine();
        clienteCorrente = s;
    }
    //Ordine elementi nella slot Diamante->Ciliegia->Cocomero->Sette->TriploDiamante


    //permette  di recuperare i path dele foto dei simbolo dell'array list
    public String getCollegamento(Simboli s){
        return slotMachine.getCollegamentoSimboli().get(s);
    }
    public  Simboli getSimboloCasuale(){
        return slotMachine.getSimboloCasuale();
    }
    public int getsaldopartita(Simboli s1, Simboli s2, Simboli s3, int saldo){
        return slotMachine.getsaldopartita(s1,s2,s3,saldo);
    }
    public String getPathSette(){
        return getCollegamento(sette);
    }
    public int getsaldoGiocatore(){return clienteCorrente.getSaldoGiocatore();}
    public void decrementa(int creditoInserito) throws RuntimeException{clienteCorrente.decrementaSaldoGiocatore(creditoInserito);}
    public void incrementa(int creditoInserito){clienteCorrente.incrementaSaldoGiocatore(creditoInserito);}
    public void aggiornaCliente(){clienteCorrente.aggiornaDatiCliente(); clienteCorrente.terminaSessione();}
    public void aggiornaVincitaPercentuale(boolean v){clienteCorrente.aggiornaVincitaPercentuale(v);}
    public void startTimer(){clienteCorrente.startTimer();}
    public void stopTimer(){clienteCorrente.stopTimer();}
    public String getCheckPercentualeVittoria(){return " "+clienteCorrente.getTime()+" "+clienteCorrente.stringaPercentuale()+"";}
    public ClienteCorrente getClienteCorrente() {
        return clienteCorrente;
    }
}
