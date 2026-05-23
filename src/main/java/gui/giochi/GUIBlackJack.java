package gui.giochi;

import controller.blackjack.ControllerBlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.gestionale.utenteEFigli.Cliente;

public class GUIBlackJack {
    private JLabel PlayerLabel;
    //deck
    private JLabel deck;
    private JPanel pokerPanel;
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

    private int currentHand = 0;
    private Cliente current_client;

    private ControllerBlackJack controller;
    private JFrame frameChiamante;


    public GUIBlackJack(JFrame gameMenuChiamante, Cliente cliente) {

        this.frameChiamante= gameMenuChiamante;
        current_client= cliente;


        JFrame frame = new JFrame("GUIBlackJack");
        frame.setContentPane(pokerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //immagini
        int soldi = 1000;
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

        paintCardsDealer();
        paintCardsPlayer();
        pulsantiera();

        staiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manoGiocatorePanel.removeAll();
                nextHand();
                paintCardsPlayer();

                pulsantiera();
            }
        });
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

    public void paintCardsDealer()
    {
        JLabel temp;

        temp = new JLabel(new ImageIcon(getClass().getResource(controller.displayCardDealer(0))));
        manoBancoPanel.add(temp);
        temp = new JLabel(new ImageIcon(getClass().getResource("/carte2/42_kerenel_Cards.png")));
        manoBancoPanel.add(temp);
    }

    public void paintCardsPlayer()
    {
        String pathIm;
        JLabel temp;

        manoTag.setText("Mano 1");
        for(int j = 0; j < controller.getManoSize(currentHand); j++)
        {
            pathIm = controller.displayCard(currentHand, j);

            temp = new JLabel(new ImageIcon(getClass().getResource(pathIm)));
            manoGiocatorePanel.add(temp);
        }
    }

    public void nextHand(){currentHand += 1;}
}
