package controller.poker;

import controller.blackjack.DeckOut;
import controller.gestionale.ClientWelcomeController;
import controller.mazzo.ControllerMazzo;
import database.implementazioneDAO.ImpDAOop;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.Carte.*;

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
        almenoUnGiro = false;
    }

    @Override
    public void reinizializzaMazzo() {
        mazzo = new Sabot(mazzo.getNumeroDiMazzi(), mazzo.getGioco());
        mazzo.inizializzaSabotPoker(listaMani.size());
        mazzo.mischiaMazzo();
    }

    //a ognuno 5 carte
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

    //i listener delle carte aggiungono gli indici nell'array attributo di manoPoker, rimuovi carte effettua questa
    //rimozione delle carte dalla mano
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
                if(contatore > 1) break;
                indiceVincitore = i;
            }
        }

        if(contatore == 1) return indiceVincitore;
        else return null;
    }

    public boolean controlloStessePuntate() {
        int maxPuntata = 0;

        //max puntata escludendo solo i foldati
        for (Mano i : listaMani) {
            ManoPoker temp = (ManoPoker) i;
            if (!temp.isFolded() && temp.getPuntata() > maxPuntata) {
                maxPuntata = temp.getPuntata();
            }
        }

        //adesso controllo anche tra i giocatori non in all in che abbiano tutti la stessa puntata
        for (Mano i : listaMani) {
            ManoPoker temp = (ManoPoker) i;

            if (!(temp.isFolded() || temp.isAllIn()) && temp.getPuntata() < maxPuntata) {
                return false;
            }
        }

        return true;
    }

    public boolean tuttiAllin()
    {
        for(Mano i : listaMani)
        {
            if(!((ManoPoker) i).isAllIn()) return false;
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

    //rispetto al metodo di ControllerMazzo cambia solo il valore dell'asso
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

            //per ogni mano che non è stata foldata e non compare nella lista di esclusione si calcola il valore della
            //combo
            if(!j.isFolded() && (listaEsclusi == null || !listaEsclusi.contains(i)))
            {
                ArrayList<Integer> numbers = new ArrayList<>();
                ArrayList<Seme> seeds = new ArrayList<>();

                //per ogni carta della mano prendo seme e numero
                for(Carta z : j.getListaMano())
                {
                    //i valori dei numeri vanno da 2 a 14, ho considerato l'asso come 14
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
        //TODO potenzialmente ci possono essere 5 tie breaker diversi, quindi l'array di ritorno potrei programmarlo come uno da 6
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


    public String nomeCombo(int index)
    {
        Mano mano = listaMani.get(index);

        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Seme> seeds = new ArrayList<>();

        for(Carta i : mano.getListaMano())
        {
            numbers.add(getValoreNumero(i));
            seeds.add(i.getSeme());
        }

        //scoperto grazie ai quick fixes dei warning di intellij
        return switch (valoreCombo(numbers, seeds)[0]) {
            case ComboPoker.scalaReale -> "scala reale";
            case ComboPoker.scalaColore -> "scala colore";
            case ComboPoker.poker -> "poker";
            case ComboPoker.colore -> "colore";
            case ComboPoker.full -> "full";
            case ComboPoker.scala -> "scala";
            case ComboPoker.tris -> "tris";
            case ComboPoker.doppiaCoppia -> "doppia coppia";
            case ComboPoker.coppia -> "coppia";
            default -> "carta alta";
        };
    }

    //ritorna array di 2 valori interi: 1) valore della combo (vedi ComboPoker), 2) tie-break
    private int[] valoreCombo(ArrayList<Integer> numbers, ArrayList<Seme> seeds)
    {
        //sorting in ordine crescente
        numbers.sort(null);

        int[] punteggio = new int[2];

        //da ora in poi si effettuano diverse verifiche per trovare una combo nella mano a partire da quelle più forti
        //tutte le funzioni sono delle flag alla fine dei conti, anche quelle che restituiscono Integer perché considero
        //null come false e valore come true, però così, visto che il dato è intero posso anche conservare il valore
        //tie-breaker della combo
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

    //restituisce int[2]: 1) numero di coppie, 2) tie-breaker
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
                //non mi serve calcolare il massimo in altro modo, so che l'ultima coppia registrata in occur è la più
                //alta, indice maggiore
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

    //restituisce tie breaker
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

    //restituisce tie breaker
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

    //ritorna il valore più alto della scala
    private Integer scala(ArrayList<Integer> numbersSorted)
    {
        int primo = numbersSorted.get(0);

        //asso fa sia scala con 10, j, q, k che con 2, 3, 4, 5; il secondo caso lo verifico meccanicamente in questo if
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
        Seme seme = seeds.getFirst();

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

        //aggiunge alla potEffettiva i soldi puntati da ogni giocatore a patto che siano minori uguali al saldo con cui
        //il giocatore interessato dalla funzione ha iniziato il round
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

    public int getNumeroCarteSelezionate(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);

        return temp.getCarteSelezionate().size();
    }
}
