package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.poker.ControllerPoker;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.Carte.EventiPoker;
import model.giochi.Carte.ManoPoker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Per la programmazione di questo gioco, l'operazione che è stata fatta è: sintetizzare gli stati del gioco del Poker
 * e successivamente tradurli in funzione. Quindi il ciclo di gioco viene inteso come macchina a stati in cui ogni
 * funzione rappresenta uno stato, queste funzioni sono numerate da [0] a [5]. L'area di testo in alto a sinistra mostra
 * che combo ha il giocatore, l'area di testo in alto a destra invece è un log di tutte le azioni svolte dai giocatori nel match
 * corrente.
 * [0] GUIPoker
 * [1] pescataIniziale
 * [2] rimischiata
 * [3] puntata2
 * [4] mostrareLeCarte
 * [5] vittoriaReset
 * Solo [0] è eseguito solo una volta in ingresso, gli altri 5 stati vanno in loop finchè il giocatore non sceglie di uscire
 * in fase [5]
 */
public class GUIPoker {
    private JPanel mano;
    private JPanel pokerPanel;
    private JLabel mazzo;
    private JButton puntaButton;
    private JButton checkButton;
    private JButton foldButton;
    private JSpinner spinnerPuntata;
    private JButton confermaButton;
    private JButton indietroButton;
    private JButton giocaButton;
    private JSpinner spinnerNplayer;
    private JLabel labelNplayer;
    private JButton vediCarteButton;
    private JSpinner spinnerAnte;
    private JLabel labelAnte;
    private JLabel usernameLabel;
    private JLabel saldo;
    private JLabel pot;
    private JButton rimischiaButton;
    private JButton okButton;
    private JTextPane infoTextPane;
    private JTextArea logAvvenimenti;
    private JLabel risultatiLabel;

    //conta qual è la mano corrente
    private int currentHand = 0;

    private ControllerPoker controller;
    private ClientWelcomeController sessioneCorrente;

    private final ArrayList<ClientWelcomeController> sessioniCorrenti;

    private final JFrame thisFrame;
    private final JFrame frameChiamante;

    /**
     * Il costruttore funge come funzione di setup per il gioco, oltre a svolgere le classiche mansioni da costruttore,
     * in questa fase l'host può scegliere il valore dell'ante (puntata di ingresso a buio, vedi 'regolePoker') (da 0 a massimo 1000) e
     * il numero di giocatori (da 2 al numero di posti). Una volta clickato il gioca button gli altri utenti potranno effettuare
     * il login al loro account per unirsi al tavolo, se 3 tentativi di login di fila vengono falliti si ritorna nella
     * schermata di selezione del tavolo.
     *
     * @param frameChiamante frameChiamante serve per gestire visibilità dei Frame
     * @param host           serve per lo scambio di dati relativi a giocatore con questo controller, in particolare questo
     *                       è il controller relativo all'host della partita di Poker, altri giocatori possono effettuare
     *                       il login per unirsi alla sessione e quindi ci sono più ClientWelcomeController
     * @param soldiTavolo    per restituire i soldi del tavolo all'host in cui non si vada oltre la fase 1
     */
//[0]
    public GUIPoker(JFrame frameChiamante, ClientWelcomeController host, int soldiTavolo)
    {
        thisFrame = new JFrame("Poker");
        thisFrame.setContentPane(pokerPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        this.frameChiamante = frameChiamante;

        sessioniCorrenti = new ArrayList<>();
        sessioniCorrenti.add(host);
        sessioneCorrente = host;

        //caricamento immagine deck
        Image img = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/carte2/42_kerenel_Cards.png"))
        ).getImage();
        mazzo.setIcon(new ImageIcon(img));

        azioniButton(false);
        relativiRilancia(false);
        vediCarteButton.setVisible(false);
        rimischiaButton.setVisible(false);
        okButton.setVisible(false);
        risultatiLabel.setVisible(false);

        SpinnerNumberModel modelloSpinnerNplayer = new SpinnerNumberModel(2, 2, sessioneCorrente.getPostiTavolo(), 1);
        spinnerNplayer.setModel(modelloSpinnerNplayer);
        ((JSpinner.DefaultEditor) spinnerNplayer.getEditor()).getTextField().setEditable(false);

        SpinnerNumberModel modelloSpinnerAnte = new SpinnerNumberModel(10, 0, 1000, 5);
        spinnerAnte.setModel(modelloSpinnerAnte);
        ((JSpinner.DefaultEditor) spinnerAnte.getEditor()).getTextField().setEditable(false);

        infoTextPane.setEditable(false);
        infoTextPane.setFocusable(false);

        logAvvenimenti.setEditable(false);
        logAvvenimenti.setFocusable(false);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    host.incrementaSaldoGiocatore(soldiTavolo);
                    for(ClientWelcomeController i : sessioniCorrenti)
                    {
                        i.terminaSessione();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
        giocaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmani = (int) spinnerNplayer.getValue();
                controller = new ControllerPoker(nmani);

                int ante = (int) spinnerAnte.getValue();
                controller.setAnte(ante);

                int counterErrori = 0;
                while(sessioniCorrenti.size() < nmani)
                {
                    String username = JOptionPane.showInputDialog(null,
                            "inserisci il tuo username per aggiungerti alla partita");
                    String password = JOptionPane.showInputDialog(null, "inserisci la password");

                    try {
                        Cliente cliente = controller.caricaPlayer(username, password, sessioniCorrenti);

                        if(counterErrori == 2)
                        {
                            host.incrementaSaldoGiocatore(soldiTavolo);
                            for(ClientWelcomeController i : sessioniCorrenti)
                            {
                                i.terminaSessione();
                            }

                            thisFrame.dispose();
                            frameChiamante.setVisible(true);

                            JOptionPane.showMessageDialog(null, "troppi login falliti",
                                    "errore", JOptionPane.ERROR_MESSAGE);

                            return;
                        }
                        if(cliente == null) {
                            JOptionPane.showMessageDialog(null,
                                    "credenziali sbagliate, al 3o login fallito, si ritornerà in seleziona tavoli",
                                    "errore", JOptionPane.ERROR_MESSAGE);
                            counterErrori++;
                        }
                        else
                        {
                            sessioniCorrenti.add(new ClientWelcomeController(cliente));
                            sessioniCorrenti.getLast().creaNuovaSessioneDiGioco(sessioneCorrente.getTavoloCorrente());
                            JOptionPane.showMessageDialog(null,
                                    "registrazione avvenuta con successo");
                            counterErrori = 0;
                        }
                    } catch (SQLException | RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "errore", JOptionPane.ERROR_MESSAGE);
                    }
                }

                pescataIniziale();
            }
        });
    }

    /**
     * In questa fase del gioco i giocatori a turno visualizzano le loro carte e decidono che azione intraprendere.
     * Prima di ciò un ciclo prende tutti i giocatori con fiches < dell'ante (giocatori che non possono più giocare) e
     * li elimina da sessioniCorrenti dopo averne chiuso le sessioni, dopo di ciò viene effettuato un altro controllo,
     * se sessioniCorrenti ha un solo giocatore la sessione si conclude perché non c'è più nessuno. Quando si è compiuto
     * almeno un giro di puntate e tutti i giocatori hanno la stessa puntata si va allo stato di gioco [2], se tutti hanno
     * foldato si va direttamente allo stato di gioco [5]
     */
//[1]
    public void pescataIniziale()
    {
        clearLog();
        pot.setVisible(true);
        //puntata più alta per giro di puntata settata a 0
        controller.setPuntataAttuale(0);
        //in questo caso con questa funzione si resettano i pulsanti a check e punta
        variazionePulsantePerPuntataPuntaORilancia();

        //eliminazione dei giocatori con un saldo minore dell'ante
        ArrayList<Integer> giocatoriDaEliminare = new ArrayList<>();
        for(int i = 0; i < sessioniCorrenti.size(); i++)
        {
            if(!decrementa(controller.getAnte(), i))
            {
                giocatoriDaEliminare.add(i);
            }
        }
        giocatoriDaEliminare.sort(Collections.reverseOrder());
        for(int i : giocatoriDaEliminare)
        {
            try {
                sessioniCorrenti.get(i).terminaSessione();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
            }
            sessioniCorrenti.remove(i);
            controller.eliminaMano(i);
        }

        //uscita se rimane solo un giocatore che può giocare
        if(sessioniCorrenti.size() == 1)
        {
            JOptionPane.showMessageDialog(null, "il giocatore " + sessioneCorrente.getClienteUsername() +
                    " è l'unico rimasto al tavolo, la sessione è stata chiusa");
            try {
                sessioneCorrente.terminaSessione();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
            }

            thisFrame.dispose();
            frameChiamante.setVisible(true);
        }


        //pot portata a 0
        controller.resetPot();
        //pot aggiornata con gli ante di tutti i giocatori in partita
        aggiornaPot(controller.getAnte()*sessioniCorrenti.size());
        rimuoviActionListener(vediCarteButton);
        rimuoviActionListener(puntaButton);
        rimuoviActionListener(confermaButton);
        rimuoviActionListener(checkButton);
        rimuoviActionListener(foldButton);

        //visibilità pulsanti
        startingButton(false);
        vediCarteButton.setVisible(true);
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        usernameLabel.setVisible(true);
        saldo.setVisible(false);

        controller.serviCarte();
        controller.calcolaComboTutti();

        sessioneCorrente = sessioniCorrenti.get(currentHand);

        listenerFasePuntata(true);
    }

    /**
     * In questa fase i giocatori a turno possono selezionare da 0 a 4 carte della mano e poi clickare sul pulsante
     * 'rimischia carte' per rimischiare le carte selezionate, il giocatore sia prima che dopo avere mischiato le carte
     * può visualizzare il valore della combo attuale in alto a sinistra, dopo aver rimischiato il giocatore può visualizzare
     * le carte ottenute finché non clicka sull'apposito bottone per andare avanti. Quando l'ultimo giocatore ha finito di
     * rimischiare e visualizzare le carte viene chiamata la funzione dello stato [3]
     */
//[2]
    public void rimischiata()
    {
        //System.out.println("sono qui");
        rimuoviActionListener(vediCarteButton);
        rimuoviActionListener(rimischiaButton);
        rimuoviActionListener(okButton);

        resetCurrentHand();
        sessioneCorrente = sessioniCorrenti.get(currentHand);
        usernameLabel.setText(sessioneCorrente.getClienteUsername());

        vediCarteButton.setVisible(true);
        saldo.setVisible(false);

        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mano.removeAll();
                //disegna carte selezionabili
                disegnaCarteClickabili();
                refreshPanel(mano);

                displayComboName();

                vediCarteButton.setVisible(false);
                saldo.setVisible(true);
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

                rimischiaButton.setVisible(true);
            }
        });
        rimischiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.rimischiataMano(currentHand);
                //messaggio log
                displayNCarteRimischiate(controller.getNumeroCarteSelezionate(currentHand));

                mano.removeAll();
                disegnaCarte();
                refreshPanel(mano);

                controller.calcolaComboSingolo(currentHand);
                displayComboName();

                rimischiaButton.setVisible(false);
                okButton.setVisible(true);
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButton.setVisible(false);
                vediCarteButton.setVisible(true);
                mano.removeAll();

                infoTextPane.setText(null);

                nextHand2(true);
            }
        });
    }

    /**
     * Questa fase è quasi identica alla fase 1, i giocatori adesso con le nuove mani possono effettuare un altro giro di
     * puntate ripartendo da 0 come puntata più alta attuale, quando anche questo finisce però si va allo stato [4].
     * In più c'è un controllo all'inizio che verifica se tutti i giocatori sono in all-in, se è così si salta direttamente
     * alla fase [4].
     */
//[3]
    public void puntata2()
    {
        resetCurrentHandNoAllIn();
        controller.setPuntataAttuale(0);
        controller.resetPuntateMani();

        rimuoviActionListener(vediCarteButton);
        rimuoviActionListener(puntaButton);
        rimuoviActionListener(confermaButton);
        rimuoviActionListener(checkButton);
        rimuoviActionListener(foldButton);

        //per quando tutti i giocatori sono in all-in, per andare alla fase successiva
        if(currentHand >= sessioniCorrenti.size())
        {
            currentHand--;
            vediCarteButton.setVisible(false);
            nextHand1(false);
            return;
        }

        vediCarteButton.setVisible(true);
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        usernameLabel.setVisible(true);
        saldo.setVisible(false);
        checkButton.setText("check");
        puntaButton.setText("punta");

        sessioneCorrente = sessioniCorrenti.get(currentHand);

        listenerFasePuntata(false);
    }

    /**
     * In questa fase tutte le carte sono scoperte e tutti i giocatori possono vederle, i giocatori possono clickare il
     * pulsante apposito per visualizzare la mano del giocatore successivo
     */
//[4]
    public void mostrareLeCarte()
    {
        rimuoviActionListener(okButton);

        resetCurrentHand();
        sessioneCorrente = sessioniCorrenti.get(currentHand);
        usernameLabel.setText(sessioneCorrente.getClienteUsername());

        risultatiLabel.setVisible(true);
        okButton.setVisible(true);

        mano.removeAll();
        disegnaCarte();
        refreshPanel(mano);

        saldo.setVisible(false);
        okButton.setText("avanti");

        displayComboName();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextHand2(false);

                if(currentHand < sessioniCorrenti.size())
                {
                    displayComboName();

                    mano.removeAll();
                    disegnaCarte();
                    refreshPanel(mano);
                }
            }
        });
    }

    /**
     * 1) Se si arriva a questa funzione da [4]: l'algoritmo per gestire le vincite gira così, vengono calcolati i vincitori
     * tra tutte le mani non foldate e ne si prendono gli indici, vengono incrementate le percentuali di vincita di questi
     * giocatori, si entra in un ciclo while(true), tra i vincitori si prendono tutti quelli che sono in allin, se non ci
     * sono giocatori in allin o il giocatore in allin è l'ultimo e unico giocatore di cui si sta calcolando la vincita si
     * esce dal ciclo while. Se si prosegue nel ciclo, se ci sono più giocatori in allin che hanno vinto da gestire li si
     * ordina nell'arrayList in ordine crescente tramite la funzione del ControllerPoker (serve per l'algoritmo che calcola
     * la vincita per i giocatori in allin), si calcola la vincita per ognuno di questi giocatori e poi si calcolano nuovamente
     * i vincitori escludendo quelli in allin che sono già stati premiati, dopo di chè si ripete il ciclo finché non ci sono più
     * giocatori in allin nella lista dei vincitori. La complessità di questa funzione è dovuta al fatto che nel poker, se
     * un giocatore è in allin e altri giocatori continuano a puntare, giustamente il giocatore in allin non potrà aspirare
     * al piatto completo, ma a solo una sua parte equivalente alla somma delle puntate di tutti i giocatori a patto che non
     * superino i soldi che effettivamente il giocatore in allin ha puntato in tutto il match corrente. Questo comporta che
     * oltre al piatto principali si possano formare più sidepot da gestire per giocatori in allin che hanno pareggiato e
     * bisogna gestire anche questi casi limite, pure se è quasi impossibile che avvengano.
     * 2) Se si arriva a questa funzione da [1] o [3] per fold: non succede nulla visto che il premio per il giocatore rimasto
     * è gestito stesso in 'vittoriaPerFold', viene cambiata giusto la visibilità di alcuni pulsanti.
     * Successivamente i giocatori possono decidere se continuare a giocare o finire la sessione
     *
     * @param foldFlag questa variabile serve a indicare alla funzione come si è arrivati qui: true: tutti i giocatori tranne
     *                 uno hanno foldato e quindi si salta direttamente alla fase di reset false: ci sono 2 o più giocatori
     *                 che hanno puntato la stessa cifra e hanno scoperto le carte
     */
//[5] vincitore e reset
    public void vittoriaReset(boolean foldFlag)
    {
        rimuoviActionListener(okButton);
        rimuoviActionListener(indietroButton);
        risultatiLabel.setVisible(false);

        if(!foldFlag){
            //segmento di codice se arrivi qua da mostrareLeCarte (non è vittoria per fold)

            //indici dei vincitori, possono essere più di uno in caso di pareggio
            ArrayList<Integer> indiciVincitori = controller.trovaVincitori(null);
            ArrayList<Integer> listaEsclusi = new ArrayList<>();

            //aggiornamento tassi di vittoria dei giocatori
            for(int i = 0; i < sessioniCorrenti.size(); i++)
            {
                sessioniCorrenti.get(i).aggiornaVincitaPercentuale(indiciVincitori.contains(i));
            }

            String messaggioVittoria = "";

            while(true)
            {
                ArrayList<Integer> sideBetDaGestire = new ArrayList<>();

                //mi prendo da parte tutti i vincitori che sono in allin e quindi che hanno una side pot a sè
                for(int i : indiciVincitori)
                {
                    ManoPoker temp = (ManoPoker) controller.getMano(i);
                    if(temp.getSidePot() != null) sideBetDaGestire.add(i);
                }

                //se non ho vincitori in allin da gestire o è rimasto un solo giocatore da gestire esco
                if(sideBetDaGestire.isEmpty() || controller.soloUnGiocatore(listaEsclusi)) break;

                //evento se c'è pareggio tra giocatori in all-in, molto molto raro
                if(sideBetDaGestire.size() > 1) controller.sortPerSideBet(sideBetDaGestire);

                //gestione vincitori in allin
                for(int j = 0; j < sideBetDaGestire.size(); j++)
                {
                    int i = sideBetDaGestire.get(j);

                    //calcola il premio dalla side pot in base al numero di giocatori vincitori (sidepot/numero vincitori)
                    int sp = controller.calcolaPremio(indiciVincitori.size() - j,
                            ((ManoPoker) controller.getMano(i)).getSidePot());
                    sessioniCorrenti.get(i).incrementaSaldoGiocatore(sp);
                    //devo andare a sottrarre la vincita del giocatore dalla pot generale
                    controller.incrementaPot(-sp);
                    //per i giocatori a cui ho già calcolato il premio li metto in una lista di esclusione
                    listaEsclusi.add(i);

                    //per aggiungere i giocatori con side pot al messaggio di vittoria
                    ArrayList<Integer> temp = new ArrayList<>();
                    //questa operazione di formattazione messaggio vittoria va fatta nel for perché giocatori con
                    //side pot da gestire chiaramente possono avere sidepot diverse
                    temp.add(i);
                    messaggioVittoria += formattaMessaggioVittoria(temp, sp);

                    //ricalibra sidepot
                    controller.ricalibraSidePot(sp);
                }

                //si continua a ricalcolare la combo con gli esclusi finché non ci sono più allin da gestire
                indiciVincitori = controller.trovaVincitori(listaEsclusi);
            }

            if(!indiciVincitori.isEmpty() && controller.getPot() >= 0)
            {
                int premio = controller.calcolaPremio(indiciVincitori.size());
                if(premio != 0) messaggioVittoria += formattaMessaggioVittoria(indiciVincitori, premio);

                infoTextPane.setText(messaggioVittoria);

                for(int i : indiciVincitori)
                {
                    sessioniCorrenti.get(i).incrementaSaldoGiocatore(premio);
                }
            }

            indietroButton.setVisible(true);

            mano.removeAll();
            refreshPanel(mano);
        }
        else
        {
            //nel caso di vittoria per fold il premio viene gestito nella rispettiva funzione

            okButton.setVisible(true);
            indietroButton.setVisible(true);
        }

        //cambio di chi inizia per primo
        sessioniCorrenti.addLast(sessioniCorrenti.getFirst());
        sessioniCorrenti.removeFirst();
        controller.ruotaGiocatori();

        usernameLabel.setVisible(false);
        saldo.setVisible(false);
        pot.setVisible(false);

        okButton.setText("continua");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mano.removeAll();
                refreshPanel(mano);

                currentHand = 0;
                ogniBottone();
                controller.reinizializzaMazzo();
                controller.resettaMani();
                infoTextPane.setText(null);

                pescataIniziale();
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for(ClientWelcomeController i : sessioniCorrenti)
                    {
                        i.terminaSessione();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }


    /**
     * Funzione che formatta messaggio di vittoria mostrato nella text area sinistra a fine partita
     *
     * @param indiciVincitori indici dei vincitori
     * @param premio premio a loro assegnato
     * @return parte di messaggio formattato, il messaggio finale visualizzato può essere dato da queste funzioni eseguite
     * più volte
     */
    //formattazione messaggio di vittoria
    private String formattaMessaggioVittoria(ArrayList<Integer> indiciVincitori, int premio)
    {
        String messaggio = "";
        boolean moltepliciVincitori = false;

        for(int i : indiciVincitori)
        {
            messaggio += sessioniCorrenti.get(i).getClienteUsername();
            if(i != indiciVincitori.getLast())
            {
                moltepliciVincitori = true;
                messaggio += ", ";
            }
            else messaggio += " ";
        }

        if(moltepliciVincitori) messaggio += "vincono ";
        else messaggio += "vince ";

        messaggio += premio + "\n";

        return messaggio;
    }

    //funzioni di utility
    //gestione visibilità componenti
    private void startingButton(boolean visibilita)
    {
        indietroButton.setVisible(visibilita);
        labelNplayer.setVisible(visibilita);
        spinnerNplayer.setVisible(visibilita);
        giocaButton.setVisible(visibilita);
        spinnerAnte.setVisible(visibilita);
        labelAnte.setVisible(visibilita);
    }

    private void azioniButton(boolean visibilita)
    {
        puntaButton.setVisible(visibilita);
        checkButton.setVisible(visibilita);
        foldButton.setVisible(visibilita);
        //usernameLabel.setVisible(visibilità);
    }

    private void relativiRilancia(boolean visibilita)
    {
        spinnerPuntata.setVisible(visibilita);
        confermaButton.setVisible(visibilita);
    }

    //per togliere visibilità a tutti i bottoni
    private void ogniBottone()
    {
        startingButton(false);
        azioniButton(false);
        relativiRilancia(false);
        okButton.setVisible(false);
    }

    //macro
    private void prossimoPescata()
    {
        azioniButton(false);
        confermaButton.setVisible(false);
        spinnerPuntata.setVisible(false);
        vediCarteButton.setVisible(true);
        saldo.setVisible(false);

        mano.removeAll();
        refreshPanel(mano);
    }

    /**
     * Funzione che aggiunge al panel mano le immagini (label con png) delle 5 carte che ha il giocatore corrente
     */
    //funzioni che disegnano le carte
    private void disegnaCarte()
    {
        String pathIm;
        JLabel temp;

        for(int i = 0; i < 5; i++)
        {
            pathIm = controller.displayCard(currentHand, i);
            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));
            //System.out.println(temp + " " + pathIm);
            mano.add(temp);
        }
    }

    /**
     * Funzione simile a disegnaCarte, ma in più si occupa di associare un mouse listener alle label che è in grado di:
     * una volta che avviene il click sulla carta si prende il riferimento alla label rispettiva, si prende la mano corrente
     * e si accede al suo attributo ArrayList che contiene le informazioni su quali carte sono state toccate un numero dispari
     * di volte (l'idea è che le carte siano una sorta di switch on/off, un click le selziona, un secondo click le deseleziona)
     * e quindi sono candidate per essere rimischiate, sulla carta clickata che fa scattare il mouse listener concettualmente
     * viene controllato lo stato e si passa a quello inverso on -> off od off -> on. Essere on realmente vuol dire che l'indice,
     * associato alla carta alla creazione del listener, compare nell'ArrayList della mano e inoltre graficamente la carta
     * è contornata da un bordo rosso, essere off vuol dire l'inverso.
     */
    //visto che poi al turno successivo dal panel mano si cancellano tutti i riferimenti alle label (le carte)
    //in automatico il garbage collector disalloca sia queste che i listener a esse associate, ho controllato
    private void disegnaCarteClickabili()
    {
        String pathIm;
        JLabel temp;

        for(int i = 0; i < 5; i++)
        {
            pathIm = controller.displayCard(currentHand, i);
            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));

            temp.setCursor(new Cursor(Cursor.HAND_CURSOR));

            //per passare variabile esterna in un listener deve essere final
            final int indexCarta = i;

            temp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel cartaCliccata = (JLabel) e.getSource(); //restituisce la Jlabel clickata

                    ManoPoker manoTemp = (ManoPoker) controller.getMano(currentHand);

                    ArrayList<Integer> indexList = manoTemp.getCarteSelezionate();

                    if(indexList.contains(indexCarta))
                    {
                        //set border per creare o togliere bordo intorno alla carta
                        cartaCliccata.setBorder(null);

                        //per togliere l'oggetto effettivamente e non a un indice
                        indexList.remove((Object) indexCarta);
                    }
                    else
                    {
                        if(indexList.size() >= 4)
                        {
                            JOptionPane.showMessageDialog(null, "non puoi rimischiare tutta la mano",
                                    "errore", JOptionPane.ERROR_MESSAGE);

                            return;
                        }

                        cartaCliccata.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

                        indexList.add(indexCarta);
                    }

                    manoTemp.setCarteSelezionate(indexList);
                }
            });

            mano.add(temp);
        }
    }

    //per tutte le seguenti funzioni documentazione più precisa in GUIBlackJack
    private void refreshPanel(JPanel pannello)
    {
        //ricalcola la posizione delle componenti nel pannello
        pannello.revalidate();
        //renderizza i nuovi widget in maniera che possono essere visti
        pannello.repaint();
    }

    //pulisci action listener
    private void rimuoviActionListener(JButton pulsante)
    {
        for (ActionListener i : pulsante.getActionListeners()) {
            pulsante.removeActionListener(i);
        }
    }

    //transazioni
    private boolean decrementa(int input, int indice)
    {
        ClientWelcomeController sessioneCorrente = sessioniCorrenti.get(indice);
        try {
            sessioneCorrente.decrementaSaldoGiocatore(input);
            saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());
            controller.incrementaPuntataTotalePartita(indice, input);

            return true;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, sessioniCorrenti.get(indice).getClienteUsername()
                            + ": " + ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Questa funzione serve per gestire il passaggio del turno da un giocatore all'altro, prima di tutto si preoccupa
     * di settare lo stato di un giocatore in allin se questo non ha più fiches, poi viene incrementato l'indice che
     * indica la mano corrente. Il ciclo while true serve per continuare a verificare un numero indefinito di volte se
     * effettivamente la mano a cui si è avanzato può svolgere azioni (mano non foldata e non in allin) sennò si continua
     * ad andare avanti. Dopo il while se è avvenuto almeno un giro tra tutti i giocatori e tutti hanno la stessa puntata
     * si avanza di fase.
     *
     * @param fase true: se avviene un avanzamento di fase si va nello stato [2]
     *             false: se avviene un avanzamento di fase si va nello stato [4]
     */
    //aggiornamenti
    //gestione cambio player
    private void nextHand1(boolean fase)
    {
        if(sessioneCorrente.getSaldoGiocatore() == 0) {
            controller.setHandAllIn(currentHand, true);
            logAvvenimenti.append(sessioneCorrente.getClienteUsername() + " è in all-in\n");
        }

        //per non mostrare informazioni sulle combo degli avversari
        infoTextPane.setText(null);
        currentHand++;

        while(true)
        {
            if(currentHand >= sessioniCorrenti.size()) {
                //controllo perché bisogna almeno fare un giro di puntate
                controller.setAlmenoUnGiro(true);
                //reset
                currentHand = 0;
                //se tutti sono in allin si rischia il ciclo infinito quindi se dopo un giro bisogna verificare se
                //tutti siano in allin
                if(controller.tuttiAllin()) break;
            }

            if(!controller.getFolded(currentHand) && !controller.isHandAllIn(currentHand)) {
                break;
            }

            currentHand++;
        }

        if(controller.isAlmenoUnGiro() && controller.controlloStessePuntate())
        {
            controller.setAlmenoUnGiro(false);

            //serve perché in questo caso non viene eseguito prossimo pescata che esegue queste funzioni
            mano.removeAll();
            refreshPanel(mano);

            azioniButton(false);
            relativiRilancia(false);

            //la side pot va settata a fine fase perché il premio a cui può aspirare un giocatore è determinato anche
            //da se gli altri hanno chiamato
            for(int i = 0; i < sessioniCorrenti.size(); i++)
            {
                ClientWelcomeController temp = sessioniCorrenti.get(i);
                //setta la side pot per la mano di un giocatore in allin
                if(temp.getSaldoGiocatore() == 0) controller.sidePot(i);
            }

            if(fase) rimischiata();
            else mostrareLeCarte();
            return;
        }

        variazionePulsantePerPuntataPuntaORilancia();

        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        sessioneCorrente = sessioniCorrenti.get(currentHand);

        prossimoPescata();
    }

    /**
     * In base alla puntata più alta attuale cambia il nome dei pulsanti
     */
    public void variazionePulsantePerPuntataPuntaORilancia()
    {
        if(controller.getPuntataAttuale() != 0)
        {
            checkButton.setText("call");
            puntaButton.setText("rilancia");
        }
        else
        {
            checkButton.setText("check");
            puntaButton.setText("punta");
        }
    }

    /**
     * Questa funzione serve per gestire il passaggio del turno da un giocatore all'altro, lo fa incrementando l'indice
     * che indica la mano corrente. Ci sono controlli che mandano avanti questo indice finché non c'è una mano non foldata
     *
     * @param fase true: se avviene un avanzamento di fase si va nello stato [3]
     *             false: se avviene un avanzamento di fase si va nello stato [5]
     */
    private void nextHand2(boolean fase)
    {
        do {
            currentHand++;
            if(currentHand >= sessioniCorrenti.size())
            {
                if(fase) puntata2();
                else vittoriaReset(false);
                return;
            }
        }while(controller.getFolded(currentHand));

        saldo.setVisible(false);
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        sessioneCorrente = sessioniCorrenti.get(currentHand);
    }

    //aggiorna pot
    private void aggiornaPot(int valore)
    {
        controller.incrementaPot(valore);
        pot.setText("pot: " + controller.getPot());
    }

    //reset alla prima mano non foldata
    private void resetCurrentHand()
    {
        currentHand = 0;

        while(currentHand < sessioniCorrenti.size() && controller.getFolded(currentHand)) currentHand++;
    }

    //reset alla prima mano non foldata o in all-in
    private void resetCurrentHandNoAllIn()
    {
        currentHand = 0;

        while(currentHand < sessioniCorrenti.size() &&
                (controller.getFolded(currentHand) || controller.isHandAllIn(currentHand))) currentHand++;
    }


    //TODO gestione per allin che vince
    /**
     * Si occupa di premiare l'ultimo giocatore rimasto con tutta la pot rimasta
     *
     * @param indexVincitore indice del vincitore in sessioniCorrenti
     */
    //gestione vittoria per fold
    private void vittoriaPerFold(Integer indexVincitore)
    {
        if(indexVincitore == null) return;

        sessioneCorrente = sessioniCorrenti.get(indexVincitore);
        //serve per evitare problemi nel caso in cui si ha rilancio e tutti foldano (la flag viene settata e quindi deve
        // essere disabilitata se finisce la partita)
        controller.setAlmenoUnGiro(false);

        sessioneCorrente.incrementaSaldoGiocatore(controller.getPot());
        infoTextPane.setText(null);

        JOptionPane.showMessageDialog(null, "il giocatore " + sessioneCorrente.getClienteUsername() +
                " vince " + controller.getPot() + " perchè gli altri hanno foldato");

        mano.removeAll();
        refreshPanel(mano);
        ogniBottone();
        vittoriaReset(true);
    }

    //funzioni per le aree di testo
    private void displayComboName()
    {
        infoTextPane.setText(controller.nomeCombo(currentHand));
    }

    private void displayBettingEvents(EventiPoker x)
    {
        String azione;

        switch(x)
        {
            case check -> azione = "ha checkato";
            case call -> azione = "ha chiamato";
            case bet -> azione = "ha puntato ";
            case raise -> azione = "ha rilanciato a ";
            case fold -> azione = "ha foldato";
            default -> azione = "stato indefinito";
        }

        if(x == EventiPoker.bet || x == EventiPoker.raise)
        {
            azione += controller.getMano(currentHand).getPuntata();
        }

        logAvvenimenti.append(sessioneCorrente.getClienteUsername() + ": " + azione + "\n");
    }

    private void displayNCarteRimischiate(int numero)
    {
        logAvvenimenti.append(sessioneCorrente.getClienteUsername() + " ha rimischiato " + numero + " carte\n");
    }

    /**
     * Pulisce il log text area in alto a destra
     */
    public void clearLog()
    {
        logAvvenimenti.setText(null);
    }

    /**
     * Attiva i listener per tutte le azioni che un giocatore può svolgere nella fase [1] e [3]
     *
     * @param fase true: fase [1], false: fase [3]. Fase è utilizzato solo per essere passato come parametro di nextHand1
     */
    public void listenerFasePuntata(boolean fase)
    {
        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mano.removeAll();
                disegnaCarte();
                refreshPanel(mano);

                vediCarteButton.setVisible(false);
                azioniButton(true);
                saldo.setVisible(true);
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

                displayComboName();

                //se puntata attuale > del saldo: min = saldo e viceversa
                int min = controller.puntataSpinnerValue(sessioneCorrente.getSaldoGiocatore());
                SpinnerNumberModel modelloSpinnerPuntata = new SpinnerNumberModel(min,
                        min, sessioneCorrente.getSaldoGiocatore(), 1);
                //((JSpinner.DefaultEditor) spinnerPuntata.getEditor()).getTextField().setEditable(false);
                spinnerPuntata.setModel(modelloSpinnerPuntata);
            }
        });

        puntaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puntaButton è solo uno switch della visibilità dei pulsanti per il rilancio
                relativiRilancia(!confermaButton.isVisible());
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //il quantitativo da decrementare è quello del rilancio/puntata che si vuole fare - i soldi già messi
                int input = ((int) spinnerPuntata.getValue()) - controller.getMano(currentHand).getPuntata();
                if(!decrementa(input, currentHand)) return;

                controller.getMano(currentHand).incrementaPuntata(input);
                aggiornaPot(input);
                //si segna nel controller la puntata più alta
                controller.setPuntataAttuale((int) spinnerPuntata.getValue());

                //in base a se è avvenuta una puntata o rilancio si scrive un messaggio diverso nel log
                if(controller.getPuntataAttuale() == input)
                    displayBettingEvents(EventiPoker.bet);
                else displayBettingEvents(EventiPoker.raise);

                nextHand1(fase);
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tettoMax;
                //il solito controllo per il massimo della puntata (caso dei giocatori allin)
                if(controller.getPuntataAttuale() > sessioneCorrente.getSaldoGiocatore())
                    tettoMax = sessioneCorrente.getSaldoGiocatore();
                else tettoMax = controller.getPuntataAttuale();

                int input = tettoMax - controller.getMano(currentHand).getPuntata();

                if(!decrementa(input, currentHand)) return;

                controller.getMano(currentHand).incrementaPuntata(input);
                aggiornaPot(input);
                pot.setText("pot: " + controller.getPot());

                if(checkButton.getText().equals("check")) displayBettingEvents(EventiPoker.check);
                else displayBettingEvents(EventiPoker.call);

                nextHand1(fase);
            }
        });

        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setFolded controlla anche se è rimasto un solo giocatore non foldato e restituisce l'indice del vincitore
                Integer vincitore = controller.setFolded(currentHand);

                displayBettingEvents(EventiPoker.fold);

                if(vincitore != null){
                    vittoriaPerFold(vincitore);
                } else {
                    nextHand1(fase);
                }

            }
        });
    }
}
