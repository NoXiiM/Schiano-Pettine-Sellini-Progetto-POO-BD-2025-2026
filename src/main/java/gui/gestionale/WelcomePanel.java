package gui.gestionale;

import controller.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel {

    private JPanel WelcomePanel;
    private JPanel LoginForm;
    private JLabel WelcomeImageLabel;

    private JPasswordField passwordField;
    private JButton accediButton;
    private JTextField userField;
    private JLabel ForgotPass;
    private JComboBox userType;

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
                    welcomeController.login(username, password, loginMode);

                    if(userType.getSelectedItem().equals("Admin")){
                        mainMenuAdmin mainMenuAdminFrame= new mainMenuAdmin(welcomeController, mainframe, username);
                    } else {
                        mainMenuPlayer mainMenuPlayerFrame= new mainMenuPlayer(welcomeController, mainframe, username);
                    }

                } catch (RuntimeException empty_field_ex){
                    JOptionPane.showMessageDialog(null, empty_field_ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
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