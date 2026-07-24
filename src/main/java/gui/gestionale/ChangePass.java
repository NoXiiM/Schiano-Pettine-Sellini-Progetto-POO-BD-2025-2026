package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePass {
    private JPanel changePassPanel;
    private JPasswordField passwordNuovaField;
    private JPasswordField passwordVecchiaField;
    private JPasswordField passwordNuovaField2;
    private JButton confermaCambioPasswordButton;
    private JButton backButton;

    JFrame frameChiamante;
    ClientWelcomeController controller;

    public ChangePass(JFrame frameChiamante, ClientWelcomeController controller) {
        this.frameChiamante= frameChiamante;
        this.controller= controller;

        JFrame frameChiamato = new JFrame("ChangePass");
        frameChiamato.setContentPane(changePassPanel);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        confermaCambioPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String oldPass = new String(passwordVecchiaField.getPassword());   //getPassword restituisce char[]
                String newPass1 = new String(passwordNuovaField.getPassword());   //getPassword restituisce char[]
                String newPass2 = new String(passwordNuovaField2.getPassword());   //getPassword restituisce char[]


                try {
                    if(controller.changePass(oldPass, newPass1, newPass2)){
                        JOptionPane.showMessageDialog(null, "Password modificata con successo !");

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
