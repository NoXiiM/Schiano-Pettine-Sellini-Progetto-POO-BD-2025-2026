package gui.gestionale;

import controller.WelcomeController;

import javax.swing.*;

import gui.giochi.GUIBlackJack;
import model.gestionale.utenteEFigli.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabbedMenuPlayer {
    private JTabbedPane tabbedMenuPlayer;
    private JPanel mainMultiTabbedPanel;
    private JPanel gamePanelPlayer;
    private JButton pokerButton;
    private JButton gestisciSaldoButton;
    private JButton logoutButtonInGamePanel;
    private JPanel saldoInSaldoPanel;
    private JLabel saldoInSaldoText;
    private JLabel saldoInGameText;
    private JButton depositaButton;
    private JButton prelevaButton;
    private JButton logoutButtonInSaldoPanel;
    private JLabel userFieldGamePanel;
    private JLabel userFieldSaldoPanel;

    private JFrame frameChiamante;
    private WelcomeController controller;
    private Cliente currentClient;

    public TabbedMenuPlayer(WelcomeController controller, JFrame mainframe, Cliente client) {
        this.frameChiamante = mainframe;
        this.currentClient= client;
        this.controller= controller;

        JFrame frameChiamato = new JFrame("TabbedMenuPlayer");
        frameChiamato.setContentPane(tabbedMenuPlayer);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        frameChiamante.setVisible(false);

        userFieldGamePanel.setText(currentClient.getUsername() + "\t");
        userFieldSaldoPanel.setText(currentClient.getUsername() + "\t");

        saldoInGameText.setText("Saldo disponibile: " + currentClient.getSaldo());
        saldoInSaldoText.setText("Saldo disponibile: " + currentClient.getSaldo());

        logoutButtonInSaldoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int risposta = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler tornare alla schermata di login ?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION
                );

                if (risposta == JOptionPane.YES_OPTION) {
                    frameChiamato.setVisible(false);
                    frameChiamante.setVisible(true);
                    frameChiamato.dispose();
                }

                frameChiamato.setVisible(false);
                frameChiamante.setVisible(true);
                frameChiamato.dispose();

            }
        });


        logoutButtonInGamePanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int risposta = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler tornare alla schermata di login ?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION
                );

                if (risposta == JOptionPane.YES_OPTION) {
                    frameChiamato.setVisible(false);
                    frameChiamante.setVisible(true);
                    frameChiamato.dispose();
                }

            }
        });


//        pokerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                GUIBlackJack blackJackFrame= new GUIBlackJack(frameChiamato, currentClient);
//            }
//        });
    }

}
