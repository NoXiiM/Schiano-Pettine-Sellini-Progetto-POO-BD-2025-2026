package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DealerPanel {
    private JPanel dealer;
    private JTabbedPane dealerPanel;
    private JTextArea textAreaSessioni;
    private JList listaSessioni;
    private JButton cercaButton;
    private JButton logoutButton;
    private JButton aggiornaListaButton;
    private JButton attivaSospettoButton;
    private JPanel gestioneAccountPanel;
    private JButton cambiaPasswordButton;
    private JButton cambiaUsernameButton;
    private JButton resettaPasswordButton;
    private JLabel usernameSessionePanel;
    private JPanel ricercaPanel;
    private JCheckBox controllaPercentualeCheckBox;
    private JLabel percentualVincitaTextMin;
    private JSpinner spinnerPercMin;
    private JLabel percentualVincitaTextMax;
    private JSpinner spinnerPercMax;
    private JCheckBox controllaDurataCheckBox;
    private JLabel durataTotaleTextMin;
    private JSpinner spinnerDurMin;
    private JLabel durataTotaleTextMax;
    private JSpinner spinnerDurMax;
    private JCheckBox controllaPartiteCheckBox;
    private JLabel partiteTextMin;
    private JSpinner spinnerParMin;
    private JSpinner spinnerParMax;
    private JLabel partiteTextMax;
    private JLabel infoSessioneText;
    private JLabel sessioneText;
    private JTextField textFieldUsername;
    private JLabel usernameText;
    private JCheckBox controllaUsernameCheckBox;

    private DipendenteWelcomeController controller;


    public DealerPanel(DipendenteWelcomeController controller, JFrame frameChiamante)
    {
        JFrame thisFrame = new JFrame("dealerPanel");
        thisFrame.setContentPane(dealer);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        frameChiamante.setVisible(false);

        this.controller = controller;

        usernameSessionePanel.setText(controller.getUserUtente());

        textAreaSessioni.setFocusable(false);
        textAreaSessioni.setEditable(false);

        percentualVincitaTextMin.setVisible(false);
        percentualVincitaTextMax.setVisible(false);
        spinnerPercMin.setVisible(false);
        spinnerPercMax.setVisible(false);

        durataTotaleTextMin.setVisible(false);
        durataTotaleTextMax.setVisible(false);
        spinnerDurMin.setVisible(false);
        spinnerDurMax.setVisible(false);

        partiteTextMin.setVisible(false);
        partiteTextMax.setVisible(false);
        spinnerParMin.setVisible(false);
        spinnerParMax.setVisible(false);

        usernameText.setVisible(false);
        textFieldUsername.setVisible(false);

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

                    frameChiamante.setVisible(true);
                    thisFrame.dispose();
                }
            }
        });
        controllaPercentualeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controllaPercentualeCheckBox.isSelected()){
                    percentualVincitaTextMin.setVisible(true);
                    percentualVincitaTextMax.setVisible(true);
                    spinnerPercMin.setVisible(true);
                    spinnerPercMax.setVisible(true);

                } else {
                    percentualVincitaTextMin.setVisible(false);
                    percentualVincitaTextMax.setVisible(false);
                    spinnerPercMin.setVisible(false);
                    spinnerPercMax.setVisible(false);
                }
            }
        });
        controllaDurataCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controllaDurataCheckBox.isSelected()){
                    durataTotaleTextMin.setVisible(true);
                    durataTotaleTextMax.setVisible(true);
                    spinnerDurMin.setVisible(true);
                    spinnerDurMax.setVisible(true);

                } else {
                    durataTotaleTextMin.setVisible(false);
                    durataTotaleTextMax.setVisible(false);
                    spinnerDurMin.setVisible(false);
                    spinnerDurMax.setVisible(false);
                }
            }
        });
        controllaPartiteCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controllaPartiteCheckBox.isSelected()){
                    partiteTextMin.setVisible(true);
                    partiteTextMax.setVisible(true);
                    spinnerParMin.setVisible(true);
                    spinnerParMax.setVisible(true);

                } else {
                    partiteTextMin.setVisible(false);
                    partiteTextMax.setVisible(false);
                    spinnerParMin.setVisible(false);
                    spinnerParMax.setVisible(false);
                }
            }
        });
        controllaUsernameCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controllaUsernameCheckBox.isSelected()){
                    usernameText.setVisible(true);
                    textFieldUsername.setVisible(true);
                }else{
                    usernameText.setVisible(false);
                    textFieldUsername.setVisible(false);
                }
            }
        });
        cambiaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePass(thisFrame, controller);
            }
        });
        cambiaUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JLabel> labels = new ArrayList<>();
                labels.add(usernameSessionePanel);
                new ChangeUsername(thisFrame, controller, labels);
            }
        });
        resettaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ForgotPassword(controller, thisFrame);
            }
        });
    }
}
