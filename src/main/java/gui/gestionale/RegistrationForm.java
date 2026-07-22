package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;

public class RegistrationForm {
    private JPanel registrationPanel;
    private JTextField usernameRegField;
    private JTextField nameRegField;
    private JTextField surnameRegField;
    private JTextField codFisRegField;
    private JPasswordField passRegField;
    private JButton registratiButton;
    private JButton tornaAlLoginRegButton;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxYear;

    private JFrame frameChiamante;
    private ClientWelcomeController controller;

    public RegistrationForm(ClientWelcomeController controller, JFrame mainframe) {
        this.controller= controller;
        this.frameChiamante= mainframe;

        JFrame frameChiamato = new JFrame("RegistrationForm");
        frameChiamato.setContentPane(registrationPanel);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        frameChiamante.setVisible(false);

        inizializzaComboboxData();

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int giorno = (int) comboBoxDay.getSelectedItem();
                    int mese = comboBoxMonth.getSelectedIndex() + 1; // getSelectedIndex parte da 0, la funzione LocalDate.of parte da 1 per i mesi
                    int anno = (int) comboBoxYear.getSelectedItem();

                    LocalDate dataNascita = LocalDate.of(anno, mese, giorno);
                    String password = new String(passRegField.getPassword());   //getPassword restituisce char[]

                    controller.registrati(usernameRegField.getText(), nameRegField.getText(), surnameRegField.getText(), codFisRegField.getText(), dataNascita, password);
                    JOptionPane.showMessageDialog(null, "Registrazione completata con successo");

                    frameChiamato.setVisible(false);
                    frameChiamante.setVisible(true);
                    frameChiamato.dispose();

                } catch (DateTimeException ex) {        //combinazioni come 31 Febbraio sono selezionabili ma non esistono, LocalDate.of le rifiuta
                    JOptionPane.showMessageDialog(null, "Data non valida.", "Errore", JOptionPane.ERROR_MESSAGE);

                } catch (RuntimeException e1){
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tornaAlLoginRegButton.addActionListener(new ActionListener() {
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
    }

    private void inizializzaComboboxData(){

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
