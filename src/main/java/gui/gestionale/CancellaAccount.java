package gui.gestionale;

import controller.gestionale.ClientWelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * GUI tramite cui un utente può cambiare username
 */
public class CancellaAccount {
    private JPanel delUserPanel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JTextField confirmField;
    private JButton cancellaAccountButton;
    private JButton backButton;

    /**
     * Bisogna riempire correttamente i campi di testo con username, password e parola di conferma per cancellare correttamente
     * l'account
     *
     * @param controller     the controller
     * @param frameChiamante the frame chiamante
     * @param loginFrame     the login frame, primo frame
     */
    public CancellaAccount(ClientWelcomeController controller, JFrame frameChiamante, JFrame loginFrame) {
        JFrame thisFrame = new JFrame("CancellaAccount");
        thisFrame.setContentPane(delUserPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);


        cancellaAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String user= userField.getText();
                    //una getText più sicura per le password
                    String pass= new String(passwordField.getPassword());
                    String conferma= confirmField.getText();

                    int risposta = JOptionPane.showConfirmDialog(
                            null,
                            "Sei sicuro di voler eliminare DEFINITIVAMENTE il tuo account ? \nATTENZIONE: Il saldo non prelevato andrà perso",
                            "Conferma eliminazione account",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if(risposta == JOptionPane.YES_OPTION) {

                        if (controller.deleteUser(user, pass, conferma)) {
                            JOptionPane.showMessageDialog(null, "Account eliminato !");

                            thisFrame.setVisible(false);
                            thisFrame.dispose();
                            frameChiamante.setVisible(false);
                            frameChiamante.dispose();
                            loginFrame.setVisible(true);

                        } else {
                            JOptionPane.showMessageDialog(null, "I campi non corrispondono !", "Errore", JOptionPane.ERROR_MESSAGE);

                        }

                        userField.setText("");
                        passwordField.setText("");
                        confirmField.setText("");
                    }

                } catch(RuntimeException | SQLException e1){
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

