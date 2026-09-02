package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ForgotPassword {
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField usernameTextField;
    private JButton resetPassButton;
    private JButton tornaAlLoginButton;
    private JPanel ForgotPassPanel;

    private JFrame frameChiamante;
    private WelcomeController controller;

    public ForgotPassword(WelcomeController welcomeController, JFrame mainframe) {

        this.controller= welcomeController;
        this.frameChiamante= mainframe;

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
