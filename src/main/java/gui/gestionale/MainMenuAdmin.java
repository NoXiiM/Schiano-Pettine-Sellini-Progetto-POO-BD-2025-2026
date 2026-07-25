package gui.gestionale;

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
    private JButton cercaButton;
    private JTextField textFieldNome;
    private JTextField textFieldCognome;
    private JTextField textFieldUsername;
    private JSpinner spinnerSaldo;
    private JCheckBox siCheckBoxSospetto;
    private JCheckBox noCheckBoxSospetto;
    private JCheckBox siCheckBoxBan;
    private JCheckBox noCheckBoxBan;
    private JSlider sliderPercentualeVincita;
    private JSpinner spinnerPartiteGiocate;
    private JScrollPane jScrollPaneJlist;
    private JScrollPane jScrollPaneInfoClienti;
    private JRadioButton piuRadioButtonVincita;
    private JRadioButton menoRadioButtonVincita;
    private JRadioButton piuRadioButtonPartite;
    private JRadioButton menoRadioButtonPartite;
    private JLabel nomeText;
    private JLabel cognomeText;
    private JPanel jpanelRicerca;
    private JLabel usernameText;
    private JLabel saldoText;
    private JLabel sospettoText;
    private JLabel banText;
    private JLabel percentualVincitaText;
    private JLabel partiteGiocateText;
    private JSlider sliderPartite;
    private JLabel ricercaText;

    JFrame thisFrame;
    JFrame frameChiamante;
    WelcomeController controller;

    private static DefaultListModel<Cliente> modelloListaClienti;

    public MainMenuAdmin(DipendenteWelcomeController controller, JFrame frameChiamante) {
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
        piuRadioButtonVincita.setActionCommand("piu");
        menoRadioButtonVincita.setActionCommand("meno");
        ButtonGroup selezionaVincita = new ButtonGroup();
        selezionaVincita.add(menoRadioButtonVincita);
        selezionaVincita.add(piuRadioButtonVincita);

        piuRadioButtonPartite.setActionCommand("piu");
        menoRadioButtonPartite.setActionCommand("meno");
        ButtonGroup selezionaPartite = new ButtonGroup();
        selezionaPartite.add(menoRadioButtonPartite);
        selezionaPartite.add(piuRadioButtonPartite);

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

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeRicerca = textFieldNome.getText();
                String cognomeRicerca = textFieldCognome.getText();
                String usernameRicerca = textFieldUsername.getText();

                int saldoRicerca = (int)spinnerSaldo.getValue();

                double percentualeVittoriaRicerca = sliderPercentualeVincita.getValue();
                String piuMenoVittoria = selezionaVincita.getSelection().getActionCommand(); //Se si true, se no false

                int partiteGiocateRicerca = (int) spinnerPartiteGiocate.getValue();
                String piuMenoPartite = selezionaPartite.getSelection().getActionCommand();

                //Definiamo se sia indifferente, si o no, il sospetto
                String sospettoRicerca;
                if(siCheckBoxSospetto.isSelected() && noCheckBoxSospetto.isSelected()) {
                    sospettoRicerca = "indifferente";
                } else if (siCheckBoxSospetto.isSelected()) {
                    sospettoRicerca = "si";
                } else if (noCheckBoxSospetto.isSelected()) {
                    sospettoRicerca = "no";
                } else  {
                    sospettoRicerca = "indifferente";
                }

                //Definiamo se sia indifferente, si o no, il ban
                String banRicerca;
                if(siCheckBoxBan.isSelected() && noCheckBoxBan.isSelected()) {
                    banRicerca = "indifferente";
                } else if (siCheckBoxBan.isSelected()) {
                    banRicerca = "si";
                } else if (noCheckBoxBan.isSelected()) {
                    banRicerca = "no";
                } else  {
                    banRicerca = "indifferente";
                }
                modelloListaClienti.clear();
                modelloListaClienti.addAll(controller.ricercaClienti(nomeRicerca,cognomeRicerca,usernameRicerca,
                        saldoRicerca,percentualeVittoriaRicerca,piuMenoVittoria,partiteGiocateRicerca,piuMenoPartite,sospettoRicerca,banRicerca));
                listaClienti.setModel(modelloListaClienti);

            }
        });
    }

    public void aggiornaUsername(){
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
    }

}
