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
import java.util.Collections;

/**
 * Controller realizzato per gestire la logica del Poker
 */
public class ControllerPoker extends ControllerMazzo
{
    private int puntataAttuale;
    private int pot;
    private int ante;
    private boolean almenoUnGiro;

    /**
     * Istanzia un nuovo ControllerPoker di nmani mani e con un sabot di un solo mazzo che verrà inizializzato con un
     * numero di carte <= 52.
     *
     * @param nmani numero di mani
     */
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

    /**
     * Funzione che serve 5 carte a ogni giocatore
     *
     * @throws DeckOut the deck out
     */
//a ognuno 5 carte
    public void serviCarte() throws DeckOut
    {
        for(Mano i : listaMani)
        {
            for(int j = 0; j < 5; j++) serviCarta(i);
        }
    }

    /**
     * Funzione che effettua un reset delle mani di Poker
     */
    public void resettaMani()
    {
        super.resettaMani(Gioco.Poker);
    }

    /**
     * Funzione per effettuare il login di cliente quando vuole unirsi a una partita hostata di poker, un giocatore già
     * loggato non può rifare il login
     *
     * @param username  username
     * @param password  password
     * @param loggedYet lista dei giocatori che hanno già compiuto il login
     * @return null se il login è fallito, un'istanza di cliente se il login ha avuto successo
     * @throws RuntimeException errore se il cliente è già loggato
     * @throws SQLException     the sql exception
     */
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
                    saldo[0], partiteGiocate[0], vincitaPercentualeTot[0], dataDiBan[0], motiviBan[0]);
        }
        return null;
    }

    /**
     * Data una mano di un certo indice in lista mani si rimuovono le carte che sono state selezionate e poi a questa mano
     * vengono riaggiunte lo stesso numero di carte
     *
     * @param index the index
     */
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

    /**
     * Gets puntata attuale.
     *
     * @return the puntata attuale
     */
    public int getPuntataAttuale() {
        return puntataAttuale;
    }

    /**
     * Sets puntata attuale.
     *
     * @param puntataAttuale the puntata attuale
     */
    public void setPuntataAttuale(int puntataAttuale) {
        this.puntataAttuale = puntataAttuale;
    }

    /**
     * Questa funzione imposta la flag di fold della mano del giocatore come true, ma si occupa anche di verificare quanti
     * giocatori sono rimasti in gioco, se n'è rimasto solo uno ne si restituisce l'indice in maniera tale che possa essere
     * gestita la sua vittoria
     *
     * @param index the index
     * @return the folded
     */
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

    /**
     * Funzione che verifica che tutti i giocatori abbiano puntato lo stesso numero di fiches nel turno corrente. Verifica
     * prima quale sia la puntata più alta, poi si controlla fra tutte le mani non in allin e non foldate se c'è un una
     * puntata minore della massima, in caso affermativo viene ritornato false, altrimenti true
     *
     * @return true: puntate uguali, false: puntate diverse
     */
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

    /**
     * Controlla se tutti sono in allin
     *
     * @return true: tutti i giocatori sono in allin, false: alcuni giocatori non sono in allin
     */
    public boolean tuttiAllin()
    {
        for(Mano i : listaMani)
        {
            ManoPoker j = (ManoPoker) i;
            if(!j.isAllIn() && !j.isFolded()) return false;
        }

        return true;
    }

    /**
     * Reset puntate mani.
     */
    public void resetPuntateMani()
    {
        for(Mano i : listaMani)
        {
            i.setPuntata(0);
        }
    }

    /**
     * Funzione che dice se la mano di indice index in listaMani è foldato oppure no
     *
     * @param index indice in lista mani
     * @return true: giocatore foldato, false: giocatore non foldato
     */
    public boolean getFolded(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);

        return temp.isFolded();
    }

    /**
     * Funzione di mapping numero:valore in poker
     *
     * @param carta carta
     * @return valore carta
     */
//rispetto al metodo di ControllerMazzo cambia solo il valore dell'asso
    public static int getValoreNumero(Carta carta) {
        int val = ControllerMazzo.getValoreNumero(carta);

        if(val == 1) val = 14;

        return val;
    }

    /**
     * Calcola la combo di ogni mano non foldata e registra il punteggio nella mano
     */
//essendo getValoreNumero un metodo di ControllerPoker valoreCombo l'ho lasciata in questo controller, a livello
    //di senso comune credo che ha più senso se l'interpretazione del valore delle carte sia nel controller piuttosto che
    //nella mano stessa, anche se devo riconoscere che se avessi scritto i metodi nelle mani sarebbe stato più elegante a
    //livello di codice
    public void calcolaComboTutti()
    {
        for(int i = 0; i < listaMani.size(); i++)
        {
            ManoPoker j = (ManoPoker) getMano(i);

            //per ogni mano che non è stata foldata e non compare nella lista di esclusione si calcola il valore della
            //combo
            if(!j.isFolded())
            {
                j.setValoreCombo(valoreCombo(j));
            }
        }
    }

    /**
     * Calcola la combo solo della mano in lista mani a indice index
     *
     * @param index indice
     */
    public void calcolaComboSingolo(int index)
    {
        ManoPoker j = (ManoPoker) getMano(index);

        j.setValoreCombo(valoreCombo(j));
    }

    /**
     * Questa funzione trova i vincitori fra tutte le mani non foldate e non messe in listaEsclusi. Un giocatore vince se
     * ha la combo più alta o tie-breaker più forti dell'avversario, nel rarissimo caso di un pareggio i giocatori vengono
     * messi entrambi nella lista dei vincitori
     *
     * @see ComboPoker
     * @param listaEsclusi lista dei giocatori da non considerare
     * @return lista vincitori
     */
//ritorna indici in listaMani dei vincitori
    public ArrayList<Integer> trovaVincitori(ArrayList<Integer> listaEsclusi)
    {
        ArrayList<ManoPoker> maniAttive = new ArrayList<>();

        for(int i = 0; i < listaMani.size(); i++)
        {
            ManoPoker j = (ManoPoker) getMano(i);

            //per ogni mano che non è stata foldata e non compare nella lista di esclusione si calcola il valore della
            //combo
            if(!j.isFolded() && (listaEsclusi == null || !listaEsclusi.contains(i)))
            {
                maniAttive.add(j);
            }
        }

        ArrayList<ManoPoker> vincitori = new ArrayList<>();

        //ordine lessicografico: a > b <-> valore combo di a < valore combo di b OR (valore combo di a = valore combo
        // di b AND tie-break di a > tie-break di b), ci sono più tie-breaker per diverse combo
        for(ManoPoker i : maniAttive)
        {
            if(vincitori.isEmpty()) vincitori.add(i);
            else
            {
                for(int j = 0; j < 6; j++)
                {
                    //appena viene determinato un vincitore tra due mani si rompe il ciclo
                    if(vincitori.getFirst().getValoreCombo()[j] < i.getValoreCombo()[j]) {
                        vincitori.clear();
                        vincitori.add(i);
                        break;
                    }
                    else if(vincitori.getFirst().getValoreCombo()[j] > i.getValoreCombo()[j]) break;

                    //all'ultimo giro se nessuno dei tie breaker ha funzionato ci metto i due vincitori
                    if(j == 5) vincitori.add(i);
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


    /**
     * Funzione che trova il nome della combo
     *
     * @param index indice in listaMani
     * @return la stringa del nome della combo del giocatore all'indice index
     */
    public String nomeCombo(int index)
    {
        ManoPoker mano = (ManoPoker) listaMani.get(index);

        //scoperto grazie ai quick fixes dei warning di intellij
        return switch (mano.getValoreCombo()[0]) {
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

    /**
     * Questa funzione restituisce il valore della combo come array di 6 interi, il 1o valore è una costante di ComboPoker,
     * gli altri sono tutti valori di tie-break, non tutte le combo hanno 5 tie-breaker quindi gli ultimi valori non utilizzati
     * dell'array sono 0. La presenza di una combo in una mano è verificata facendo check dalla combo più forte a quella
     * più debole, quando viene trovata una combo si ha un return. I vari controlli delle combo sono effettuate in altre
     * funzioni private che seguono questa
     *
     * @see ComboPoker
     * @param mano mano interessata
     * @return valore combo int[6]
     */
    //ritorna array che hanno 6 valori interi: 1) valore della combo (vedi ComboPoker), >2) tie-break
    private int[] valoreCombo(ManoPoker mano)
    {
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Seme> seeds = new ArrayList<>();

        //per ogni carta della mano prendo seme e numero
        for(Carta z : mano.getListaMano())
        {
            //i valori dei numeri vanno da 2 a 14, ho considerato l'asso come 14
            numbers.add(getValoreNumero(z));
            seeds.add(z.getSeme());
        }

        //sorting in ordine crescente
        numbers.sort(null);

        int[] punteggio = new int[6];
        Arrays.fill(punteggio, 0);

        //da ora in poi si effettuano diverse verifiche per trovare una combo nella mano a partire da quelle più forti
        //tutte le funzioni sono delle flag alla fine dei conti, anche quelle che restituiscono Integer o int[] perché considero
        //null come false e valore come true, però così, visto che il dato è intero posso anche conservare i valori dei
        //tie-breaker della combo
        Integer flagScala = scala(numbers);
        int[] flagColore = colore(seeds, numbers);

        //1) scala reale
        if(flagScala != null && flagScala == 14 && flagColore != null)
        {
            punteggio[0] = ComboPoker.scalaReale;
            return punteggio;
        }
        //2) scala colore
        if(flagScala != null && flagColore != null)
        {
            punteggio[0] = ComboPoker.scalaColore;
            punteggio[1] = flagScala;
            return punteggio;
        }

        int[] flagPoker = poker(numbers);

        //3) poker
        if(flagPoker != null)
        {
            punteggio[0] = ComboPoker.poker;
            punteggio[1] = flagPoker[0];
            punteggio[2] = flagPoker[1];
            return punteggio;
        }
        //4) colore
        if(flagColore != null)
        {
            punteggio[0] = ComboPoker.colore;
            punteggio[1] = flagColore[0];
            punteggio[2] = flagColore[1];
            punteggio[3] = flagColore[2];
            punteggio[4] = flagColore[3];
            punteggio[5] = flagColore[4];
            return punteggio;
        }

        int[] flagTris = tris(numbers);
        int[] flagCoppie = coppie(numbers);

        //5) full
        if(flagTris != null && flagCoppie != null)
        {
            punteggio[0] = ComboPoker.full;
            punteggio[1] = flagTris[0];
            punteggio[2] = flagCoppie[0];
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
            punteggio[1] = flagTris[0];
            punteggio[2] = flagTris[1];
            punteggio[3] = flagTris[2];
            return punteggio;
        }

        //8) doppia coppia
        if(flagCoppie != null && flagCoppie[3] == 0)
        {
            punteggio[0] = ComboPoker.doppiaCoppia;
            punteggio[1] = flagCoppie[0];
            punteggio[2] = flagCoppie[1];
            punteggio[3] = flagCoppie[2];
            return punteggio;
        }

        //9) coppia
        //&& flagCoppie[3] != 0
        if(flagCoppie != null)
        {
            punteggio[0] = ComboPoker.coppia;
            punteggio[1] = flagCoppie[0];
            punteggio[2] = flagCoppie[1];
            punteggio[3] = flagCoppie[2];
            punteggio[4] = flagCoppie[3];
            return punteggio;
        }

        //10) carta alta
        punteggio[0] = ComboPoker.cartaAlta;
        punteggio[1] = numbers.get(4);
        punteggio[2] = numbers.get(3);
        punteggio[3] = numbers.get(2);
        punteggio[4] = numbers.get(1);
        punteggio[5] = numbers.get(0);
        return punteggio;
    }

    //restituisce int[2]: 1) numero di coppie, 2) tie-breaker
    private int[] coppie(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];
        int[] tb = new int[4];

        Arrays.fill(occur, 0);
        Arrays.fill(tb, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        int counter = 0;
        ArrayList<Integer> couples = new ArrayList<>();
        ArrayList<Integer> singles = new ArrayList<>();

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 2)
            {
                counter++;
                couples.add(i+2);
            }
            if(occur[i] == 1) singles.add(i+2);
        }
        singles.sort(Collections.reverseOrder());

        if(counter == 2)
        {
            tb[0] = couples.getLast();
            tb[1] = couples.getFirst();
            tb[2] = singles.getFirst();

            return tb;
        }
        else if(counter == 1)
        {
            tb[0] = couples.getFirst();
            for(int i = 0; i < singles.size(); i++)
            {
                tb[i + 1] = singles.get(i);
            }

            return tb;
        }
        else return null;
    }

    //restituisce tie breaker
    private int[] tris(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];
        int[] tb = new int[3];
        ArrayList<Integer> hc = new ArrayList<>();

        Arrays.fill(occur, 0);
        Arrays.fill(tb, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 3) tb[0] = i+2;
            if(occur[i] == 1) hc.add(i+2);
        }

        if(tb[0] != 0)
        {
            hc.sort(Collections.reverseOrder());
            for(int i = 0; i < hc.size(); i++)
            {
                tb[i+1] = hc.get(i);
            }
            return tb;
        }
        else return null;
    }

    //restituisce tie breaker
    private int[] poker(ArrayList<Integer> numbersSorted)
    {
        int[] occur = new int[13];
        int[] tb = new int[2];

        Arrays.fill(occur, 0);
        Arrays.fill(tb, 0);

        for(int i : numbersSorted)
        {
            occur[i-2]++;
        }

        for(int i = 0; i < occur.length; i++)
        {
            if(occur[i] == 4) tb[0] = i + 2;
            if(occur[i] == 1) tb[1] = i + 2;
        }
        if(tb[0] != 0) return tb;
        else return null;
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

    private int[] colore(ArrayList<Seme> seeds, ArrayList<Integer> numbers)
    {
        Seme seme = seeds.getFirst();

        for(Seme i : seeds)
        {
            if(!i.equals(seme)) return null;
        }

        numbers.sort(Collections.reverseOrder());
        int[] tb = new int[5];

        for(int i = 0; i < numbers.size(); i++)
        {
            tb[i] = numbers.get(i);
        }

        return tb;
    }

    /**
     * Il premio è da spartire tra tutti i vincitori in egual modo se non ci sono side pot dovute a degli allin
     *
     * @param numeroVincitori numero di vincitori
     * @return premio
     */
    public int calcolaPremio(int numeroVincitori)
    {
        return pot/numeroVincitori;
    }

    /**
     * Calcolo del premio in caso ci sia una sidePot per un giocatore in allin
     *
     * @param numeroVincitori numero di vincitori
     * @param sidePot         side pot
     * @return premio
     */
    public int calcolaPremio(int numeroVincitori, int sidePot)
    {
        return sidePot/numeroVincitori;
    }

    /**
     * Per impostare correttamente il valore minimo dello spinner in base al saldo del giocatore, serve per gestire il caso
     * in cui la puntata per giocare supera il saldo del giocatore, il giocatore può comunque andare in allin e continuare
     * a giocare quindi min e max grazie a questa funzione combaciano e il giocatore può puntare
     *
     * @param saldoGiocatore saldo del giocatore
     * @return puntata minima
     */
    public int puntataSpinnerValue(int saldoGiocatore)
    {
        if(puntataAttuale > saldoGiocatore) return saldoGiocatore;
        else return puntataAttuale;
    }

    /**
     * Calcola la sidePot della mano in listaMani all'indice index
     *
     * @param index indice
     */
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

    /**
     * Ruota l'ordine delle mani in lista mani in maniera tale che il primo giocatore sia l'ultimo a parlare e il secondo
     * il primo nel prossimo turno
     */
    public void ruotaGiocatori()
    {
        listaMani.addLast(listaMani.getFirst());
        listaMani.removeFirst();
    }

    /**
     * Funzione che serve nel caso in cui ci sono più giocatori in allin da gestire per le vincite, dopo che l'n-esimo ha
     * ritirato la sua vincita a quelli successivi deve essere sottratta dalla side pot il valore vinto dall'n-esimo giocatore
     *
     * @param value vincita del giocatore in allin
     */
    public void ricalibraSidePot(int value)
    {
        for(Mano i : listaMani)
        {
            ManoPoker j = (ManoPoker) i;
            Integer jSide = j.getSidePot();

            if(jSide != null)
            {
                if(jSide > value) j.setSidePot(jSide - value);
                else j.setSidePot(0);
            }
        }
    }

    /**
     * Controlla se c'è un solo giocatore non foldato in listamani - lista di esclusione
     *
     * @param esclusi lista di esclusione
     * @return true: c'è un solo giocatore attivo, false: più di un giocatore attivo
     */
    public boolean soloUnGiocatore(ArrayList<Integer> esclusi)
    {
        int counter = 0;

        for(Mano i : listaMani)
        {
            ManoPoker j = (ManoPoker) i;

            if(!j.isFolded() && !esclusi.contains(listaMani.indexOf(j))) counter++;
        }

        return counter == 1;
    }

    /**
     * Algoritmo di sorting per valore di side bet
     *
     * @param indexSB indici delle mani che hanno sideBet interessate
     */
    public void sortPerSideBet(ArrayList<Integer> indexSB)
    {
        ArrayList<Integer> arrVal = new ArrayList<>();

        for(int i : indexSB)
        {
            arrVal.add(((ManoPoker) getMano(i)).getSidePot());
        }

        for(int i = 0; i < indexSB.size() - 1; i++)
        {
            for(int j = i + 1; j < indexSB.size(); j++)
            {
                if(arrVal.get(j) < arrVal.get(i))
                {
                    int temp = arrVal.get(j );
                    arrVal.set(j, arrVal.get(i));
                    arrVal.set(i, temp);

                    temp = indexSB.get(j);
                    indexSB.set(j, indexSB.get(i));
                    indexSB.set(i, temp);
                }
            }
        }
    }

    /**
     * Elimina mano.
     *
     * @param index the index
     */
    public void eliminaMano(int index)
    {
        listaMani.remove(index);
    }

    /**
     * Is hand all in boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean isHandAllIn(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        return temp.isAllIn();
    }

    /**
     * Sets hand all in.
     *
     * @param index the index
     * @param value the value
     */
    public void setHandAllIn(int index, boolean value)
    {
        ManoPoker temp = (ManoPoker) getMano(index);
        temp.setAllIn(value);
    }

    /**
     * Incrementa l'attributo che registra la puntata totale che ha effettuato un giocatore nella partita. Questo dato serve
     * fondamentalmente per calcolare le side pot in caso di necessità
     *
     * @param index  indice giocatore
     * @param valore incremento
     */
    public void incrementaPuntataTotalePartita(int index, int valore) {
        ManoPoker temp = (ManoPoker) getMano(index);
        temp.incrementaPuntataTotalePartita(valore);
    }

    /**
     * Gets ante.
     *
     * @return the ante
     */
    public int getAnte() {
        return ante;
    }

    /**
     * Sets ante.
     *
     * @param ante the ante
     */
    public void setAnte(int ante) {
        this.ante = ante;
    }

    /**
     * Gets pot.
     *
     * @return the pot
     */
    public int getPot() {
        return pot;
    }

    /**
     * Incrementa pot.
     *
     * @param incremento the incremento
     */
    public void incrementaPot(int incremento) {
        pot += incremento;
    }

    /**
     * Reset pot.
     */
    public void resetPot()
    {
        pot = 0;
    }

    /**
     * Is almeno un giro boolean.
     *
     * @return the boolean
     */
    public boolean isAlmenoUnGiro() {
        return almenoUnGiro;
    }

    /**
     * Sets almeno un giro.
     *
     * @param almenoUnGiro the almeno un giro
     */
    public void setAlmenoUnGiro(boolean almenoUnGiro) {
        this.almenoUnGiro = almenoUnGiro;
    }

    /**
     * Restituisce il numero di carte selezionate in una mano nella fase di rimischiata
     *
     * @param index indice
     * @return numero carte selezionate
     */
    public int getNumeroCarteSelezionate(int index)
    {
        ManoPoker temp = (ManoPoker) getMano(index);

        return temp.getCarteSelezionate().size();
    }
}
