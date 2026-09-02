package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import database.implementazioneDAO.ImpDAOopc;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Sessione;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dipendente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private JLabel tavoloAssociatoLabel;

    private DipendenteWelcomeController controller;

    private static DefaultListModel<Sessione> modelloListaSessioni;

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

        spinnerPercMin.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        spinnerPercMax.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        spinnerDurMin.setModel(new SpinnerNumberModel(0, 0, null, 1));
        spinnerDurMax.setModel(new SpinnerNumberModel(0, 0, null, 1));
        spinnerParMin.setModel(new SpinnerNumberModel(0, 0, null, 1));
        spinnerParMax.setModel(new SpinnerNumberModel(0, 0, null, 1));

        modelloListaSessioni = new DefaultListModel<>();

        int[] idTavoloAssociato = new int[1];
        idTavoloAssociato[0] = -1;

        HashMap<String,Boolean> userSuspect = new HashMap<>();
        HashMap<Integer,String>  userSessione = new HashMap<>();

        try {
            modelloListaSessioni.addAll(controller.visualizzaSessioniTavolo(idTavoloAssociato,userSuspect,userSessione));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaSessioni.setModel(modelloListaSessioni);
        tavoloAssociatoLabel.setText("tavolo a cui lavori: " + (idTavoloAssociato[0] == -1 ? "nessuno" : idTavoloAssociato[0]));

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
                thisFrame.setVisible(false);
            }
        });
        cambiaUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JLabel> labels = new ArrayList<>();
                labels.add(usernameSessionePanel);
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
        aggiornaListaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaSessioni.clear();
                try {
                    modelloListaSessioni.addAll(controller.visualizzaSessioniTavolo(null,userSuspect,userSessione));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        listaSessioni.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Sessione temp= (Sessione) listaSessioni.getSelectedValue();
                if(temp != null){
                    textAreaSessioni.setText("Sessione: "+temp.getIdSessione()+
                                            "\nUtente: "+userSessione.get(temp.getIdSessione())+
                                            "\nSospetto: "+(userSuspect.get(userSessione.get(temp.getIdSessione())) ? "Si" : "No")+
                                            "\nDurata sessione: "+temp.getDurataSessione().getSeconds()+ " secondi"+
                                            "\nPartite svolte: "+temp.getPartiteSvolte()+
                                            "\nPercentuale vittoria: "+temp.getVincitaPercentuale());
                }
                else
                {
                    textAreaSessioni.setText(null);
                }
            }
        });
        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String usernameRicerca = textFieldUsername.getText();
                boolean controllaUsername = controllaUsernameCheckBox.isSelected();

                int percMin= (int) spinnerPercMin.getValue();
                int percMax = (int) spinnerPercMax.getValue();
                boolean controllaPercentuale = controllaPercentualeCheckBox.isSelected();

                int durMin= (int) spinnerDurMin.getValue();
                int durMax= (int) spinnerDurMax.getValue();
                boolean controllaDurata = controllaDurataCheckBox.isSelected();

                int partMin = (int) spinnerParMin.getValue();
                int partMax = (int) spinnerParMax.getValue();
                boolean controllaPartite = controllaPartiteCheckBox.isSelected();

                modelloListaSessioni.clear();
                modelloListaSessioni.addAll(controller.ricercaSessioni(usernameRicerca, controllaUsername, percMin, percMax,
                                                                        controllaPercentuale, durMin, durMax,
                                                                        controllaDurata,partMin,partMax,controllaPartite));
            }
        });
        attivaSospettoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sessione temp = (Sessione) listaSessioni.getSelectedValue();

                if(temp != null) {
                    if(!userSuspect.get(userSessione.get(temp.getIdSessione()))) {
                        int input = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler flaggare questo Utente come sospetto?");
                        if (input == JOptionPane.YES_OPTION) {



                            try {
                                controller.updateSospetto(userSessione.get(temp.getIdSessione()));
                                JOptionPane.showMessageDialog(null, "sospetto aggiornato con successo");
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "errore nell'impostare il sospetto",
                                        "errore", JOptionPane.ERROR_MESSAGE);
                            }

                            modelloListaSessioni.clear();
                            try {
                                modelloListaSessioni.addAll(controller.visualizzaSessioniTavolo(null,userSuspect,userSessione));
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                                        JOptionPane.ERROR_MESSAGE);
                            }


                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Utente già sospetto", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Sessione non selezionata", "Errore", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
