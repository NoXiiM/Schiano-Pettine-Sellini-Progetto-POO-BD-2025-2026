package model.giochi.NonCarte;

import java.util.ArrayList;

public class SlotMachine {
    private ArrayList<String> simboli;
    private ArrayList<String> simboliPath;
    private int numeroSimboli;

    public SlotMachine(){
        simboli = new ArrayList<>();
        simboliPath = new ArrayList<>();

        simboli.add("Diamante");
        simboli.add("Ciliegia");
        simboli.add("Cocomero");
        simboli.add("Sette");
        simboli.add("TriploDiamante");

        simboliPath.add("/IconeSlotmachin/DiamanteIcon.png");
        simboliPath.add("/IconeSlotmachin/CiliegiaIcon.png");
        simboliPath.add("/IconeSlotmachin/CocomeroIcon.png");
        simboliPath.add("/IconeSlotmachin/setteIcon.png");
        simboliPath.add("/IconeSlotmachin/TriploDiamanteIcon.png");
        numeroSimboli = simboli.size();

    }
    public String getSimboloCasuale(){
        return (simboli.get((int)(Math.random()%(numeroSimboli))));
    }
    public float getsaldopartita(String s1, String s2, String s3, float saldo){
        float creditoUscente = 0;
        if(s1.equals(s2) && s2.equals(s3)){// la proprietà transitiva dell'uguale garantisce in questo modo cche siano tutti e 3 uguali
            if(s1.equals(simboli.get(numeroSimboli-1))){// Ultimo simbolo di cui index corrisponde la grandezza, il triplodiamante
                creditoUscente = saldo * 10;
            }
            if(s1.equals(simboli.get(numeroSimboli-2))){// posizione sette
                creditoUscente = saldo * 7;
            }
            if(s1.equals(simboli.get(numeroSimboli-3))){// posizione cocomero
                creditoUscente = saldo * 5;
            }
            if(s1.equals(simboli.get(numeroSimboli-4))){// posizione ciliegia
                creditoUscente = saldo * 3;
            }
            if(s1.equals(simboli.get(numeroSimboli-5))){// posizione diamante
                creditoUscente = saldo * 8;
            }
        }
        // Il prossimo controllo permette di capire se uno dei 3 simboli è un diamante
        if(s1.equals(simboli.get(numeroSimboli-5)) || s2.equals(simboli.get(numeroSimboli-5)) || s3.equals(simboli.get(numeroSimboli-5))) {
            // il prossimo controllo è vero solo se tutti i simboli sono diversi ma almeno uno è un diamante
            if(!s1.equalsIgnoreCase(s2) && !s2.equalsIgnoreCase(s3) && !s1.equalsIgnoreCase(s3)) {

                creditoUscente = saldo * 2;
            }
            else {// qui ci si finesce se solo 2 simboli sono diamante
                creditoUscente = saldo * 4;
            }
        }
        return creditoUscente;
    }

    public ArrayList<String> getSimboli() {
        return simboli;
    }

    public ArrayList<String> getSimboliPath() {
        return simboliPath;
    }

    public int getNumeroSimboli() {
        return numeroSimboli;
    }
}
