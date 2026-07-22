package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancellaAccount {
    private JPanel delUserPanel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JTextField confirmField;
    private JButton cancellaAccountButton;
    private JButton backButton;

    ClientWelcomeController controller;
    JFrame frameChiamante;
    JFrame frameLogin;

    public CancellaAccount(ClientWelcomeController controller, JFrame frameChiamante, JFrame loginFrame) {

        this.controller= controller;
        this.frameLogin= loginFrame;
        this.frameChiamante= frameChiamante;

        JFrame thisFrame = new JFrame("CancellaAccount");
        thisFrame.setContentPane(delUserPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);


        cancellaAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String user= userField.getText();
                    String pass= new String(passwordField.getPassword());
                    String conferma= confirmField.getText();

                    if(controller.deleteUser(user, pass, conferma)){
                        JOptionPane.showMessageDialog(null, "Account eliminato !");

                        thisFrame.setVisible(false);
                        thisFrame.dispose();
                        frameChiamante.setVisible(false);
                        frameChiamante.dispose();
                        frameLogin.setVisible(true);

                    } else{
                        JOptionPane.showMessageDialog(null, "I campi non corrispondono !", "Errore", JOptionPane.ERROR_MESSAGE);

                    }

                    userField.setText("");
                    passwordField.setText("");
                    confirmField.setText("");

                } catch(RuntimeException e1){
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                thisFrame.setVisible(false);
                frameChiamante.setVisible(true);
                thisFrame.dispose();

            }
        });
    }
}

