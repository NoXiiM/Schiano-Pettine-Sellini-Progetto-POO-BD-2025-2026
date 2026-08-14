package controller.poker;

import controller.blackjack.DeckOut;
import controller.gestionale.ClientWelcomeController;
import controller.mazzo.ControllerMazzo;
import database.implementazioneDAO.ImpDAOop;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerPoker extends ControllerMazzo
{
    private int puntataAttuale;
    private int pot;
    private int ante;
    private boolean almenoUnGiro;

    public ControllerPoker(int nmani)
    {
        super(1, nmani, Gioco.Poker);

        mazzo.inizializzaSabotPoker(nmani);
        mazzo.mischiaMazzo();

        puntataAttuale = 0;
        pot = 0;
        ante = 0;
    }

    @Override
    public void reinizializzaMazzo() {
        mazzo = new Sabot(mazzo.getNumeroDiMazzi(), mazzo.getGioco());
        mazzo.inizializzaSabotPoker(listaMani.size());
        mazzo.mischiaMazzo();
    }

    public void serviCarte() throws DeckOut
    {
        for(Mano i : listaMani)
        {
            for(int j = 0; j < 5; j++) serviCarta(i);
        }
    }

    public void resettaMani()
    {
        int nmani = listaMani.size();
        listaMani = new ArrayList<>();

        for(int i = 0; i < nmani; i++)
        {
            this.addMano(creaMano(Gioco.Poker));
        }
    }

    public Cliente caricaPlayer(String username, String password, ArrayList<ClientWelcomeController> loggedYet)
            throws RuntimeException, SQLException
    {
        ArrayList<String> usrLogged = new ArrayList<>();

        for(ClientWelcomeController i : loggedYet)
        {
            usrLogged.add(i.getClienteUsername());
        }

        if(usrLogged.contains(username)) throw new RuntimeException("questo utente è già loggato");

        ImpDAOop db = new ImpDAOop();

        String[] codiceTessera = new String[1];
        int[] saldo = new int[1];
        long[] tempoDiGioco = new long[1];
        int[] fichesGiocate = new int[1];
        double[] vincitaPercentualeTot = new double[1];
        int[] partiteGiocate = new int[1];
        String[] tipo = new String[1];
        double[] scontoPokerPercentuale = new double[1];
        boolean[] sospetto = new boolean[1];
        LocalDate[] dataDiBan = new LocalDate[1];
        String[] motiviBan = new String[1];
        String[] nome = new String[1];
        String[] cognome = new String[1];
        String[] codiceFiscale = new String[1];
        LocalDate[] dataDiNascita = new LocalDate[1];

        if(db.loginCliente(codiceTessera, saldo, tempoDiGioco, fichesGiocate, vincitaPercentualeTot,
                partiteGiocate, tipo, scontoPokerPercentuale, sospetto, dataDiBan, motiviBan, nome,
                cognome, codiceFiscale, dataDiNascita, username, password))
        {
            boolean flag = tipo[0].equals("Premium");

            return new Cliente(username, nome[0], cognome[0], codiceFiscale[0], dataDiNascita[0], password,
                    codiceTessera[0], flag, scontoPokerPercentuale[0], sospetto[0], tempoDiGioco[0], fichesGiocate[0],
                    saldo[0], partiteGiocate[0], dataDiBan[0], motiviBan[0]);
        }
        return null;
    }

    public void rimischiataMano(int index)
    {
        ManoPoker mano = (ManoPoker) listaMani.get(index);

        mano.rimuoviCarte();

        while(mano.getListaMano().size() < 5)
        {
            serviCarta(mano);
        }
    }

    public int getPuntataAttuale() {
        return puntataAttuale;
    }

    public void setPuntataAttuale(int puntataAttuale) {
        this.puntataAttuale = puntataAttuale;
    }

    //se rimane un solo giocatore ne restituisce la posizione
    public Integer setFolded(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        int contatore = 0;
        Integer indiceVincitore = null;

        temp.setFolded(true);

        for(int i = 0; i < listaMani.size(); i++)
        {
            ManoPoker j = (ManoPoker) listaMani.get(i);
            if(!j.isFolded())
            {
                contatore++;
                indiceVincitore = i;
            }
        }

        if(contatore == 1) return indiceVincitore;
        else return null;
    }

    public boolean controlloStessePuntate()
    {
        ArrayList<Integer> listaPuntate = new ArrayList<>();

        for(Mano i : listaMani)
        {
            ManoPoker temp = (ManoPoker) i;

            if(!temp.isFolded() && !temp.isAllIn()) listaPuntate.add(temp.getPuntata());
        }

        int val = listaPuntate.getFirst();

        for(Integer i : listaPuntate)
        {
            if(val != i) return false;
        }

        return true;
    }

    public void resetPuntateMani()
    {
        for(Mano i : listaMani)
        {
            i.setPuntata(0);
        }
    }

    public boolean getFolded(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);

        return temp.isFolded();
    }

    public static int getValoreNumero(Carta carta) {
        int val = ControllerMazzo.getValoreNumero(carta);

        if(val == 1) val = 14;

        return val;
    }

    //indici in listaMani dei vincitori
    public ArrayList<Integer> calcolaCombo(ArrayList<Integer> listaEsclusi)
    {
        ArrayList<ManoPoker> maniAttive = new ArrayList<>();

        for(int i = 0; i < listaMani.size(); i++)
        {
            ManoPoker j = (ManoPoker) getMano(i);

            if(!j.isFolded() && (listaEsclusi == null || !listaEsclusi.contains(i)))
            {
                ArrayList<Integer> numbers = new ArrayList<>();
                ArrayList<Seme> seeds = new ArrayList<>();

                for(Carta z : j.getListaMano())
                {
                    numbers.add(getValoreNumero(z));
                    seeds.add(z.getSeme());
                }

                j.setValoreCombo(valoreCombo(numbers, seeds));

                maniAttive.add(j);
            }
        }

        ArrayList<ManoPoker> vincitori = new ArrayList<>();

        //ordine lessicografico: a > b <-> valore combo di a < valore combo di b OR (valore combo di a = valore combo
        // di b AND tie-break di a > tie-break di b)
        for(ManoPoker i : maniAttive)
        {
            if(vincitori.isEmpty()) vincitori.add(i);
            else
            {
                if(vincitori.getFirst().getValoreCombo()[0] > i.getValoreCombo()[0])
                {
                    vincitori.clear();
                    vincitori.add(i);
                }
                else if(vincitori.getFirst().getValoreCombo()[0] == i.getValoreCombo()[0])
                {
                    if(vincitori.getFirst().getValoreCombo()[1] < i.getValoreCombo()[1])
                    {
                        vincitori.clear();
                        vincitori.add(i);
                    }
                    else if(vincitori.getFirst().getValoreCombo()[1] == i.getValoreCombo()[1]) vincitori.add(i);
                }
            }
        }

        ArrayList<Integer> indiciVincitori = new ArrayList<>();

        for(ManoPoker i : vincitori)
        {
            indiciVincitori.add(listaMani.indexOf(i));
        }

        return indiciVincitori;
    }

    //ritorna array di 2 valori interi: 1) valore della combo (vedi ComboPoker), 2) tie-break
    private int[] valoreCombo(ArrayList<Integer> numbers, ArrayList<Seme> seeds)
    {
        numbers.sort(null);

        int[] punteggio = new int[2];

        Integer flagScala = scala(numbers);
        boolean flagColore = colore(seeds);

        //1) scala reale
        if(flagScala != null && flagScala == 14 && flagColore)
        {
            punteggio[0] = ComboPoker.scalaReale;
            return punteggio;
        }
        //2) scala colore
        if(flagScala != null && flagColore)
        {
            punteggio[0] = ComboPoker.scalaColore;
            punteggio[1] = flagScala;
            return punteggio;
        }

        Integer flagPoker = poker(numbers);

        //3) poker
        if(flagPoker != null)
        {
            punteggio[0] = ComboPoker.poker;
            punteggio[1] = flagPoker;
            return punteggio;
        }
        //4) colore
        if(flagColore)
        {
            punteggio[0] = ComboPoker.colore;
            punteggio[1] = numbers.getLast();
            return punteggio;
        }

        Integer flagTris = tris(numbers);
        Integer[] flagCoppie = coppie(numbers);

        //5) full
        if(flagTris != null && flagCoppie != null)
        {
            punteggio[0] = ComboPoker.full;
            punteggio[1] = flagTris;
            return punteggio;
        }

        //6) scala
        if(flagScala != null)
        {
            punteggio[0] = ComboPoker.scala;
            punteggio[1] = flagScala;
            return punteggio;
        }

        //7) tris
        if(flagTris != null)
        {
            punteggio[0] = ComboPoker.tris;
            punteggio[1] = flagTris;
            return punteggio;
        }

        //8) doppia coppia
        if(flagCoppie != null && flagCoppie[0] == 2)
        {
            punteggio[0] = ComboPoker.doppiaCoppia;
            punteggio[1] = flagCoppie[1];
            return punteggio;
        }

        //9) coppia
        if(flagCoppie != null && flagCoppie[0] == 1)
        {
            punteggio[0] = ComboPoker.coppia;
            punteggio[1] = flagCoppie[1];
            return punteggio;
        }

        //10) carta alta
        punteggio[0] = ComboPoker.cartaAlta;
        punteggio[1] = numbers.getLast();
        return punteggio;
    }

    private Integer[] coppie(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];

        Arrays.fill(occur, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        int counter = 0, highest = 0;

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 2)
            {
                counter++;
                highest = i+2;
            }
        }

        if(counter > 0)
        {
            //numero di coppie, valore coppia più alta
            Integer[] res = {counter, highest};

            return res;
        }
        else return null;
    }

    private Integer tris(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];

        Arrays.fill(occur, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 3) return i+2;
        }
        return null;
    }

    private Integer poker(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];

        Arrays.fill(occur, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 4) return i+2;
        }
        return null;
    }

    //ritorna il valore della scala
    private Integer scala(ArrayList<Integer> numbersSorted)
    {
        int primo = numbersSorted.get(0);

        if(numbersSorted.get(0) == 2
        && numbersSorted.get(1) == 3
        && numbersSorted.get(2) == 4
        && numbersSorted.get(3) == 5
        && numbersSorted.get(4) == 14) return 5;

        for(int i = 1; i < 5; i++)
        {
            if(primo != numbersSorted.get(i) - i) return null;
        }

        return numbersSorted.get(4);
    }

    private boolean colore(ArrayList<Seme> seeds)
    {
        Seme seme = seeds.get(0);

        for(Seme i : seeds)
        {
            if(!i.equals(seme)) return false;
        }

        return true;
    }

    public int calcolaPremio(int numeroVincitori)
    {
        return pot/numeroVincitori;
    }

    public int calcolaPremio(int numeroVincitori, int sidePot)
    {
        return sidePot/numeroVincitori;
    }

    public int puntataSpinnerValue(int saldoGiocatore)
    {
        if(puntataAttuale > saldoGiocatore) return saldoGiocatore;
        else return puntataAttuale;
    }

    public void sidePot(int index)
    {
        ManoPoker temp = (ManoPoker) listaMani.get(index);
        int saldoPuntato = temp.getPuntataTotalePartita();
        int potEffettiva = 0;

        for(Mano i : listaMani)
        {
            ManoPoker j = (ManoPoker) i;
            if(j.getPuntataTotalePartita() > saldoPuntato) potEffettiva += saldoPuntato;
            else potEffettiva += j.getPuntataTotalePartita();
        }

        temp.setSidePot(potEffettiva);
    }

    public void eliminaMano(int index)
    {
        listaMani.remove(index);
    }

    public boolean isHandAllIn(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        return temp.isAllIn();
    }

    public void setHandAllIn(int index, boolean value)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        temp.setAllIn(value);
    }

    public void incrementaPuntataTotalePartita(int index, int valore) {
        ManoPoker temp = (ManoPoker) getMano(index);
        temp.incrementaPuntataTotalePartita(valore);
    }

    public int getPuntataTotalePartita(int index) {
        ManoPoker temp = (ManoPoker) getMano(index);
        return temp.getPuntataTotalePartita();
    }

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getPot() {
        return pot;
    }

    public void incrementaPot(int incremento) {
        pot += incremento;
    }

    public void resetPot()
    {
        pot = 0;
    }

    public boolean isAlmenoUnGiro() {
        return almenoUnGiro;
    }

    public void setAlmenoUnGiro(boolean almenoUnGiro) {
        this.almenoUnGiro = almenoUnGiro;
    }
}
