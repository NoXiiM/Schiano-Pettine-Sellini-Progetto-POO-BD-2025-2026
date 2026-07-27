package gui;

import controller.gestionale.DipendenteWelcomeController;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Dealer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class AggiungiGiocoDealer {
    private JPanel aggiungiGiocoPanel;
    private JCheckBox pokerCheckBox;
    private JCheckBox blackJackCheckBox;
    private JButton confermaGiochi;
    private JButton tornaIndietroButton;

    DipendenteWelcomeController controller;
    JFrame frameChiamante;

    public AggiungiGiocoDealer(DipendenteWelcomeController controller, JFrame frameChiamante, Dealer dealerSelezionato) {

        ArrayList<Gioco> giochi = dealerSelezionato.getGiochiDealer();

        ArrayList<Gioco> giochiMancanti = new ArrayList<>();
        if (!giochi.contains(Gioco.Poker)) giochiMancanti.add(Gioco.Poker);
        if (!giochi.contains(Gioco.Blackjack)) giochiMancanti.add(Gioco.Blackjack);

        JFrame thisFrame = new JFrame("AggiungiGiocoDealer");
        thisFrame.setContentPane(aggiungiGiocoPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();

        if (giochiMancanti.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Il dealer conosce già tutti i giochi", "Errore", JOptionPane.ERROR_MESSAGE);
            thisFrame.dispose();
            return;
        }

        pokerCheckBox.setVisible(giochiMancanti.contains(Gioco.Poker));
        blackJackCheckBox.setVisible(giochiMancanti.contains(Gioco.Blackjack));

        this.frameChiamante = frameChiamante;
        this.controller = controller;

        frameChiamante.setVisible(false);
        thisFrame.setVisible(true);

        confermaGiochi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(pokerCheckBox.isSelected() || blackJackCheckBox.isSelected())) {
                    JOptionPane.showMessageDialog(null, "Seleziona gioco", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Gioco> giochiSelezionati = new ArrayList<>();
                if (pokerCheckBox.isSelected()) giochiSelezionati.add(Gioco.Poker);
                if (blackJackCheckBox.isSelected()) giochiSelezionati.add(Gioco.Blackjack);

                try {
                    controller.aggiungiGiochi(dealerSelezionato, giochiSelezionati);
                    thisFrame.dispose();
                    frameChiamante.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }
}