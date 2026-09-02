package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dipendente;
import model.gestionale.utenteEFigli.Supervisore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        JFrame thisFrame = new JFrame("ChangeUsername");
        thisFrame.setContentPane(changeUserPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        confermaCambioUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newUser = newUserField.getText();
                String pass1 = new String(passwordField.getPassword());   //getPassword restituisce char[]
                String pass2= new String(passwordField2.getPassword());   //getPassword restituisce char[]

                try {

                    if(controller.getCurrentUser() instanceof Cliente && ((ClientWelcomeController)controller).changeUsername(newUser, pass1, pass2)){
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");

                        thisFrame.setVisible(false);
                        frameChiamante.setVisible(true);
                        for(JLabel l : labels){
                            l.setText(controller.getUserUtente());
                        }
                        thisFrame.dispose();

                    } else if (controller.getCurrentUser() instanceof Dipendente && ((DipendenteWelcomeController)controller).changeUsername(newUser, pass1, pass2)) {
                        JOptionPane.showMessageDialog(null, "Username modificato con successo !");


                        thisFrame.setVisible(false);
                        frameChiamante.setVisible(true);
                        for(JLabel l : labels){
                            l.setText(controller.getUserUtente());
                        }
                        if(controller.getCurrentUser() instanceof Supervisore) {
                            MainMenuAdmin.getModelloListaDipendente().clear();
                            MainMenuAdmin.getModelloListaDipendente().addAll(((DipendenteWelcomeController) controller).getDipendentiInLocale());
                            MainMenuAdmin.getModelloListaTavoli().clear();
                            MainMenuAdmin.getModelloListaTavoli().addAll(((DipendenteWelcomeController) controller).getTavoliInLocale());
                        }
                        thisFrame.dispose();
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
