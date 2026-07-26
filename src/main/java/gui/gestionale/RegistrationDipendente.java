package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.DipendenteWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;

public class RegistrationDipendente {
    private JPanel registrationPanel;
    private JTextField usernameRegField;
    private JTextField nameRegField;
    private JTextField surnameRegField;
    private JTextField codFisRegField;
    private JButton registratiButton;
    private JButton tornaIndietroButton;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxYear;
    private JLabel ruolo;
    private JTextField ruoloFIeld;

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

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int giorno = (int) comboBoxDay.getSelectedItem();
                    int mese = comboBoxMonth.getSelectedIndex() + 1; // getSelectedIndex parte da 0, la funzione LocalDate.of parte da 1 per i mesi
                    int anno = (int) comboBoxYear.getSelectedItem();

                    LocalDate dataNascita = LocalDate.of(anno, mese, giorno);
                    String password = new String("P@ssw0rd!");

                    controller.registrazione(usernameRegField.getText(), nameRegField.getText(), surnameRegField.getText(), codFisRegField.getText(), dataNascita, password, 0, "dipendente");
                    JOptionPane.showMessageDialog(null, "Registrazione completata con successo");

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
    }
}
