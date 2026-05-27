package gui.gestionale;

import controller.ClienteCorrente;
import controller.WelcomeController;

import javax.swing.*;

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
    private JPanel saldoInGamePanel;
    private JLabel saldoInSaldoText;
    private JLabel saldoInGameText;
    private JButton depositaButton;
    private JButton prelevaButton;
    private JButton logoutButtonInSaldoPanel;
    private JLabel userFieldGamePanel;
    private JLabel userFieldSaldoPanel;
    private JPanel accountIn;
    private JButton cambiaPasswordButton;
    private JButton cambiaUsernameButton;
    private JButton cancellaAccountButton;
    private JButton resettaPasswordButton;
    private JButton blackJack;
    private JButton SlotMachine;
    private JFrame thisFrame;

    private JFrame frameChiamante;
    private WelcomeController controller;
    private ClienteCorrente currentClient;

    public TabbedMenuPlayer(WelcomeController controller, JFrame mainframe, ClienteCorrente currentClient) {
        this.frameChiamante = mainframe;
        this.currentClient= currentClient;
        this.controller= controller;

        thisFrame = new JFrame("TabbedMenuPlayer");
        thisFrame.setContentPane(tabbedMenuPlayer);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        frameChiamante.setVisible(false);

        aggiornaUsername();
        aggiornaSaldo();

        logoutButtonInSaldoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout(thisFrame);
            }
        });

        logoutButtonInGamePanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout(thisFrame);
            }
        });


//        pokerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                GUIBlackJack blackJackFrame= new GUIBlackJack(thisFrame, currentClient);
//            }
//        });


        depositaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog(null, "Inserisci un valore:", "Deposita", JOptionPane.QUESTION_MESSAGE);

                if (input != null) {

                    try {
                        int valore_deposito = Integer.parseInt(input);
                        controller.depositaSaldoCliente(valore_deposito, currentClient.getClienteCorrente());

                        saldoInGameText.setText("Saldo disponibile: " + currentClient.getSaldoCorrente());
                        saldoInSaldoText.setText("Saldo disponibile: " + currentClient.getSaldoCorrente());

                    } catch (NumberFormatException ex_val_depo) {
                        JOptionPane.showMessageDialog(null, "Inserisci un numero intero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        prelevaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog(null, "Inserisci un valore:", "Preleva", JOptionPane.QUESTION_MESSAGE);

                if (input != null) {

                    try {
                        int valore_prelievo = Integer.parseInt(input);
                        if(!controller.prelevaSaldoCliente(valore_prelievo, currentClient.getClienteCorrente())){
                            JOptionPane.showMessageDialog(null, "Saldo insufficiente !", "Errore", JOptionPane.ERROR_MESSAGE);
                        }

                        saldoInGameText.setText("Saldo disponibile: " + currentClient.getSaldoCorrente());
                        saldoInSaldoText.setText("Saldo disponibile: " + currentClient.getSaldoCorrente());

                    } catch (NumberFormatException ex_val_depo) {
                        JOptionPane.showMessageDialog(null, "Inserisci un numero intero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        gestisciSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // si sposta al tab "Gestisci saldo"
                for (int i = 0; i < tabbedMenuPlayer.getTabCount(); i++) {
                    if (tabbedMenuPlayer.getTitleAt(i).equals("Gestisci saldo")) {
                        tabbedMenuPlayer.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        cambiaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ChangePass changea_pass_frame= new ChangePass(thisFrame, controller);

            }
        });

        cambiaUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ChangeUsername change_user_frame = new ChangeUsername(thisFrame, controller, TabbedMenuPlayer.this);
            }
        });

        resettaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ForgotPassword forgotPasswordPanel= new ForgotPassword(controller, thisFrame);

            }
        });


        cancellaAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CancellaAccount del_account= new CancellaAccount(controller, thisFrame, frameChiamante);

            }
        });
        //collegamento
        blackJack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.setVisible(false);
                new SelezioneTavoloBlackJack(thisFrame, currentClient, TabbedMenuPlayer.this);
            }
        });

        SlotMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.setVisible(false);
                new SelezioneTavoloSlotMachine(thisFrame, currentClient);
            }
        });
    }

    private void logout(JFrame frameChiamato){
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

    public void aggiornaUsername(){
        userFieldGamePanel.setText(controller.getUserUtente() + "\t");
        userFieldSaldoPanel.setText(controller.getUserUtente() + "\t");
    }

    public void aggiornaSaldo()
    {
        saldoInGameText.setText("Saldo disponibile: " + controller.getSaldoUtente());
        saldoInSaldoText.setText("Saldo disponibile: " + controller.getSaldoUtente());
    }
}
