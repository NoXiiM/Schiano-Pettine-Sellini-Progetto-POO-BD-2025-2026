package gui.gestionale;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Schermata di Login sia per clienti che per dipendenti, presenti pulsanti di Registrazione e Reset Password.
 */
public class WelcomePanel {

    private JPanel WelcomePanel;
    private JPanel LoginForm;
    private JPasswordField passwordField;
    private JButton accediButton;
    private JTextField userField;
    private JLabel ForgotPass;
    private JButton registratiButton;

    private final WelcomeController welcomeController;
    private static JFrame mainframe;    //FRAME PRINCIPALE STATIC


    /**
     * Costruttore di WelcomePanel che, in base al tipo di Utente riconosciuto al login, chiama i rispettivi pannelli istanziando dei nuovi controller specifici.
     */
    public WelcomePanel() {

        welcomeController= new WelcomeController();

        //Setting icon
        ImageIcon iconaWelcomePanel = new ImageIcon(Objects.requireNonNull(getClass().getResource("/IconeSlotmachin/ssp_casino_welcome_v2.png")));
        mainframe.setIconImage(iconaWelcomePanel.getImage().getScaledInstance(2156, 1531, Image.SCALE_SMOOTH));

        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String password= passwordField.getText();
                String username= userField.getText();
                //String loginMode= (String) userType.getSelectedItem();

                try{
                    welcomeController.login(username, password);

                    userField.setText("");
                    passwordField.setText("");

                    if(welcomeController.isUtenteACliente()){
                        new TabbedMenuPlayer(new ClientWelcomeController(welcomeController), mainframe);
                    } else if (welcomeController.isUtenteADealer()){
                        new DealerPanel(new DipendenteWelcomeController(welcomeController), mainframe);
                    } else {
                        new MainMenuAdmin(new DipendenteWelcomeController(welcomeController), mainframe);
                    }

                } catch (RuntimeException empty_field_ex){
                    JOptionPane.showMessageDialog(null, empty_field_ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "username o password errati",
                            "Errore", JOptionPane.ERROR_MESSAGE);
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

                //IMPORTANTE: è necessario solo qui e per registrazione perché per gli accessi il current user viene sovrascritto
                welcomeController.setCurrentUserNull();
                new ForgotPassword(new ClientWelcomeController(welcomeController), mainframe);
            }
        });


        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userField.setText("");
                passwordField.setText("");

                //IMPORTANTE: è necessario solo qui e per forgot password perché per gli accessi il current user viene sovrascritto
                welcomeController.setCurrentUserNull();
                new RegistrationForm(new ClientWelcomeController(welcomeController), mainframe);
            }
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    static void main(String[] args) {
        mainframe = new JFrame("SSP Casino");
        mainframe.setContentPane(new WelcomePanel().WelcomePanel);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.pack();
        mainframe.setVisible(true);

    }
}