package gui.gestionale;

import controller.ClienteCorrente;
import controller.WelcomeController;
import model.gestionale.utenteEFigli.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomePanel {

    private JPanel WelcomePanel;
    private JPanel LoginForm;
    private JPasswordField passwordField;
    private JButton accediButton;
    private JTextField userField;
    private JLabel ForgotPass;
    private JComboBox userType;
    private JButton registratiButton;

    private WelcomeController welcomeController;
    private static JFrame mainframe;    //FRAME PRINCIPALE STATIC


    public WelcomePanel() {

        welcomeController= new WelcomeController();

        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String password= passwordField.getText();
                String username= userField.getText();
                String loginMode= (String) userType.getSelectedItem();

                try{
                    Utente user= welcomeController.login(username, password);

                    if(user instanceof Dipendente){

                    } else {
                        userField.setText("");
                        passwordField.setText("");
                        TabbedMenuPlayer menuPlayerFrame= new TabbedMenuPlayer(welcomeController, mainframe, new ClienteCorrente((Cliente) user));
                    }

                } catch (RuntimeException empty_field_ex){
                    JOptionPane.showMessageDialog(null, empty_field_ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ForgotPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // cursore con dito quando si passa sopra Pass dimenticata
        ForgotPass.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {                                //quando il mouse entra nel blocco ( sottolinea )
                ForgotPass.setText("<html><u>Password dimenticata ?</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {                                 //quando esce dal blocco ( toglie sottolineatura )
                ForgotPass.setText("Password dimenticata ?");
            }

            @Override
            public void mouseClicked(MouseEvent e) {                                //quando viene cliccato

                userField.setText("");
                passwordField.setText("");

                ForgotPassword forgotPasswordPanel= new ForgotPassword(welcomeController, mainframe);
            }
        });


        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userField.setText("");
                passwordField.setText("");

                RegistrationForm registrationForm= new RegistrationForm(welcomeController, mainframe);

            }
        });
    }

    public static void main(String[] args) {
        mainframe = new JFrame("SSP Casino");
        mainframe.setContentPane(new WelcomePanel().WelcomePanel);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.pack();
        mainframe.setVisible(true);
    }
}