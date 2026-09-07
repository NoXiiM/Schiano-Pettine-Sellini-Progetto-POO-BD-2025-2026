package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Interfaccia per resettare la password, valida sia per {@link model.gestionale.utenteEFigli.Dipendente} che per {@link model.gestionale.utenteEFigli.Cliente}.
 */
public class ForgotPassword {
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField usernameTextField;
    private JButton resetPassButton;
    private JButton tornaAlLoginButton;
    private JPanel ForgotPassPanel;

    private JFrame frameChiamante;
    private WelcomeController controller;

    /**
     * Costruttore di ForgotPassword, richiede che vengano inseriti nome, cognome ed username dell'utente di cui resettare la password.
     *
     * @param welcomeController controller contenente le info generali per la gestione di {@link model.gestionale.utenteEFigli.Utente}: {@link DipendenteWelcomeController}
     * @param frameChiamante    frame chiamante, reso nuovamente visibile alla fine del reset della password
     */
    public ForgotPassword(WelcomeController welcomeController, JFrame frameChiamante) {

        this.controller= welcomeController;
        this.frameChiamante= frameChiamante;

        JFrame thisFrame = new JFrame("TabbedMenuPlayer");
        thisFrame.setContentPane(ForgotPassPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        frameChiamante.setVisible(false);

        resetPassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    controller.resetPass(nameTextField.getText(), surnameTextField.getText(), usernameTextField.getText());

                    nameTextField.setText("");
                    surnameTextField.setText("");
                    usernameTextField.setText("");

                    JOptionPane.showMessageDialog(null, "Password resettata a P@ssw0rd!");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(null, ex1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tornaAlLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                thisFrame.setVisible(false);
                frameChiamante.setVisible(true);
                thisFrame.dispose();

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
}
