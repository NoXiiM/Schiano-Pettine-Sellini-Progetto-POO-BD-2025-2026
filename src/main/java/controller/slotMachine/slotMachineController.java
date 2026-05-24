package controller.slotMachine;

import model.giochi.NonCarte.SlotMachine;

public class slotMachineController {
    private SlotMachine slotMachine;

    public slotMachineController(){
        slotMachine = new SlotMachine();
    }
    //Ordine elementi nella slot Diamante->Ciliegia->Cocomero->Sette->TriploDiamante

    public String getDiamante(){//permette  di recuperare il simbolo dall'arrray list
        return slotMachine.getSimboli().get(slotMachine.getNumeroSimboli()-5);
    }
    public String getCiliegia(){
        return slotMachine.getSimboli().get(slotMachine.getNumeroSimboli()-4);
    }
    public String getCocomero(){
        return slotMachine.getSimboli().get(slotMachine.getNumeroSimboli()-3);
    }
    public String getSette(){
        return slotMachine.getSimboli().get(slotMachine.getNumeroSimboli()-2);
    }
    public String getTriploDiamante(){
        return slotMachine.getSimboli().get(slotMachine.getNumeroSimboli()-1);
    }


    public String getPathDiamante(){//permette  di recuperare i path dele foto dei simbolo dall'arrray list
        return slotMachine.getSimboliPath().get(slotMachine.getNumeroSimboli()-5);
    }
    public String getPathCiliegia(){
        return slotMachine.getSimboliPath().get(slotMachine.getNumeroSimboli()-4);
    }
    public String getPathCocomero(){
        return slotMachine.getSimboliPath().get(slotMachine.getNumeroSimboli()-3);
    }
    public String getPathSette(){
        return slotMachine.getSimboliPath().get(slotMachine.getNumeroSimboli()-2);
    }
    public String getPathTriploDiamante(){
        return slotMachine.getSimboliPath().get(slotMachine.getNumeroSimboli()-1);
    }

    public String universalPathGetter(String s){
        if(s.equals(getDiamante())) return getPathDiamante();
        if(s.equals(getCiliegia())) return getPathCiliegia();
        if(s.equals(getCocomero())) return getPathCocomero();
        if(s.equals(getSette()))    return getPathSette();
        if(s.equals(getTriploDiamante())) return getPathTriploDiamante();
        return "";
    }
    public String getSimboloCasuale(){
        return slotMachine.getSimboloCasuale();
    }
    public float getsaldopartita(String s1, String s2, String s3, float saldo){

        return slotMachine.getsaldopartita(s1,s2,s3,saldo);
    }

}
