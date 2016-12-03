package client;

import client.game.FactoryManager;
import client.game.FactoryPanel;
import client.sockets.FactoryClientListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

/* NOTES
The FactoryClientGUI only sets up the frame, but the bulk of the work is being done by
- FactoryPanel: renders the gameboard
- FactoryManager: contains the animation thread that both runs and animates the simulation
 */

public class FactoryClientGUI extends JFrame {
    public static final long serialVersionUID = 1;

    private FactoryPanel factoryPanel;
    private final ThreadLocal<FactoryManager> factoryManager = new ThreadLocal<>();

    private JTextArea messageTextArea;
    private JTable productTable;
    private JScrollPane tableScrollPane;
    private JSlider simulationSpeedController;

    // JTable is kinda like a spreadsheet, data about the product's state
    public JTable getTable() {
        return productTable;
    }

    // addMessage prints to the GUI.
    public void addMessage(String msg) {
        if (messageTextArea.getText().length() != 0) {
            messageTextArea.append("\n");
        }
        messageTextArea.append(msg);
    }


    // SET UP GUI...

    FactoryClientGUI(Socket socket) {
        super(Constants.factoryGUITitleString);
        factoryManager.set(new FactoryManager());
        initializeVariables();
        createGUI();
        new FactoryClientListener(factoryManager.get(), this, socket);
        addActionAdapters();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeVariables() {
        messageTextArea = new JTextArea(10, 50);
        factoryPanel = new FactoryPanel();
        factoryManager.get().setFactoryRenderer(factoryPanel);

        DefaultTableModel productTableModel = new DefaultTableModel(null, Constants.tableColumnNames);
        productTable = new JTable(productTableModel);
        productTable.setEnabled(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(productTableModel);
        productTable.setRowSorter(sorter);
        tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBounds(Constants.factoryGUIwidth - Constants.tableWidth - 10, 0, Constants.tableWidth, Constants.factoryGUIheight - 100);

        // create the simulator speed controller JSlider
        simulationSpeedController = new JSlider(JSlider.HORIZONTAL, Constants.simulation_0x, Constants.simulation_3x, Constants.simulation_1x);
        simulationSpeedController.addChangeListener(factoryManager.get().new SpeedChangeListener());
        simulationSpeedController.setMajorTickSpacing(1);
        simulationSpeedController.setMinorTickSpacing(1);
        simulationSpeedController.setPaintTicks(true);
    }

    private void createGUI() {
        setSize(Constants.factoryGUIwidth, Constants.factoryGUIheight);
        setLayout(new BorderLayout());
        JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);

        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(messageTextAreaScrollPane);
        bottomBox.add(simulationSpeedController);

        add(factoryPanel, BorderLayout.CENTER);
        add(bottomBox, BorderLayout.SOUTH);
        add(tableScrollPane, BorderLayout.EAST);
    }

    private void addActionAdapters() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

}
