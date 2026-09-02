package gui.gestionale;

import model.gestionale.Sessione;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class VisualizzatoreSessioni
{
    private JList listaSessioni;
    private JPanel visualizzatoreSessioniPanel;
    private JButton indietroButton;
    private JLabel clienteSessioni;
    private JTextArea textAreaSessioni;

    private static DefaultListModel<Sessione> modelloListaSessioni;

    public VisualizzatoreSessioni(JFrame frameChiamante, ArrayList<Sessione> sessioni, String user)
    {
        JFrame thisFrame = new JFrame("VisualizzatoreSessioni");
        thisFrame.setContentPane(visualizzatoreSessioniPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        textAreaSessioni.setFocusable(false);
        textAreaSessioni.setEditable(false);

        clienteSessioni.setText("Sessioni svolte da " + user + ":");

        modelloListaSessioni = new DefaultListModel<>();
        modelloListaSessioni.addAll(sessioni);
        listaSessioni.setModel(modelloListaSessioni);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });

        listaSessioni.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Sessione temp = (Sessione) listaSessioni.getSelectedValue();

                if(temp != null)
                {
                    textAreaSessioni.setText(temp.infoSessione());
                }
                else textAreaSessioni.setText(null);
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
