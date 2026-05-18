package gui;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel {

    private JPanel WelcomePanel;
    private JPanel LoginForm;
    private JLabel WelcomeImageLabel;

    private JPasswordField passwordField1;
    private JButton accediButton;
    private JTextField textField1;
    private JLabel ForgotPass;



    public static void main(String[] args) {
        JFrame frame = new JFrame("SSP Casino");
        frame.setContentPane(new WelcomePanel().WelcomePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}