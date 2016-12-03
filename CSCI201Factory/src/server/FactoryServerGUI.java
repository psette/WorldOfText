package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

public class FactoryServerGUI extends JFrame {

    public static final long serialVersionUID = 1;
    private static JTextArea textArea;
    private JScrollPane textAreaScrollPane;
    private JButton selectFactoryButton;
    private JComboBox<String> selectFactoryComboBox;

    // called by
    // - ConfigureFactoryListener (when a file has been read and loaded)
    // - ServerListener (whenever a new client has connected to the factory)
    // - ServerClientCommunicator (whenever a client sends a message)
    public static void addMessage(String msg) {
        if (textArea.getText() != null && textArea.getText().trim().length() > 0) {
            textArea.append("\n" + msg);
        } else {
            textArea.setText(msg);
        }
    }

    // SETTING UP GUI:
    FactoryServerGUI() {
        super(Constants.factoryGUITitleString);
        initializeVariables();
        createGUI();
        addActionAdapters();
        setVisible(true);
    }

    private void initializeVariables() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textAreaScrollPane = new JScrollPane(textArea);

        Vector<String> filenamesVector = new Vector<>();
        File directory = new File(Constants.defaultResourcesDirectory);
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null) {
            for (File file : filesInDirectory) {
                if (file.isFile()) {
                    filenamesVector.add(file.getName());
                }
            }
        }
        selectFactoryButton = new JButton(Constants.selectFactoryButtonString);
        selectFactoryComboBox = new JComboBox<>(filenamesVector);
    }

    private void createGUI() {
        setSize(Constants.factoryGUIwidth, Constants.factoryGUIheight);
        JPanel northPanel = new JPanel();
        northPanel.add(selectFactoryComboBox);
        northPanel.add(selectFactoryButton);
        add(northPanel, BorderLayout.NORTH);
        add(textAreaScrollPane, BorderLayout.CENTER);
    }

    private void addActionAdapters() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        selectFactoryButton.addActionListener(new ConfigureFactoryListener(selectFactoryComboBox));
    }
}
