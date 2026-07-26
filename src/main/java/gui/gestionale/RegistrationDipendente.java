package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.DipendenteWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

            }
        });
    }
}
