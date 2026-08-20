package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.poker.ControllerPoker;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;
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

public class GUIPoker {
    private JPanel mano;
    private JLabel indicatoreMani;
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

    private ArrayList<ClientWelcomeController> sessioniCorrenti;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Poker");
        frame.setContentPane(new GUIPoker().pokerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //[0]
    public GUIPoker()
    {
        sessioniCorrenti = new ArrayList<>();

        //caricamento immagine deck
        Image img = new ImageIcon(
                getClass().getResource("/carte2/42_kerenel_Cards.png")
        ).getImage();
        mazzo.setIcon(new ImageIcon(img));

        azioniButton(false);
        relativiRilancia(false);
        vediCarteButton.setVisible(false);
        rimischiaButton.setVisible(false);
        okButton.setVisible(false);
        risultatiLabel.setVisible(false);

        //TODO da modificare max con numero di posti
        SpinnerNumberModel modelloSpinnerNplayer = new SpinnerNumberModel(2, 2, 5, 1);
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

            }
        });
        giocaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmani = (int) spinnerNplayer.getValue();
                controller = new ControllerPoker(nmani);

                int ante = (int) spinnerAnte.getValue();
                controller.setAnte(ante);

                while(sessioniCorrenti.size() < nmani)
                {
                    String username = JOptionPane.showInputDialog(null,
                            "inserisci il tuo username per aggiungerti alla partita");
                    String password = JOptionPane.showInputDialog(null, "inserisci la password");

                    try {
                        Cliente cliente = controller.caricaPlayer(username, password, sessioniCorrenti);

                        //TODO a 2 errori consecutivi si esce dalla schermata
                        if(cliente == null) JOptionPane.showMessageDialog(null,
                                "credenziali sbagliate, al 3o login fallito, si ritornerà in seleziona tavoli",
                                "errore", JOptionPane.ERROR_MESSAGE);
                        else
                        {
                            sessioniCorrenti.add(new ClientWelcomeController(cliente));
                            //TODO da modificare con comunicazione con altre interfacce
                            sessioniCorrenti.getLast().creaNuovaSessioneDiGioco(new Tavolo(0, Gioco.Blackjack,
                                    0));
                            JOptionPane.showMessageDialog(null,
                                    "registrazione avvenuta con successo");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "errore", JOptionPane.ERROR_MESSAGE);
                    } catch (RuntimeException ex2) {
                        JOptionPane.showMessageDialog(null, ex2.getMessage(),
                                "errore", JOptionPane.ERROR_MESSAGE);
                    }
                }

                pescataIniziale();
            }
        });
    }

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
            sessioniCorrenti.remove(i);
            controller.eliminaMano(i);
        }

        //TODO uscita se rimane solo un giocatore che può giocare
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

                //se puntata attuale > del saldo min = saldo e viceversa
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
                if(confermaButton.isVisible()) relativiRilancia(false);
                else relativiRilancia(true);
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

                nextHand1(true);
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

                if(checkButton.getText().equals("check")) displayBettingEvents(EventiPoker.check);
                else displayBettingEvents(EventiPoker.call);

                nextHand1(true);
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
                    nextHand1(true);
                }

            }
        });
    }

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
                if(confermaButton.isVisible()) relativiRilancia(false);
                else relativiRilancia(true);
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = ((int) spinnerPuntata.getValue()) - controller.getMano(currentHand).getPuntata();
                if(!decrementa(input, currentHand)) return;

                controller.getMano(currentHand).incrementaPuntata(input);
                aggiornaPot(input);
                controller.setPuntataAttuale((int) spinnerPuntata.getValue());

                if(controller.getPuntataAttuale() == input)
                    displayBettingEvents(EventiPoker.bet);
                else displayBettingEvents(EventiPoker.raise);

                nextHand1(false);
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tettoMax;
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

                nextHand1(false);
            }
        });

        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer vincitore = controller.setFolded(currentHand);

                displayBettingEvents(EventiPoker.fold);

                if (vincitore != null) {
                    vittoriaPerFold(vincitore);
                } else {
                    nextHand1(false);
                }
            }
        });
    }

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

    //[5] vincitore e reset
    //TODO puoi renderlo più elegante se hai tempo
    public void vittoriaReset(boolean foldFlag)
    {
        rimuoviActionListener(okButton);
        risultatiLabel.setVisible(false);

        if(!foldFlag){
            //segmento di codice se arrivi qua da mostrareLeCarte (non è vittoria per fold)

            //indici dei vincitori, possono essere più di uno in caso di pareggio
            ArrayList<Integer> indiciVincitori = controller.trovaVincitori(null);
            ArrayList<Integer> listaEsclusi = new ArrayList<>();

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

                //se non ho vincitori in allin da gestire esco
                //System.out.println(controller.soloUnGiocatore(listaEsclusi));
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

            }
        });
    }

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
    private void startingButton(boolean visibilità)
    {
        indietroButton.setVisible(visibilità);
        labelNplayer.setVisible(visibilità);
        spinnerNplayer.setVisible(visibilità);
        giocaButton.setVisible(visibilità);
        spinnerAnte.setVisible(visibilità);
        labelAnte.setVisible(visibilità);
    }

    private void azioniButton(boolean visibilità)
    {
        puntaButton.setVisible(visibilità);
        checkButton.setVisible(visibilità);
        foldButton.setVisible(visibilità);
        //usernameLabel.setVisible(visibilità);
    }

    private void relativiRilancia(boolean visibilità)
    {
        spinnerPuntata.setVisible(visibilità);
        confermaButton.setVisible(visibilità);
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
        };

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

    public void clearLog()
    {
        logAvvenimenti.setText(null);
    }
}
