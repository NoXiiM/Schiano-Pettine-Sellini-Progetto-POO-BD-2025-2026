package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import database.implementazioneDAO.ImpDAOopc;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private ClientWelcomeController controller;

    public TabbedMenuPlayer(ClientWelcomeController controller, JFrame mainframe) {
        this.frameChiamante = mainframe;
        this.controller= controller;

        thisFrame = new JFrame("TabbedMenuPlayer");
        thisFrame.setContentPane(tabbedMenuPlayer);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        frameChiamante.setVisible(false);
        aggiornaUsername();
        aggiornaSaldo();

        controller.pulisciUsernames();

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

        depositaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog(null, "Inserisci un valore:", "Deposita", JOptionPane.QUESTION_MESSAGE);

                if (input != null) {

                    try {
                        int valore_deposito = Integer.parseInt(input);
                        controller.depositaSaldoCliente(valore_deposito);

                        aggiornaSaldo();

                    } catch (NumberFormatException exValDepo) {
                        JOptionPane.showMessageDialog(null, "Inserisci un numero intero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }catch (RuntimeException exNegativo){
                        JOptionPane.showMessageDialog(null, exNegativo.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        prelevaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input = JOptionPane.showInputDialog(null, "Inserisci un valore:", "Preleva", JOptionPane.QUESTION_MESSAGE);

                if(input != null){
                    try {
                        int valore_prelievo = Integer.parseInt(input);
                        if(!controller.prelevaSaldoCliente(valore_prelievo)){
                            JOptionPane.showMessageDialog(null, "Saldo insufficiente !", "Errore", JOptionPane.ERROR_MESSAGE);
                        }

                        aggiornaSaldo();

                    } catch (NumberFormatException ex_val_depo) {
                        JOptionPane.showMessageDialog(null, "Inserisci un numero intero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }catch (RuntimeException exNegativo){
                        JOptionPane.showMessageDialog(null, exNegativo.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        gestisciSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // si sposta al tab "Gestisci saldo"
                for (int i = 0; i < tabbedMenuPlayer.getTabCount(); i++) {
                    if (tabbedMenuPlayer.getTitleAt(i).equals("Gestisci Saldo")) {
                        tabbedMenuPlayer.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        cambiaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePass(thisFrame, controller);
                thisFrame.setVisible(false);
            }
        });

        cambiaUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JLabel> labels = new ArrayList<>();
                labels.add(userFieldGamePanel);
                labels.add(userFieldSaldoPanel);
                new ChangeUsername(thisFrame, controller, labels);
                thisFrame.setVisible(false);
            }
        });

        resettaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ForgotPassword(controller, thisFrame);
            }
        });


        cancellaAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CancellaAccount(controller, thisFrame, frameChiamante);
                thisFrame.setVisible(false);
            }
        });
        //collegamento
        blackJack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.isBanned()) {
                    thisFrame.setVisible(false);
                    new SelezioneTavoloBlackJack(thisFrame, controller, TabbedMenuPlayer.this);
                } else{
                    JOptionPane.showMessageDialog(null, "Sei stato bannato !", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        SlotMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.isBanned()) {
                    thisFrame.setVisible(false);
                    new SelezioneTavoloSlotMachine(thisFrame, controller, TabbedMenuPlayer.this);
                } else{
                    JOptionPane.showMessageDialog(null, "Sei stato bannato !", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pokerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.isBanned())
                {
                    thisFrame.setVisible(false);
                    new SelezioneTavoloPoker(thisFrame, controller, TabbedMenuPlayer.this);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Sei stato bannato !", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    controller.salvaDatiClienteUscitaDaGestione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.dispose();
                System.exit(0);
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

            //salvataggio dati al logout
            try {
                controller.salvaDatiClienteUscitaDaGestione();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
            ;
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
        saldoInGameText.setText("Saldo disponibile: " + controller.getSaldoCliente());
        saldoInSaldoText.setText("Saldo disponibile: " + controller.getSaldoCliente());
    }
}
