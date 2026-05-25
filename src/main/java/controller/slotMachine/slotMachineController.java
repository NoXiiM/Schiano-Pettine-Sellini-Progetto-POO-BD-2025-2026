package controller.slotMachine;

import model.giochi.NonCarte.Simboli;
import model.giochi.NonCarte.SlotMachine;

import static model.giochi.NonCarte.Simboli.*;

public class slotMachineController {
    private SlotMachine slotMachine;

    public slotMachineController(){
        slotMachine = new SlotMachine();
    }
    //Ordine elementi nella slot Diamante->Ciliegia->Cocomero->Sette->TriploDiamante


    //permette  di recuperare i path dele foto dei simbolo dell'array list
    public String getCollegamento(Simboli s){
        return slotMachine.getCollegamentoSimboli().get(s);
    }
    public  Simboli getSimboloCasuale(){
        return slotMachine.getSimboloCasuale();
    }
    public float getsaldopartita(Simboli s1, Simboli s2, Simboli s3, float saldo){

        return slotMachine.getsaldopartita(s1,s2,s3,saldo);
    }
    public String getPathSette(){
        return getCollegamento(sette);
    }

}
