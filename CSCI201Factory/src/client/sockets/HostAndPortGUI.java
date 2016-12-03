package client.sockets;

import client.Constants;
import utilities.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HostAndPortGUI extends JFrame {
    public static final long serialVersionUID = 1;
    private JTextField portTextField, hostnameTextField;
    private JLabel descriptionLabel, portLabel, hostnameLabel, errorLabel;
    private JButton connectButton;
    private Lock hostAndPortLock;
    private Condition hostAndPortCondition;
    private Socket socket;

    // Called by FactoryClient
    public Socket getSocket() {
        while (socket == null) {
            hostAndPortLock.lock();
            try {
                // wait until port is set, and socket is created
                hostAndPortCondition.await();
            } catch (InterruptedException ie) {
                Util.printExceptionToCommand(ie);
            }
            hostAndPortLock.unlock();
        }
        return socket;
    }

    private class ConnectListener implements ActionListener {
        // this is triggered when the user presses "connect"
        public void actionPerformed(ActionEvent ae) {

            // GRAB PORT NUMBER
            String portStr = portTextField.getText();
            int portInt;
            try {
                portInt = Integer.parseInt(portStr);
            } catch (Exception e) {
                errorLabel.setText(Constants.portErrorString);
                return;
            }

            // ATTEMPT CONNECTION
            if (portInt > utilities.Constants.lowPort && portInt < utilities.Constants.highPort) {
                // try to connect
                String hostnameStr = hostnameTextField.getText();
                try {
                    // set the socket
                    socket = new Socket(hostnameStr, portInt);
                    hostAndPortLock.lock();
                    hostAndPortCondition.signal();
                    hostAndPortLock.unlock();
                    HostAndPortGUI.this.setVisible(false);
                } catch (IOException ioe) {
                    errorLabel.setText(Constants.unableToConnectString);
                    Util.printExceptionToCommand(ioe);
                }
            } else { // port value out of range
                errorLabel.setText(Constants.portErrorString);
            }
        }
    }


    // SET UP GUI...

    public HostAndPortGUI() {
        super(Constants.hostAndPortGUITitleString);
        initializeVariables();
        createGUI();
        addActionAdapters();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeVariables() {
        socket = null;
        descriptionLabel = new JLabel(Constants.hostAndPortDescriptionString);
        portLabel = new JLabel(Constants.portLabelString);
        hostnameLabel = new JLabel(Constants.hostnameLabelString);
        errorLabel = new JLabel();
        portTextField = new JTextField(20);
        portTextField.setText("" + utilities.Constants.defaultPort);
        hostnameTextField = new JTextField(20);
        hostnameTextField.setText(utilities.Constants.defaultHostname);
        connectButton = new JButton(Constants.connectButtonString);
        hostAndPortLock = new ReentrantLock();
        hostAndPortCondition = hostAndPortLock.newCondition();
    }

    private void createGUI() {
        setSize(Constants.hostAndPortGUIwidth, Constants.hostAndPortGUIheight);
        setLayout(new GridLayout(5, 1));
        add(descriptionLabel);
        add(errorLabel);
        JPanel hostFieldPanel = new JPanel();
        hostFieldPanel.setLayout(new FlowLayout());
        hostFieldPanel.add(hostnameLabel);
        hostFieldPanel.add(hostnameTextField);
        add(hostFieldPanel);
        JPanel portFieldPanel = new JPanel();
        portFieldPanel.setLayout(new FlowLayout());
        portFieldPanel.add(portLabel);
        portFieldPanel.add(portTextField);
        add(portFieldPanel);
        add(connectButton);
    }

    private void addActionAdapters() {
        connectButton.addActionListener(new ConnectListener());
        hostnameTextField.addActionListener(new ConnectListener());
        portTextField.addActionListener(new ConnectListener());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
