package gui.gestionale;

import controller.DipendenteCorrente;
import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;
import model.gestionale.utenteEFigli.Cliente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuAdmin {
    private JPanel AdminPanel;
    private JTabbedPane tabbedPane1;
    private JList listaClienti;
    private JButton bannaButton;
    private JTextArea infoField;
    private JButton logoutButton;
    private JLabel userFieldAdmin;

    JFrame thisFrame;
    JFrame frameChiamante;
    WelcomeController controller;

    private static DefaultListModel<Cliente> modelloListaClienti;

    public MainMenuAdmin(DipendenteWelcomeController controller, JFrame frameChiamante, DipendenteCorrente dipendenteCorrente) {
        this.controller = controller;
        this.frameChiamante= frameChiamante;

        modelloListaClienti= new DefaultListModel<>();
        modelloListaClienti.addAll(controller.getLista_clienti());
        listaClienti.setModel(modelloListaClienti);

        thisFrame = new JFrame("MainMenuAdmin");
        thisFrame.setContentPane(AdminPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        frameChiamante.setVisible(false);


        bannaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Cliente temp= (Cliente) listaClienti.getSelectedValue();
                String input = JOptionPane.showInputDialog(null, "Inserisci motivo ban:", "Ban utente", JOptionPane.QUESTION_MESSAGE);

                if (input != null && !input.isBlank()) {
                    temp.creaBan(input);
                }
            }
        });


        listaClienti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Cliente temp= (Cliente) listaClienti.getSelectedValue();
                if(temp != null){
                    infoField.setText("User: " + temp.getUsername() + "\nSaldo: " + temp.getSaldo() + "\nTasso vincita: " +
                            temp.getVincitaPercentualeTot() + "\nSaldo giocato: " + temp.getFichesGiocate() + "\nTempo di gioco totale: " + temp.getTempoDiGioco() +
                            "\nBan: " + (temp.getMotivoBan() != null ? temp.getMotivoBan() : "Nessuno")
                    );
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int risposta = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler tornare alla schermata di login ?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION
                );

                if (risposta == JOptionPane.YES_OPTION) {
                    thisFrame.setVisible(false);
                    frameChiamante.setVisible(true);
                    thisFrame.dispose();
                }

            }
        });

    }

    public void aggiornaUsername(){
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
    }

}
