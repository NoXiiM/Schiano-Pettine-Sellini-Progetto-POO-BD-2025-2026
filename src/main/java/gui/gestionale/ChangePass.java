package gui.gestionale;

import controller.gestionale.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePass {
    private JPanel changePassPanel;
    private JPasswordField passwordNuovaField;
    private JPasswordField passwordVecchiaField;
    private JPasswordField passwordNuovaField2;
    private JButton confermaCambioPasswordButton;
    private JButton backButton;

    JFrame frameChiamante;
    WelcomeController controller;

    public ChangePass(JFrame frameChiamante, WelcomeController controller) {
        this.frameChiamante= frameChiamante;
        this.controller= controller;

        JFrame thisFrame = new JFrame("ChangePass");
        thisFrame.setContentPane(changePassPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        confermaCambioPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String oldPass = new String(passwordVecchiaField.getPassword());   //getPassword restituisce char[]
                String newPass1 = new String(passwordNuovaField.getPassword());   //getPassword restituisce char[]
                String newPass2 = new String(passwordNuovaField2.getPassword());   //getPassword restituisce char[]


                try {
                    if(controller.changePass(oldPass, newPass1, newPass2)){
                        JOptionPane.showMessageDialog(null, "Password modificata con successo !");

                        thisFrame.setVisible(false);
                        frameChiamante.setVisible(true);
                        thisFrame.dispose();

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
