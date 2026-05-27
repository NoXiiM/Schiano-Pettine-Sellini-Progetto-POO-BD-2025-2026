package gui.giochi;

import controller.ClienteCorrente;
import controller.blackjack.*;
import model.gestionale.Sessione;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.HandStateBJ;
import model.giochi.ManoBlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private static JFrame thisFrame;

    private JFrame frameChiamante;
    private ClienteCorrente sessioneCorrente;

    public GUIBlackJack(JFrame frameChiamante, ClienteCorrente sessioneCorrente) {
        sessioneCorrente.startTimer();

        this.frameChiamante = frameChiamante;
        this.sessioneCorrente = sessioneCorrente;


        thisFrame = new JFrame("GUIBlackJack");
        thisFrame.setContentPane(blackjackPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        //immagini
        saldo.setText(String.valueOf(sessioneCorrente.getSaldoGiocatore()));

        Image img = new ImageIcon(
                getClass().getResource("/carte2/42_kerenel_Cards.png")
        ).getImage();
        deck.setIcon(new ImageIcon(img));

        //spinner
        SpinnerNumberModel modelloSpinnerMazzi = new SpinnerNumberModel(1, 1, 16, 1);
        spinnernMazzi.setModel(modelloSpinnerMazzi);

        SpinnerNumberModel modelloSpinnerMani = new SpinnerNumberModel(1, 1,
                sessioneCorrente.getPostiTavolo(), 1);
        spinnernMani.setModel(modelloSpinnerMani);

        //pulsanti azioni inizialmente invisibili, solo start rimane visibile
        //TODO: usa funzioni ad hoc
        staiButton.setVisible(false);
        chiediButton.setVisible(false);
        raddoppiaButton.setVisible(false);
        dividiButton.setVisible(false);
        assicuraButton.setVisible(false);
        evenMoneyButton.setVisible(false);
        rifiutaButton.setVisible(false);
        pulsantiPuntVisibilita(false);
        okButton.setVisible(false);
        continuaButton.setVisible(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmazzi = (int) spinnernMazzi.getValue();
                mani = (int) spinnernMani.getValue();

                controller = new ControllerBlackJack(nmazzi, mani);

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
                sessioneCorrente.stopTimer();
                //per vedere se funziona, i dati della sessione poi andranno salvati nel DB
                sessioneCorrente.aggiornaDatiCliente();
                sessioneCorrente.terminaSessione();
                System.out.println("secondi: " + sessioneCorrente.getTimeSecondi());
                System.out.println(sessioneCorrente.getTime());
                System.out.println(sessioneCorrente.stringaPercentuale());
                System.out.println(sessioneCorrente.getClienteCorrente());

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    public void puntare()
    {
        rimuoviActionListener(immettiButton);
        rimuoviActionListener(indietroButton);

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
                sessioneCorrente.stopTimer();
                //per vedere se funziona, i dati della sessione poi andranno salvati nel DB
                sessioneCorrente.incrementaSaldoGiocatore(controller.restituisciPuntate());
                sessioneCorrente.aggiornaDatiCliente();
                sessioneCorrente.terminaSessione();
                System.out.println("secondi: " + sessioneCorrente.getTimeSecondi());
                System.out.println(sessioneCorrente.getTime());
                System.out.println(sessioneCorrente.stringaPercentuale());
                System.out.println(sessioneCorrente.getClienteCorrente());

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    public void iniziaPartita()
    {
        rimuoviActionListener(assicuraButton);
        rimuoviActionListener(evenMoneyButton);
        rimuoviActionListener(rifiutaButton);
        rimuoviActionListener(staiButton);
        rimuoviActionListener(chiediButton);
        rimuoviActionListener(raddoppiaButton);
        rimuoviActionListener(dividiButton);

        indietroButton.setVisible(false);

        currentHand = 0;
        controller.serviCarte();

        controller.setStatoBanco();

        paintCardsDealer1();
        paintCardsPlayer();
        pulsantiera();

        refreshManoTag();

        //pulsanti per condizioni di gioco speciali
        assicuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
                if(!decrementa(manoCorrente.getPuntata()/2)) return;
                manoCorrente.setSideBet(manoCorrente.getPuntata() / 2);
                manoCorrente.setFlag(HandStateBJ.assicurazione);
                saldo.setText(String.valueOf(sessioneCorrente.getSaldoGiocatore()));

                setVisibilityPulsantiSpeciali(false);
                setVisibilityPulsantiNormali(true);
            }
        });
        evenMoneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
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
                setVisibilityPulsantiNormali(true);
            }
        });
        //pulsanti in condizioni di gioco normali
        staiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manoGiocatorePanel.removeAll();
//                if(controller.bancoHaAsso()) setVisibilityPulsantiNormali(false);
//                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
//                if (manoCorrente.getFlag() == HandStateBJ.bj) setVisibilityPulsantiNormali(true);
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

                controller.serviCarta(controller.getMano(currentHand));
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
                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
                if(!decrementa(manoCorrente.getPuntata())) return;

                raddoppiaButton.setVisible(false);
                chiediButton.setVisible(false);
                dividiButton.setVisible(false);

                //TODO: da modificare col collegamento
                saldo.setText(String.valueOf(sessioneCorrente.getSaldoGiocatore()));
                manoCorrente.raddoppio();

                controller.serviCarta(controller.getMano(currentHand));
                manoGiocatorePanel.removeAll();
                paintCardsPlayer();
                refreshPanel(manoGiocatorePanel);

                if(controller.getPoints(controller.getMano(currentHand)) > 21)
                {
                    risultati.setText("hai sballato");
                }
            }
        });
        //TODO: sistema questa funzionalità
        dividiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!decrementa(controller.getMano(currentHand).getPuntata())) return;
                saldo.setText(String.valueOf(sessioneCorrente.getSaldoGiocatore()));

                controller.divisione(currentHand);

//                controller.serviCarta(controller.getMano(currentHand));
//                controller.serviCarta(controller.getMano(currentHand + 1));

                manoGiocatorePanel.removeAll();
                paintCardsPlayer();
                dividiButton.setVisible(false);
                refreshManoTag();
                mani += 1;
            }
        });
    }

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

    public void reset()
    {
        rimuoviActionListener(continuaButton);

        indietroButton.setVisible(true);
        continuaButton.setVisible(true);
        okButton.setVisible(false);

        manoGiocatorePanel.removeAll();
        manoBancoPanel.removeAll();

        refreshPanel(manoGiocatorePanel);
        refreshPanel(manoBancoPanel);

        currentHand = 0;
        controller.resetAll(mani);

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
    }

    //gestione saldo insufficiente
    public boolean decrementa(int input)
    {
        try {
            sessioneCorrente.decrementaSaldoGiocatore(input);
            saldo.setText((String.valueOf(sessioneCorrente.getSaldoGiocatore())));

            return true;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //gestione del messaggio nella fase 4 risultatiGioco
    public void gestionePremio()
    {
        int vincita = controller.calcolaVincita(currentHand);
        sessioneCorrente.incrementaSaldoGiocatore(vincita);
        //TODO: forse non è la soluzione migliore
        if(vincita == controller.getMano(currentHand).getPuntata()) risultati.setText("push " + vincita);
        else if(vincita > 0)
        {
            risultati.setText("hai vinto: " + vincita);
            sessioneCorrente.aggiornaVincitaPercentuale(true);
        }
        else
        {
            risultati.setText("hai perso");
            sessioneCorrente.aggiornaVincitaPercentuale(false);
        }

        saldo.setText(String.valueOf(sessioneCorrente.getSaldoGiocatore()));
    }

    //funzioni visibilità pulsanti
    public void setVisibilityPulsantiSpeciali(boolean stato)
    {
        evenMoneyButton.setVisible(stato);
        assicuraButton.setVisible(stato);
        rifiutaButton.setVisible(stato);
    }

    public void setVisibilityPulsantiNormali(boolean stato)
    {
        staiButton.setVisible(stato);
        chiediButton.setVisible(stato);
        raddoppiaButton.setVisible(stato);
        if(controller.isSplittable(currentHand)) dividiButton.setVisible(stato);
    }

    public void pulsantiPuntVisibilita(boolean val)
    {
        puntata.setVisible(val);
        textFieldPuntata.setVisible(val);
        immettiButton.setVisible(val);
    }

    //funzione per pulire tutti gli action listener di un jbutton
    public void rimuoviActionListener(JButton pulsante)
    {
        for (ActionListener i : pulsante.getActionListeners()) {
            pulsante.removeActionListener(i);
        }
    }
    //funzioni di aggiornamento
    public void refreshPanel(JPanel pannello)
    {
        pannello.revalidate();
        pannello.repaint();
    }

    public void refreshManoTag()
    {
        manoTag.setText("Mano " + (currentHand + 1));
    }

    //sacrosantissima funzione che gestisce in base allo stato della mano corrente i pulsanti da mostrare all'inizio
    public void pulsantiera()
    {
        switch(controller.statoPartitaIniziale(currentHand))
        {
            case evenmoney:
                evenMoneyButton.setVisible(true);
                rifiutaButton.setVisible(true);
                break;
            case assicurabile:
                assicuraButton.setVisible(true);
                rifiutaButton.setVisible(true);
                break;
            case blackjack:
                JOptionPane.showMessageDialog(null, "Black Jack!");
                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
                manoCorrente.setFlag(HandStateBJ.bj);
                staiButton.setVisible(true);
                break;
            case normale:
                setVisibilityPulsantiNormali(true);
                break;
        }
    }

    //funzioni che "disegnano" le carte
    public void paintCardsDealer1()
    {
        JLabel temp;

        temp = new JLabel(new ImageIcon(getClass().getResource(controller.displayCardDealer(0))));
        manoBancoPanel.add(temp);
        temp = new JLabel(new ImageIcon(getClass().getResource("/carte2/42_kerenel_Cards.png")));
        manoBancoPanel.add(temp);
    }

    public void paintCardsDealer2()
    {
        String pathIm;
        JLabel temp;

        for(int j = 0; j < controller.getManoBancoSize(); j++)
        {
            pathIm = controller.displayCardDealer(j);

            temp = new JLabel(new ImageIcon(getClass().getResource(pathIm)));
            manoBancoPanel.add(temp);
        }
    }

    public void paintCardsPlayer()
    {
        String pathIm;
        JLabel temp;

        //System.out.println("mano index: " + currentHand + " numero di carte: " + controller.getManoSize(currentHand));

        for(int j = 0; j < controller.getManoSize(currentHand); j++)
        {
            pathIm = controller.displayCard(currentHand, j);

            temp = new JLabel(new ImageIcon(getClass().getResource(pathIm)));
            manoGiocatorePanel.add(temp);
        }
    }

    //funzione per passare alla mano successiva
    public void nextHand()
    {
        risultati.setText("");
        currentHand += 1;
    }
}
