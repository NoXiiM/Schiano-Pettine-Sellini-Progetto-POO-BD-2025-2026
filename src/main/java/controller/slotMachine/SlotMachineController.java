package controller.slotMachine;

import model.giochi.NonCarte.Simboli;
import model.giochi.NonCarte.SlotMachine;

import static model.giochi.NonCarte.Simboli.*;

/**
 * Controller per gestire la logica della Slot machine.
 */
public class SlotMachineController {
    private final SlotMachine slotMachine;

    /**
     * Instantiates a new Slot machine controller che a sua volta instanzia una slotmachine.
     */
    public SlotMachineController(){
        slotMachine = new SlotMachine();
    }
    //Ordine elementi nella slot Diamante->Ciliegia->Cocomero->Sette->TriploDiamante


    /**
     * Get collegamento string.
     * Permette di recuperare il path della foto del simbolo s
     *
     * @param s simbolo
     * @return path
     */
    public String getCollegamento(Simboli s){
        return slotMachine.getCollegamentoSimboli().get(s);
    }

    /**
     * Get simbolo casuale simboli.
     * Funzione di collegamento tra model e gui
     * @return the simboli
     */
    public  Simboli getSimboloCasuale(){
        return slotMachine.getSimboloCasuale();
    }

    /**
     * Getsaldopartita int.
     * Funzione di collegamento tra gui e model
     * @param s1    the s 1
     * @param s2    the s 2
     * @param s3    the s 3
     * @param saldo the saldo
     * @return the int
     */
    public int getsaldopartita(Simboli s1, Simboli s2, Simboli s3, int saldo){
        return slotMachine.getsaldopartita(s1,s2,s3,saldo);
    }

    /**
     * Get path sette string.
     * Restituisce direttamente il path del sette, viene usato al primo avvio per avere sette come simbolo displayato
     * @return the string
     */
    public String getPathSette(){
        return getCollegamento(sette);
    }
}
