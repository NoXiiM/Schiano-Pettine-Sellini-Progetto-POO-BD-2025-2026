package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dipendente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChangeUsername {
    private JPanel changeUserPanel;
    private JPasswordField passwordField;
    private JPasswordField passwordField2;
    private JButton confermaCambioUsernameButton;
    private JButton backButton;
    private JTextField newUserField;

    JFrame frameChiamante;
    WelcomeController controller;

    public ChangeUsername(JFrame frameChiamante, WelcomeController controller, ArrayList<JLabel> labels) {
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

                    if(controller.getCurrentUser() instanceof Cliente && ((ClientWelcomeController)controller).changeUsername(newUser, pass1, pass2)){
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");

                        frameChiamato.setVisible(false);
                        frameChiamante.setVisible(true);
                        for(JLabel l : labels){
                            l.setText(controller.getUserUtente());
                        }
                        frameChiamato.dispose();

                    } else if (controller.getCurrentUser() instanceof Dipendente && ((DipendenteWelcomeController)controller).changeUsername(newUser, pass1, pass2)) {
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");


                        frameChiamato.setVisible(false);
                        frameChiamante.setVisible(true);
                        for(JLabel l : labels){
                            l.setText(controller.getUserUtente());
                        }
                        MainMenuAdmin.getModelloListaDipendente().clear();
                        MainMenuAdmin.getModelloListaDipendente().addAll(((DipendenteWelcomeController)controller).getDipendentiInLocale());
                        MainMenuAdmin.getModelloListaTavoli().clear();
                        MainMenuAdmin.getModelloListaTavoli().addAll(((DipendenteWelcomeController)controller).getTavoliInLocale());
                        frameChiamato.dispose();
                    }else
                    {
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
