package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ChangeUsername {
    private JPanel changeUserPanel;
    private JPasswordField passwordField;
    private JPasswordField passwordField2;
    private JButton confermaCambioUsernameButton;
    private JButton backButton;
    private JTextField newUserField;

    JFrame frameChiamante;
    WelcomeController controller;

    public ChangeUsername(JFrame frameChiamante, ClientWelcomeController controller, TabbedMenuPlayer parentMenu) {
        this.frameChiamante= frameChiamante;
        this.controller= controller;

        JFrame frameChiamato = new JFrame("ChangeUsername");
        frameChiamato.setContentPane(changeUserPanel);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        confermaCambioUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newUser = newUserField.getText();
                String pass1 = new String(passwordField.getPassword());   //getPassword restituisce char[]
                String pass2= new String(passwordField2.getPassword());   //getPassword restituisce char[]

                try {
                    if(controller.changeUsername(newUser, pass1, pass2)){
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");

                        parentMenu.aggiornaUsername();
                        frameChiamato.setVisible(false);
                        frameChiamante.setVisible(true);
                        frameChiamato.dispose();

                    } else{
                        JOptionPane.showMessageDialog(null, "Password errata ! Se non la ricordi prova a resettarla");
                    }


                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }catch (SQLException e1)
                {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frameChiamato.setVisible(false);
                frameChiamante.setVisible(true);
                frameChiamato.dispose();

            }
        });
    }
}
