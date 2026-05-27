package gui.gestionale;

import controller.ClienteCorrente;
import controller.WelcomeController;
import model.gestionale.utenteEFigli.Cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeUsername {
    private JPanel changeUserPanel;
    private JPasswordField passwordField;
    private JPasswordField passwordField2;
    private JButton confermaCambioUsernameButton;
    private JButton backButton;
    private JTextField newUserField;

    JFrame frameChiamante;
    WelcomeController controller;
    ClienteCorrente currentClient;

    public ChangeUsername(ClienteCorrente currentClient, JFrame frameChiamante, WelcomeController controller, TabbedMenuPlayer parentMenu) {
        this.currentClient = currentClient;
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
                    if(controller.changeUsername(currentClient.getClienteCorrente(), newUser, pass1, pass2)){
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");

                        parentMenu.aggiornaUsername();
                        frameChiamato.setVisible(false);
                        frameChiamante.setVisible(true);
                        frameChiamato.dispose();

                    } else{
                        JOptionPane.showMessageDialog(null, "Password errata ! Se non la ricordi prova a resettarla");
                    }


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
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
