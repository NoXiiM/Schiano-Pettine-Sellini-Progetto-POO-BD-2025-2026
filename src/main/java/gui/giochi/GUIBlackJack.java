package gui.giochi;

import controller.blackjack.*;
import controller.gestionale.ClientWelcomeController;
import model.giochi.Carte.HandStateBJ;
import model.giochi.Carte.ManoBlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Per la programmazione di questo gioco, l'operazione che è stata fatta è: sintetizzare gli stati del gioco del BlackJack
 * e successivamente tradurli in funzione. Quindi il ciclo di gioco viene inteso come macchina a stati in cui ogni
 * funzione rappresenta uno stato, queste funzioni sono numerate da [0] a [5].
 * [0] GUIBlackJack
 * [1] puntare
 * [2] iniziaPartita
 * [3] turnoBanco
 * [4] risultatiGioco
 * [5] reset
 * Solo [0] è eseguito solo una volta in ingresso, gli altri 5 stati vanno in loop finchè il giocatore non sceglie
 * di uscire in fase [1] o [5]
 */
public class GUIBlackJack {
    //deck
    private JLabel deck;
    private JPanel blackjackPanel;
    //inizio partita
    private JButton startButton;
    private JSpinner spinnernMazzi;
    private JSpinner spinnernMani;
    private JLabel numeroMazzi;
    private JLabel numeroMani;
    //pulsanti
    private JButton staiButton;
    private JButton chiediButton;
    private JButton raddoppiaButton;
    private JButton dividiButton;
    //mani
    private JPanel manoGiocatorePanel;
    private JPanel manoBancoPanel;
    private JLabel manoTag;
    //pulsanti speciali
    private JButton assicuraButton;
    private JButton evenMoneyButton;
    private JButton rifiutaButton;
    //saldo e puntata
    private JLabel saldo;
    private JTextField textFieldPuntata;
    private JLabel puntata;
    private JButton immettiButton;
    private JButton okButton;
    //comunicazione
    private JLabel risultati;
    private JButton continuaButton;
    private JButton indietroButton;
    //relativo a sessione

    //conta qual è la mano corrente
    private int currentHand = 0;

    private ControllerBlackJack controller;
    private int mani;

    private final JFrame thisFrame;

    private final JFrame frameChiamante;
    private final ClientWelcomeController sessioneCorrente;

    /**
     * Il costruttore funge come funzione di setup per il gioco, oltre a svolgere le classiche mansioni da costruttore,
     * in questa fase del gioco è possibile scegliere il numero di mazzi (da 1 a 16) con cui giocare e quante mani
     * giocare da (da 1 al numero di posti del tavolo). Lo start button ti fa avanzare allo stato successivo chiamando la
     * funzione, l'indietro button ti fa tornare alla selezione dei tavoli
     *
     * @param frameChiamante   frameChiamante serve per gestire visibilità dei Frame
     * @param sessioneCorrente scambio di dati relativi a giocatore con questo controller
     */
    //[0]
    public GUIBlackJack(JFrame frameChiamante, ClientWelcomeController sessioneCorrente) {
        this.frameChiamante = frameChiamante;
        this.sessioneCorrente = sessioneCorrente;

        thisFrame = new JFrame("GUIBlackJack");
        thisFrame.setContentPane(blackjackPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        Dimension minDim = new Dimension(900, 500);
        thisFrame.setMinimumSize(minDim);

        //immagini
        //saldo
        saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

        //deck
        Image img = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/carte2/42_kerenel_Cards.png"))
        ).getImage();
        deck.setIcon(new ImageIcon(img));

        //spinner
        //spinner per numero di mazzi
        SpinnerNumberModel modelloSpinnerMazzi = new SpinnerNumberModel(1, 1, 16, 1);
        spinnernMazzi.setModel(modelloSpinnerMazzi);
        ((JSpinner.DefaultEditor) spinnernMazzi.getEditor()).getTextField().setEditable(false);

        //spinner per numero di mani
        SpinnerNumberModel modelloSpinnerMani = new SpinnerNumberModel(1, 1,
                sessioneCorrente.getPostiTavolo(), 1);
        spinnernMani.setModel(modelloSpinnerMani);
        ((JSpinner.DefaultEditor) spinnernMani.getEditor()).getTextField().setEditable(false);

        //pulsanti azioni inizialmente invisibili, solo start rimane visibile
        setVisibilityPulsantiSpeciali(false);
        pulsantiPuntVisibilita(false);
        okButton.setVisible(false);
        continuaButton.setVisible(false);
        staiButton.setVisible(false);
        chiediButton.setVisible(false);
        raddoppiaButton.setVisible(false);
        dividiButton.setVisible(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmazzi = (int) spinnernMazzi.getValue();
                mani = (int) spinnernMani.getValue();
                //System.out.println(nmazzi);

                controller = new ControllerBlackJack(nmazzi, mani);
                //controller.stampaCarte();

                //tolgo i pulsanti di inizializzazione
                spinnernMazzi.setVisible(false);
                spinnernMani.setVisible(false);
                numeroMazzi.setVisible(false);
                numeroMani.setVisible(false);
                startButton.setVisible(false);
                indietroButton.setVisible(false);
                //startButton.setText("exit");

                puntare();
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //per vedere se funziona, i dati della sessione poi andranno salvati nel DB

                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    /**
     * In questa fase del gioco si può scegliere quanto puntare per ognuna delle tue mani. Il pulsante immetti ti permette
     * di confermare la puntata per la mano, se la puntata è valida si procede alla mano successiva (currentHand++), se
     * currentHand che è l'indice della mano è uguale al numero di mani (sono finite le mani su cui puntare) viene chiamata
     * la funzione dello stato [2]
     */
    //[1]
    public void puntare()
    {
        rimuoviActionListener(startButton);
        rimuoviActionListener(immettiButton);
        rimuoviActionListener(indietroButton);
        rimuoviWindowListener(thisFrame);

        pulsantiPuntVisibilita(true);
        indietroButton.setVisible(true);

        puntata.setText("puntata per la mano " + (currentHand + 1));

        immettiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input;
                try {
                    input = Integer.parseInt(textFieldPuntata.getText());
                    if(input <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Non puoi puntare 0 o meno",
                                "errore di input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "La puntata deve essere composta da soli numeri",
                            "errore di input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!decrementa(input)) return;

                controller.getMano(currentHand).setPuntata(input);
                currentHand ++;

                if(currentHand == mani)
                {
                    pulsantiPuntVisibilita(false);
                    iniziaPartita();
                }
                else
                {
                    puntata.setText("puntata per la mano " + (currentHand + 1));
                }
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //per vedere se funziona, i dati della sessione poi andranno salvati nel DB
                sessioneCorrente.incrementaSaldoGiocatore(controller.restituisciPuntate());

                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sessioneCorrente.incrementaSaldoGiocatore(controller.restituisciPuntate());

                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    /**
     * In questa fase inizia la partita, il giocatore vede la prima carta del banco e le carte delle sue mani in ordine
     * e decide che azioni svolgere, vedesi il file 'regoleBlackJack' nella cartella documentazione.
     * Se l'azione intrapresa dal giocatore termina il turno della mano currentHand viene incrementato, come prima se
     * si va oltre l'ultima mano viene chiamata la funzione per andare alla fase successiva, in questo caso la [3]
     */
    //[2]
    public void iniziaPartita()
    {
        rimuoviActionListener(assicuraButton);
        rimuoviActionListener(evenMoneyButton);
        rimuoviActionListener(rifiutaButton);
        rimuoviActionListener(staiButton);
        rimuoviActionListener(chiediButton);
        rimuoviActionListener(raddoppiaButton);
        rimuoviActionListener(dividiButton);
        rimuoviWindowListener(thisFrame);

        indietroButton.setVisible(false);

        currentHand = 0;
        controller.serviCarte();

        //se banco ha bj o normale
        controller.setStatoBanco();

        paintCardsDealer1();
        paintCardsPlayer();
        pulsantiera();

        refreshManoTag();

        //pulsanti per condizioni di gioco speciali
        assicuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = controller.getMano(currentHand);
                if(!decrementa(manoCorrente.getPuntata()/2)) return;
                manoCorrente.setSideBet(manoCorrente.getPuntata() / 2);
                manoCorrente.setFlag(HandStateBJ.assicurazione);
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

                setVisibilityPulsantiSpeciali(false);
                setVisibilityPulsantiNormali(true);
            }
        });
        evenMoneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = controller.getMano(currentHand);
                manoCorrente.setFlag(HandStateBJ.evenmoney);

                setVisibilityPulsantiSpeciali(false);
                raddoppiaButton.setVisible(false);
                chiediButton.setVisible(false);
                staiButton.setVisible(true);
            }
        });
        rifiutaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityPulsantiSpeciali(false);
                if(controller.getMano(currentHand).getFlag().equals(HandStateBJ.bj)) staiButton.setVisible(true);
                else setVisibilityPulsantiNormali(true);
            }
        });
        //pulsanti in condizioni di gioco normali
        staiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manoGiocatorePanel.removeAll();
                setVisibilityPulsantiNormali(false);

                nextHand();
                if (currentHand >= mani)
                {
                    staiButton.setVisible(false);
                    chiediButton.setVisible(false);
                    raddoppiaButton.setVisible(false);
                    dividiButton.setVisible(false);

                    manoTag.setVisible(false);

                    turnoBanco();
                }
                else
                {
                    paintCardsPlayer();
                    refreshManoTag();
                    //per ridisegnare pannello aggiornato
                    refreshPanel(manoGiocatorePanel);
                    pulsantiera();
                }
            }
        });
        chiediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                raddoppiaButton.setVisible(false);
                dividiButton.setVisible(false);

                try {
                    controller.serviCarta(controller.getMano(currentHand));
                } catch (DeckOut ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
                    controller.serviCarta(controller.getMano(currentHand));
                }
                manoGiocatorePanel.removeAll();
                paintCardsPlayer();
                refreshPanel(manoGiocatorePanel);

                if(controller.getPoints(controller.getMano(currentHand)) > 21)
                {
                    chiediButton.setVisible(false);
                    risultati.setText("hai sballato");
                }
            }
        });
        raddoppiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = controller.getMano(currentHand);
                if(!decrementa(manoCorrente.getPuntata())) return;

                raddoppiaButton.setVisible(false);
                chiediButton.setVisible(false);
                dividiButton.setVisible(false);

                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());
                manoCorrente.raddoppio();

                try {
                    controller.serviCarta(manoCorrente);
                } catch (DeckOut ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
                    controller.serviCarta(manoCorrente);
                }
                manoGiocatorePanel.removeAll();
                paintCardsPlayer();
                refreshPanel(manoGiocatorePanel);

                if(controller.getPoints(manoCorrente) > 21)
                {
                    risultati.setText("hai sballato");
                }
            }
        });

        dividiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!decrementa(controller.getMano(currentHand).getPuntata())) return;
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

                controller.divisione(currentHand);

                //per gestire il setting delle flag se si fa un blackjack dopo lo split
                pulsantiera();

//                controller.serviCarta(controller.getMano(currentHand));
//                controller.serviCarta(controller.getMano(currentHand + 1));

                manoGiocatorePanel.removeAll();
                paintCardsPlayer();
                dividiButton.setVisible(false);
                refreshManoTag();
                mani += 1;
            }
        });
        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    /**
     * In questa fase viene rivelata la carta coperta del banco e poi a ogni ok pigiato dall'utente questo svolge una mossa,
     * la maniera in cui gioca il banco è algoritmica, il dealer non può realmente scegliere che mossa intraprendere.
     * Quando il banco termina il suo turno viene chiamata la funzione per andare alla fase 4.
     */
    //[3]
    public void turnoBanco()
    {
        rimuoviActionListener(okButton);

        manoBancoPanel.removeAll();

        paintCardsDealer2();
        refreshPanel(manoBancoPanel);

        okButton.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.algoritmoPescataBanco())
                {
                    manoBancoPanel.removeAll();
                    refreshPanel(manoBancoPanel);

                    paintCardsDealer2();
                }
                else risultatiGioco();
            }
        });
    }

    /**
     * In questa fase ripartendo dalla prima mano del giocatore, l'utente può visualizzare i risultati delle sue mani
     * andando avanti a suon di ok, quando si va oltre l'indice dell'ultima mano viene chiamata la funzione per andare
     * alla fase [4]. In questa fase chiaramente vengono ritornate anche le vincite per ogni puntata.
     */
    //[4]
    public void risultatiGioco()
    {
        rimuoviActionListener(okButton);
        manoTag.setVisible(true);

        currentHand = 0;

        paintCardsPlayer();

        refreshPanel(manoGiocatorePanel);
        refreshManoTag();

        gestionePremio();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextHand();

                if(currentHand >= mani)
                {
                    reset();
                }
                else
                {
                    manoGiocatorePanel.removeAll();
                    paintCardsPlayer();
                    refreshPanel(manoGiocatorePanel);
                    refreshManoTag();

                    gestionePremio();
                }
            }
        });
    }

    /**
     * Nella fase di reset vengono resettate tutte le mani e viene data l'opzione al giocatore di continuare o di tornare
     * alla schermata di selezione del tavolo. Se si continua viene richiamata la funzione della fase [1] e tutto inizia
     * da capo
     */
    //[5]
    public void reset()
    {
        rimuoviActionListener(continuaButton);
        rimuoviActionListener(indietroButton);

        indietroButton.setVisible(true);
        continuaButton.setVisible(true);
        okButton.setVisible(false);

        manoGiocatorePanel.removeAll();
        manoBancoPanel.removeAll();

        refreshPanel(manoGiocatorePanel);
        refreshPanel(manoBancoPanel);

        currentHand = 0;
        controller.resettaMani();

        continuaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mani -= controller.getIndiceRiduzioneMano();
                controller.setIndiceRiduzioneMano(0);
                indietroButton.setVisible(false);
                continuaButton.setVisible(false);

                if(controller.controlloCuttingCard())
                {
                    controller.reinizializzaMazzo();
                    JOptionPane.showMessageDialog(null,
                            "cut card raggiunta, il mazzo è stato rimischiato");
                }

                puntare();
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //per vedere se funziona, i dati della sessione poi andranno salvati nel DB

                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    /**
     * Funzione che decrementa input dalle fiches del giocatore se possibile, ed eventualmente modifica il campo di testo
     * che mostra il saldo
     *
     * @param input decrmenento
     * @return vero se questo valore è decrementabile dal giocatore, altrimenti falso
     */
//puntata con gestione saldo insufficiente
    public boolean decrementa(int input)
    {
        try {
            sessioneCorrente.decrementaSaldoGiocatore(input);
            saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

            return true;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Questa funzione si occupa di costruire i messaggi in base alla vincita della mano, di aggiornare le fiches del
     * giocatore e di cambiare il valore nel saldo nella label
     */
//gestione del messaggio nella fase [4] risultatiGioco
    public void gestionePremio()
    {
        String testoAssicurazione = "";

        int vincita = controller.calcolaVincita(currentHand);
        if(controller.getMano(currentHand).getFlag().equals(HandStateBJ.assicurazione))
        {
            if(controller.insuranceVinta(currentHand))
            {
                vincita += controller.getMano(currentHand).getSideBet() * 3;
                testoAssicurazione = "assicurazione vinta";
            }
            else testoAssicurazione = "assicurazione persa";
        }
        sessioneCorrente.incrementaSaldoGiocatore(vincita);

        if(vincita == controller.getMano(currentHand).getPuntata())
            risultati.setText("push " + vincita + " " + testoAssicurazione);
        else if(vincita > 0)
        {
            risultati.setText("hai vinto: " + vincita + " " + testoAssicurazione);
            sessioneCorrente.aggiornaVincitaPercentuale(true);
        }
        else
        {
            risultati.setText("hai perso" + " " + testoAssicurazione);
            sessioneCorrente.aggiornaVincitaPercentuale(false);
        }

        saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());
    }

    /**
     * Macro per impostare la visibilità di tutti i pulsanti speciali
     *
     * @param stato true: visibile, false: non visibile
     */
//funzioni visibilità pulsanti
    public void setVisibilityPulsantiSpeciali(boolean stato)
    {
        evenMoneyButton.setVisible(stato);
        assicuraButton.setVisible(stato);
        rifiutaButton.setVisible(stato);
    }

    /**
     * Macro per impostare la visibilità di tutti i pulsanti normali
     *
     * @param stato true: visibile, false: non visibile
     */
    public void setVisibilityPulsantiNormali(boolean stato)
    {
        staiButton.setVisible(stato);
        chiediButton.setVisible(stato);
        raddoppiaButton.setVisible(stato);
        if(controller.isSplittable(currentHand)) dividiButton.setVisible(stato);
    }

    /**
     * Macro per impostare la visibilità di tutti i pulsanti relativi alla puntata
     *
     * @param stato true: visibile, false: non visibile
     */
    public void pulsantiPuntVisibilita(boolean stato)
    {
        puntata.setVisible(stato);
        textFieldPuntata.setVisible(stato);
        immettiButton.setVisible(stato);
    }

    /**
     * Funzione che rimuove tutti gli action listener associati a un pulsante
     *
     * @param pulsante the pulsante
     */
//funzione per pulire tutti gli action listener di un jbutton
    public void rimuoviActionListener(JButton pulsante)
    {
        for (ActionListener i : pulsante.getActionListeners()) {
            pulsante.removeActionListener(i);
        }
    }

    /**
     * Funzione che rimuove tutti i window listener associati a un pulsante
     *
     * @param frame the frame
     */
//funzione per pulire tutti gli action listener di un jbutton
    public void rimuoviWindowListener(JFrame frame)
    {
        for (WindowListener i : frame.getWindowListeners()) {
            frame.removeWindowListener(i);
        }
    }

    /**
     * Macro per fare revalidate e repaint di un pannello insieme
     *
     * @param pannello the pannello
     */
//funzioni di aggiornamento
    public void refreshPanel(JPanel pannello)
    {
        //ricalcola la posizione delle componenti nel pannello
        pannello.revalidate();
        //renderizza i nuovi widget in maniera che possono essere visti
        pannello.repaint();
    }

    /**
     * Macro per aggiornare la label che tiene il conto delle mani
     */
    public void refreshManoTag()
    {
        manoTag.setText("Mano " + (currentHand + 1));
    }

    /**
     * Funzione che gestisce in base allo stato della mano corrente i pulsanti da mostrare all'inizio della fase [2] e
     * quando si ha una nuova mano
     */
    public void pulsantiera()
    {
        switch(controller.statoPartitaIniziale(currentHand))
        {
            case evenmoney:
                evenMoneyButton.setVisible(true);
                rifiutaButton.setVisible(true);
                break;
            case assicurazione:
                //sennò appare assicura e rifiuta dopo lo split
                if(controller.getIndiceRiduzioneMano() == 0)
                {
                    assicuraButton.setVisible(true);
                    rifiutaButton.setVisible(true);
                }
                break;
            case bj:
                JOptionPane.showMessageDialog(null, "Black Jack!");
                ManoBlackJack manoCorrente = controller.getMano(currentHand);
                manoCorrente.setFlag(HandStateBJ.bj);
                staiButton.setVisible(true);
                break;
            case normale:
                setVisibilityPulsantiNormali(true);
                break;
        }
    }

    /**
     * Funzione che aggiunge a manoBancoPanel l'immagine della prima carta (indice 0) del dealer e l'immagine di una
     * carta coperta
     */
//funzioni che "disegnano" le carte
    //dealer con seconda carta coperta
    public void paintCardsDealer1()
    {
        JLabel temp;

        //Objects.requireNonNull suggerito da quick fixes per warning di intellij
        temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(controller.displayCardDealer(0)))));
        manoBancoPanel.add(temp);
        temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/carte2/42_kerenel_Cards.png"))));
        manoBancoPanel.add(temp);
    }

    /**
     * Funzione che aggiunge a manoBancoPanel l'immagine di tutte le carte del dealer
     */
    //dealer con carte scoperte
    public void paintCardsDealer2()
    {
        String pathIm;
        JLabel temp;

        for(int j = 0; j < controller.getManoBancoSize(); j++)
        {
            pathIm = controller.displayCardDealer(j);

            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));
            manoBancoPanel.add(temp);
        }
    }

    /**
     * Funzione che aggiunge a manoGiocatorePanel l'immagine di tutte le carte della mano corrente del giocatore (di indice
     * currentHand)
     */
    public void paintCardsPlayer()
    {
        String pathIm;
        JLabel temp;

        //System.out.println("mano index: " + currentHand + " numero di carte: " + controller.getManoSize(currentHand));

        for(int j = 0; j < controller.getManoSize(currentHand); j++)
        {
            pathIm = controller.displayCard(currentHand, j);

            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));
            manoGiocatorePanel.add(temp);
        }
    }

    /**
     * Incrementa l'indice della mano corrente di 1 e si occupa anche di pulire la label dei risultati
     */
//funzione per passare alla mano successiva
    public void nextHand()
    {
        risultati.setText(null);
        currentHand += 1;
    }
}
