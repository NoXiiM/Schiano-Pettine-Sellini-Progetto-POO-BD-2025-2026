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
        if(s1==s2 && s2==s3){// la proprietà transitiva dell'uguale garantisce in questo modo cche siano tutti e 3 uguali
            if(s1==triplodiamante){// Ultimo simbolo di cui index corrisponde la grandezza, il triplodiamante
                return saldo * 10;
            }
            if(s1==sette){// posizione sette
                return saldo * 7;
            }
            if(s1==cocomero){// posizione cocomero
                return saldo * 5;
            }
            if(s1==ciliegia){// posizione ciliegia
                return saldo * 3;
            }
            if(s1==diamante){// posizione diamante
                return saldo * 8;
            }
        }
        // Il prossimo controllo permette di capire se uno dei 3 simboli è un diamante
        if(s1==diamante || s2==diamante || s3==diamante) {
            int contatoreDiamanti = 0;

            if(s1 == diamante) contatoreDiamanti++;
            if(s2 == diamante) contatoreDiamanti++;
            if(s3 == diamante) contatoreDiamanti++;

            if(contatoreDiamanti == 2) {
                return saldo * 2;
            }
        }
        return 0;
    }


    public HashMap<Simboli,String> getCollegamentoSimboli() {
        return collegamentoSimboli;
    }

}
