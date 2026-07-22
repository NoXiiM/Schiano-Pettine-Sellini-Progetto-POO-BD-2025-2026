package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPassword {
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField usernameTextField;
    private JButton resetPassButton;
    private JButton tornaAlLoginButton;
    private JPanel ForgotPassPanel;

    private JFrame frameChiamante;
    private ClientWelcomeController controller;

    public ForgotPassword(ClientWelcomeController welcomeController, JFrame mainframe) {

        this.controller= welcomeController;
        this.frameChiamante= mainframe;

        JFrame frameChiamato = new JFrame("TabbedMenuPlayer");
        frameChiamato.setContentPane(ForgotPassPanel);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        frameChiamante.setVisible(false);

        resetPassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    controller.resetPass(nameTextField.getText(), surnameTextField.getText(), usernameTextField.getText());

                    nameTextField.setText("");
                    surnameTextField.setText("");
                    usernameTextField.setText("");

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tornaAlLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frameChiamato.setVisible(false);
                frameChiamante.setVisible(true);
                frameChiamato.dispose();

            }
        });
    }
}
