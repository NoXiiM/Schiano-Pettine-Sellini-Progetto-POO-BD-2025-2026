package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Dipendente;
import model.gestionale.utenteEFigli.Supervisore;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AssegnaDipendentiTavolo {
    private JList listaDealer;
    private JList listaSupervisori;
    private JButton assegnaTavoloButton;
    private JTextArea textAreaInfoDipendenti;
    private JPanel panelAssegnaDipendenti;
    private JButton indietroButton;
    private JLabel tavoloInteressato;

    private static DefaultListModel<Dealer> modelloListaDealer;
    private static DefaultListModel<Supervisore> modelloListaSupervisore;

    public AssegnaDipendentiTavolo(DipendenteWelcomeController controller, JFrame frameChiamante, int indiceTavolo, boolean deleteMode)
    {
        JFrame thisFrame = new JFrame("AssegnaDipendentiTavolo");
        thisFrame.setContentPane(panelAssegnaDipendenti);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        ArrayList<Dealer> dealers = new ArrayList<>();
        ArrayList<Supervisore> supervisori = new ArrayList<>();

        if(!deleteMode) controller.dividiDealerSupervisore(dealers, supervisori);
        else controller.dividiDealerSupervisoreTavoloAtIndex(dealers, supervisori, indiceTavolo);

        modelloListaDealer = new DefaultListModel<>();
        modelloListaDealer.addAll(dealers);
        listaDealer.setModel(modelloListaDealer);

        modelloListaSupervisore= new DefaultListModel<>();
        modelloListaSupervisore.addAll(supervisori);
        listaSupervisori.setModel(modelloListaSupervisore);

        textAreaInfoDipendenti.setEditable(false);
        textAreaInfoDipendenti.setFocusable(false);

        tavoloInteressato.setText(controller.infoTavoloAtIndex(indiceTavolo));

        listaDealer.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listaSupervisori.clearSelection();

                Dipendente temp= (Dipendente) listaDealer.getSelectedValue();

                if(temp != null){
                    stampaDipendenteInfoField(temp);
                }
                else
                {
                    textAreaInfoDipendenti.setText(null);
                }
            }
        });
        listaSupervisori.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listaDealer.clearSelection();

                Dipendente temp= (Dipendente) listaSupervisori.getSelectedValue();

                if(temp != null){
                    stampaDipendenteInfoField(temp);
                }
                else
                {
                    textAreaInfoDipendenti.setText(null);
                }
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });

        if(!deleteMode)
        {
            assegnaTavoloButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Dipendente temp;

                    if ((temp = (Dipendente) listaDealer.getSelectedValue()) != null) {
                        Dealer dealer = (Dealer) temp;

                        if (dealer.getGiochiDealer().contains(controller.getGiocoAtIndex(indiceTavolo))) {
                            try {
                                controller.aggiornaInfoTavolodb(temp.getIdentificativoDipendente(), "Dealer",
                                        controller.idTavoloAtIndex(indiceTavolo));

                                controller.aggiungiDealerAtIndex((Dealer) temp, indiceTavolo);

                                modelloListaDealer.clear();
                                modelloListaDealer.addAll(dealers);

                                JOptionPane.showMessageDialog(null, "operazione completata con successo");
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "il dealer è già assegnato ad un altro tavolo",
                                        "errore", JOptionPane.ERROR_MESSAGE);
                            }
                        } else JOptionPane.showMessageDialog(null, "il dealer non è abilitato a servire a questo gioco",
                                "errore", JOptionPane.ERROR_MESSAGE);
                    } else if ((temp = (Dipendente) listaSupervisori.getSelectedValue()) != null) {
                        try {
                            controller.aggiornaInfoTavolodb(temp.getIdentificativoDipendente(), "Supervisore",
                                    controller.idTavoloAtIndex(indiceTavolo));

                            controller.aggiungiSupervisoreAtIndex((Supervisore) temp, indiceTavolo);

                            modelloListaSupervisore.clear();
                            modelloListaSupervisore.addAll(supervisori);

                            JOptionPane.showMessageDialog(null, "operazione completata con successo");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "il supervisore è già assegnato a questo tavolo",
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else JOptionPane.showMessageDialog(null, "nessun dipendente selezionato",
                            "errore", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        else
        {
            assegnaTavoloButton.setText("rimuovi");

            assegnaTavoloButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Dipendente temp;

                    if (listaDealer.getSelectedValue() != null) {
                        try {
                            controller.aggiornaInfoTavolodb(null, "Dealer",
                                    controller.idTavoloAtIndex(indiceTavolo));

                            controller.aggiungiDealerAtIndex(null, indiceTavolo);

                            modelloListaDealer.clear();

                            JOptionPane.showMessageDialog(null, "operazione completata con successo");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if ((temp = (Dipendente) listaSupervisori.getSelectedValue()) != null) {
                        try {
                            controller.eliminaSupervisore(indiceTavolo, (Supervisore) temp);

                            supervisori.remove(temp);

                            modelloListaSupervisore.clear();
                            modelloListaSupervisore.addAll(supervisori);

                            JOptionPane.showMessageDialog(null, "operazione completata con successo");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "il supervisore è già assegnato a questo tavolo",
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else JOptionPane.showMessageDialog(null, "nessun dipendente selezionato",
                            "errore", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    public void stampaDipendenteInfoField(Dipendente temp)
    {
        textAreaInfoDipendenti.setText("username: " + temp.getUsername());

        if(temp instanceof Dealer i)
        {
            textAreaInfoDipendenti.append("\n\ngiochi a cui serve: " + i.getGiochiDoveServeString());
        }
    }
}
