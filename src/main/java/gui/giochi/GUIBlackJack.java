package gui.giochi;

import controller.blackjack.ControllerBlackJack;
import model.giochi.HandStateBJ;
import model.giochi.ManoBlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIBlackJack {
    private JLabel PlayerLabel;
    //deck
    private JLabel deck;
    private JPanel blackjackPanel;
    //inizio partita
    private JButton startButton;
    private JSpinner spinnernMazzi;
    private JSpinner spinnernMani;
    private JLabel numeroMazzi;
    private JLabel numeroMani;
    //azioni
    private JButton staiButton;
    private JButton chiediButton;
    private JButton raddoppiaButton;
    private JButton dividiButton;
    //mani
    private JPanel manoGiocatorePanel;
    private JPanel manoBancoPanel;
    private JLabel manoTag;
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

    private int currentHand = 0;
    //private Cliente current_client;

    private ControllerBlackJack controller;
    //private JFrame frameChiamante;

    //questo attributo va modificato e collegato col giocatore successivamente
    private int soldi = 1000;


    public static void main(String[] args) {
        JFrame frame = new JFrame("GUIBlackJack");
        frame.setContentPane(new GUIBlackJack().blackjackPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);;
    }

    public GUIBlackJack() {

//        this.frameChiamante= gameMenuChiamante;
//        current_client= cliente;


//        JFrame frame = new JFrame("GUIBlackJack");
//        frame.setContentPane(blackjackPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

        //immagini
        saldo.setText(String.valueOf(soldi));
        Image img = new ImageIcon(
                getClass().getResource("/carte2/42_kerenel_Cards.png")
        ).getImage();
        deck.setIcon(new ImageIcon(img));

        //spinner
        SpinnerNumberModel modelloSpinnerMazzi = new SpinnerNumberModel(1, 1, 16, 1);
        spinnernMazzi.setModel(modelloSpinnerMazzi);

        SpinnerNumberModel modelloSpinnerMani = new SpinnerNumberModel(1, 1, 5, 1);
        spinnernMani.setModel(modelloSpinnerMani);

        //pulsanti azioni
        staiButton.setVisible(false);
        chiediButton.setVisible(false);
        raddoppiaButton.setVisible(false);
        dividiButton.setVisible(false);
        assicuraButton.setVisible(false);
        evenMoneyButton.setVisible(false);
        rifiutaButton.setVisible(false);
        pulsantiPuntVisibilita(false);
        okButton.setVisible(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmazzi = (int) spinnernMazzi.getValue();
                int nmani = (int) spinnernMani.getValue();

                controller = new ControllerBlackJack(nmazzi, nmani);

                //tolgo i pulsanti
                spinnernMazzi.setVisible(false);
                spinnernMani.setVisible(false);
                numeroMazzi.setVisible(false);
                numeroMani.setVisible(false);
                startButton.setVisible(false);
                //startButton.setText("exit");

                puntare();
            }
        });
    }

    public void puntare()
    {
        pulsantiPuntVisibilita(true);
        int numeroPuntate = controller.getNumMani();

        puntata.setText("puntata per la mano " + (currentHand + 1));

        immettiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = Integer.parseInt(textFieldPuntata.getText());
                //modifica soldi
                soldi -= input;
                saldo.setText((String.valueOf(soldi)));

                controller.getMano(currentHand).setPuntata(input);
                currentHand ++;

                if(currentHand == numeroPuntate)
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
    }

    public void iniziaPartita()
    {
        currentHand = 0;
        controller.serviCarte();

        paintCardsDealer1();
        paintCardsPlayer();
        pulsantiera();

        refreshManoTag();

        //pulsanti per condizioni di gioco speciali
        assicuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
                manoCorrente.setSideBet(manoCorrente.getPuntata() / 2);
                manoCorrente.setFlag(HandStateBJ.assicurazione);

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
                setVisibilityPulsantiNormali(true);
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
                nextHand();
                if (currentHand >= controller.getNumMani())
                {
                    staiButton.setVisible(false);
                    chiediButton.setVisible(false);
                    raddoppiaButton.setVisible(false);
                    dividiButton.setVisible(false);

                    turnoBanco();
                }
                else
                {
                    paintCardsPlayer();
                    pulsantiera();
                    refreshManoTag();
                }

                //per ridisegnare pannello aggiornato
                refreshPanel(manoGiocatorePanel);
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
                raddoppiaButton.setVisible(false);
                chiediButton.setVisible(false);

                ManoBlackJack manoCorrente = (ManoBlackJack) controller.getMano(currentHand);
                soldi -= manoCorrente.getPuntata();
                saldo.setText(String.valueOf(soldi));
                manoCorrente.raddoppio();

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
    }

    public void turnoBanco()
    {
        rimuoviActionListenerOk();
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
        rimuoviActionListenerOk();
        currentHand = 0;

        paintCardsPlayer();

        refreshPanel(manoGiocatorePanel);
        refreshManoTag();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextHand();

                if(currentHand >= controller.getNumMani())
                {
                    reset();
                }
                else
                {
                    manoGiocatorePanel.removeAll();
                    paintCardsPlayer();
                    refreshPanel(manoGiocatorePanel);
                    refreshManoTag();
                }
            }
        });
    }

    public void reset()
    {

    }

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
        dividiButton.setVisible(stato);
    }

    public void rimuoviActionListenerOk()
    {
        for (ActionListener i : okButton.getActionListeners()) {
            okButton.removeActionListener(i);
        }
    }

    public void refreshPanel(JPanel pannello)
    {
        pannello.revalidate();
        pannello.repaint();
    }

    public void refreshManoTag()
    {
        manoTag.setText("Mano " + (currentHand + 1));
    }

    public void pulsantiPuntVisibilita(boolean val)
    {
        puntata.setVisible(val);
        textFieldPuntata.setVisible(val);
        immettiButton.setVisible(val);
    }

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
                staiButton.setVisible(true);
                chiediButton.setVisible(true);
                raddoppiaButton.setVisible(true);
                if(controller.isSplittable(currentHand)) dividiButton.setVisible(true);
                break;
        }
    }

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
            pathIm = controller.displayCardBanco(j);

            temp = new JLabel(new ImageIcon(getClass().getResource(pathIm)));
            manoBancoPanel.add(temp);
        }
    }

    public void paintCardsPlayer()
    {
        String pathIm;
        JLabel temp;

        for(int j = 0; j < controller.getManoSize(currentHand); j++)
        {
            pathIm = controller.displayCard(currentHand, j);

            temp = new JLabel(new ImageIcon(getClass().getResource(pathIm)));
            manoGiocatorePanel.add(temp);
        }
    }

    public void nextHand()
    {
        risultati.setText("");
        currentHand += 1;
    }
}
