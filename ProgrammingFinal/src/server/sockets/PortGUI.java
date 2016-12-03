package server.sockets;

import server.Constants;
import utilities.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* NOTES
.getServerSocket: waits for a serverSocket to be created
 */

public class PortGUI extends JFrame {
    public static final long serialVersionUID = 1;

    private Lock portLock;
    private Condition portCondition;

    private ServerSocket ss;

    // called by FactoryServer.java
    public ServerSocket getServerSocket() {
        while (ss == null) {
            portLock.lock();
            try {
                portCondition.await();
            } catch (InterruptedException ie) {
                Util.printExceptionToCommand(ie);
            }
            portLock.unlock();
        }
        return ss;
    }

    // SET UP GUI:
    private JTextField portTextField;
    private JLabel descriptionLabel, portLabel, portErrorLabel;
    private JButton submitPortButton;

    public PortGUI() {
        super(Constants.portGUITitleString);
        initializeVariables();
        createGUI();
        addActionAdapters();
        setVisible(true);
    }

    private void initializeVariables() {
        descriptionLabel = new JLabel(Constants.portDescriptionString);
        portLabel = new JLabel(Constants.portLabelString);
        portErrorLabel = new JLabel();
        portTextField = new JTextField(20);
        portTextField.setText("" + utilities.Constants.defaultPort);
        submitPortButton = new JButton(Constants.submitPortString);
        portLock = new ReentrantLock();
        portCondition = portLock.newCondition();
        ss = null;
    }

    private void createGUI() {
        setSize(Constants.portGUIwidth, Constants.portGUIheight);
        GridLayout gl = new GridLayout(4, 1);
        setLayout(gl);
        add(descriptionLabel);
        JPanel portFieldPanel = new JPanel();
        portFieldPanel.setLayout(new FlowLayout());
        portFieldPanel.add(portLabel);
        portFieldPanel.add(portTextField);
        add(portErrorLabel);
        add(portFieldPanel);
        add(submitPortButton);
    }

    private void addActionAdapters() {
        class PortListener implements ActionListener {
            public void actionPerformed(ActionEvent ae) {
                String portStr = portTextField.getText();
                int portNumber;
                try {
                    portNumber = Integer.parseInt(portStr);
                } catch (Exception e) {
                    portErrorLabel.setText(Constants.portErrorString);
                    return;
                }
                if (portNumber > utilities.Constants.lowPort && portNumber < utilities.Constants.highPort) {
                    try {
                        ServerSocket tempss = new ServerSocket(portNumber);
                        portLock.lock();
                        ss = tempss;
                        portCondition.signal();
                        portLock.unlock();
                        PortGUI.this.setVisible(false);
                    } catch (IOException ioe) {
                        // this will get thrown if I can't bind to portNumber
                        portErrorLabel.setText(Constants.portAlreadyInUseString);
                    }
                } else {
                    portErrorLabel.setText(Constants.portErrorString);
                }
            }
        }
        submitPortButton.addActionListener(new PortListener());
        portTextField.addActionListener(new PortListener());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
