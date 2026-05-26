package model.giochi.NonCarte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static model.giochi.NonCarte.Simboli.*;

public class SlotMachine {

    private HashMap<Simboli, String> collegamentoSimboli;
    private ArrayList<Simboli> simboli;
    private int numeroSimboli;

    public SlotMachine(){
        simboli = new ArrayList<>();
        collegamentoSimboli =  new HashMap<>();

            simboli.add(diamante);
            simboli.add(ciliegia);
            simboli.add(cocomero);
            simboli.add(sette);
            simboli.add(triplodiamante);
            simboli.add(zero);


        collegamentoSimboli.put(diamante,"/IconeSlotmachin/DiamanteIcon.png");
        collegamentoSimboli.put(ciliegia,"/IconeSlotmachin/CiliegiaIcon.png");
        collegamentoSimboli.put(cocomero,"/IconeSlotmachin/CocomeroIcon.png");
        collegamentoSimboli.put(sette,"/IconeSlotmachin/setteIcon.png");
        collegamentoSimboli.put(triplodiamante,"/IconeSlotmachin/TriploDiamanteIcon.png");
        collegamentoSimboli.put(zero, "/IconeSlotmachin/ZeroIcon.png");

        numeroSimboli = simboli.size();

    }
    public Simboli getSimboloCasuale(){
        Random random = new Random();
        return (simboli.get(random.nextInt(numeroSimboli)));
    }
    public int getsaldopartita(Simboli s1, Simboli s2, Simboli s3, int saldo){
        int creditoUscente = 0;
        if(s1==s2 && s2==s3){// la proprietà transitiva dell'uguale garantisce in questo modo cche siano tutti e 3 uguali
            if(s1==triplodiamante){// Ultimo simbolo di cui index corrisponde la grandezza, il triplodiamante
                creditoUscente = saldo * 10;
            }
            if(s1==sette){// posizione sette
                creditoUscente = saldo * 7;
            }
            if(s1==cocomero){// posizione cocomero
                creditoUscente = saldo * 5;
            }
            if(s1==ciliegia){// posizione ciliegia
                creditoUscente = saldo * 3;
            }
            if(s1==diamante){// posizione diamante
                creditoUscente = saldo * 8;
            }
        }
        // Il prossimo controllo permette di capire se uno dei 3 simboli è un diamante
        if(s1==diamante || s2==diamante || s3==diamante) {
            // il prossimo controllo è vero solo se tutti i simboli sono diversi ma almeno uno è un diamante
            if(!(s1==s2) && !(s2==s3) && !(s1==s3)) {

                //creditoUscente = saldo * 1; rimosso dato che la probabilità di vincita diverrebbe del 40%
            }
            else {// qui ci si finesce se solo 2 simboli sono diamante
                creditoUscente = saldo * 2;
            }
        }
        return creditoUscente;
    }


    public HashMap<Simboli,String> getCollegamentoSimboli() {
        return collegamentoSimboli;
    }

}
