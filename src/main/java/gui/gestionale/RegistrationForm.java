package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Form per la registrazione di un nuovo cliente.
 */
public class RegistrationForm {
    private JPanel registrationPanel;
    private JTextField usernameRegField;
    private JTextField nameRegField;
    private JTextField surnameRegField;
    private JTextField codFisRegField;
    private JPasswordField passRegField;
    private JButton registratiButton;
    private JButton tornaAlLoginRegButton;
    private JComboBox<Integer> comboBoxDay;
    private JComboBox<String> comboBoxMonth;
    private JComboBox<Integer> comboBoxYear;
    private JTextField depositoObblField;
    private JLabel depositoObbligatorio;

    private final ClientWelcomeController controller;

    /**
     * Costruttore di RegistrationForm, richiede che vengano inseriti i dati per la registrazione di un CLIENTE e un deposito
     * minimo obbligatorio.
     *
     * @param controller controller contenente le info necessarie per la gestione del cliente: {@link ClientWelcomeController}
     * @param frameChiamante  interfaccia di login al quale tornare alla fine della registrazione
     */
    public RegistrationForm(ClientWelcomeController controller, JFrame frameChiamante) {
        this.controller= controller;

        JFrame thisFrame = new JFrame("RegistrationForm");
        thisFrame.setContentPane(registrationPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        //Setting icon
        ImageIcon iconaFrame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icone/LogoCasinò.png")));
        thisFrame.setIconImage(iconaFrame.getImage().getScaledInstance(1783, 1113, Image.SCALE_SMOOTH));

        frameChiamante.setVisible(false);

        inizializzaComboboxData();
        controller.aggiornaUsernames();

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int giorno = comboBoxDay.getSelectedItem() == null ? -1 : (int) comboBoxDay.getSelectedItem();
                    int mese = comboBoxMonth.getSelectedIndex() + 1; // getSelectedIndex parte da 0, la funzione LocalDate.of parte da 1 per i mesi
                    int anno = comboBoxYear.getSelectedItem() == null ? -1 : (int) comboBoxYear.getSelectedItem();

                    LocalDate dataNascita = LocalDate.of(anno, mese, giorno);
                    String password = new String(passRegField.getPassword());   //getPassword restituisce char[]

                    int deposito = Integer.parseInt(depositoObblField.getText());

                    controller.registrazioneCliente(usernameRegField.getText(), nameRegField.getText(), surnameRegField.getText(), codFisRegField.getText(), dataNascita, password, deposito);
                    JOptionPane.showMessageDialog(null, "Registrazione completata con successo");

                    thisFrame.setVisible(false);
                    frameChiamante.setVisible(true);
                    thisFrame.dispose();

                } catch (DateTimeException ex) {        //combinazioni come 31 Febbraio sono selezionabili ma non esistono, LocalDate.of le rifiuta
                    JOptionPane.showMessageDialog(null, "Data non valida.", "Errore", JOptionPane.ERROR_MESSAGE);

                } catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Inserisci un deposito valido", "Errore", JOptionPane.ERROR_MESSAGE);

                } catch (RuntimeException e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
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
                    thisFrame.setVisible(false);
                    frameChiamante.setVisible(true);
                    thisFrame.dispose();
                }
            }
        });

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
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
