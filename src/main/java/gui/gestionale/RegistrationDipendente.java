package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Dealer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RegistrationDipendente {
    private JPanel registrationPanel;
    private JTextField usernameRegField;
    private JTextField nameRegField;
    private JLabel ruolo;
    private JTextField surnameRegField;
    private JTextField codFisRegField;
    private JButton registratiButton;
    private JButton tornaIndietroButton;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxYear;
    private JComboBox ruoloComboBox;
    private JCheckBox blackJackCheckBox;
    private JCheckBox pokerCheckBox;
    private JLabel giochiLabel;

    private JFrame frameChiamante;
    private DipendenteWelcomeController controller;

    public RegistrationDipendente(JFrame frameChiamante, DipendenteWelcomeController controller) {

        this.frameChiamante= frameChiamante;
        this.controller= controller;

        JFrame frameChiamato = new JFrame("RegistrationDipendente");
        frameChiamato.setContentPane(registrationPanel);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        controller.aggiornaUsernames();
        inizializzaComboboxData();

        giochiLabel.setVisible(false);
        pokerCheckBox.setVisible(false);
        blackJackCheckBox.setVisible(false);

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int giorno = (int) comboBoxDay.getSelectedItem();
                    int mese = comboBoxMonth.getSelectedIndex() + 1; // getSelectedIndex parte da 0, la funzione LocalDate.of parte da 1 per i mesi
                    int anno = (int) comboBoxYear.getSelectedItem();

                    LocalDate dataNascita = LocalDate.of(anno, mese, giorno);
                    String password = new String("P@ssw0rd!");

                    String ruolo= (String) ruoloComboBox.getSelectedItem();

                    ArrayList<Gioco> giochi = null;

                    if(ruolo.equals("Dealer"))
                    {
                        giochi = new ArrayList<>();
                        if(pokerCheckBox.isSelected()) giochi.add(Gioco.Poker);
                        if(blackJackCheckBox.isSelected()) giochi.add(Gioco.Blackjack);
                    }

                    try {
                        controller.registraDipendente(usernameRegField.getText(), nameRegField.getText(),
                                surnameRegField.getText(), codFisRegField.getText(), dataNascita, password, ruolo, giochi);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(null, "Registrazione completata con successo");

                    //Aggiornamento Lista
                    MainMenuAdmin.getModelloListaDipendente().clear();
                    MainMenuAdmin.getModelloListaDipendente().addAll(controller.getDipendentiInLocale());

                    frameChiamato.setVisible(false);
                    frameChiamante.setVisible(true);
                    frameChiamato.dispose();

                } catch (DateTimeException ex) {        //combinazioni come 31 Febbraio sono selezionabili ma non esistono, LocalDate.of le rifiuta
                    JOptionPane.showMessageDialog(null, "Data non valida.", "Errore", JOptionPane.ERROR_MESSAGE);

                } catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Inserisci un deposito valido", "Errore", JOptionPane.ERROR_MESSAGE);

                } catch (RuntimeException e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamato.dispose();
                controller.pulisciUsernames();
            }
        });

        ruoloComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if((ruoloComboBox.getSelectedItem()).equals("Dealer")){
                    giochiLabel.setVisible(true);
                    pokerCheckBox.setVisible(true);
                    blackJackCheckBox.setVisible(true);

                } else{
                    giochiLabel.setVisible(false);
                    pokerCheckBox.setVisible(false);
                    blackJackCheckBox.setVisible(false);
                }
            }
        });
    }

    private void inizializzaComboboxData() {

        // Giorni
        for (int i = 1; i <= 31; i++) {
            comboBoxDay.addItem(i);
        }

        // Mesi
        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
        for (String mese : mesi) {
            comboBoxMonth.addItem(mese);
        }

        // Anni
        for (int i = 2026; i >= 1920; i--) {
            comboBoxYear.addItem(i);
        }
    }
}
