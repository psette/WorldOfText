package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import resource.Factory;

public class FactoryClientGUI extends JFrame implements KeyListener, ActionListener {

	public static final long serialVersionUID = 1;

	private FactoryPanel factoryPanel;
	private FactoryManager factoryManager;
	Factory factory;
	private JTextArea messageTextArea;
	private JTable productTable;
	private DefaultTableModel productTableModel;
	private JScrollPane tableScrollPane;
	private JSlider simulationSpeedController;
	private FactoryHier factoryHeir;
	private ViewAuth viewAuth;
	private FactoryController factoryController; // Private FC for the GUI to
													// access
	private String pictureFile;
	// Member variable buttons for Lab5
	JButton pauseButton;
	JButton continueButton;
	JButton resetButton;

	FactoryClientGUI(Socket socket) {
		super(Constants.factoryGUITitleString);
		factoryManager = new FactoryManager();
		initializeVariables();
		createGUI();
		createMenu(); // Make the menu here
		new FactoryClientListener(factoryManager, this, socket);
		addActionAdapters();
		setLocationRelativeTo(null);

		// add the KeyListener

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	FactoryClientGUI(Socket socket, Factory factory) {
		super(Constants.factoryGUITitleString);
		this.factory = factory;
		factoryManager = new FactoryManager();
		initializeVariables();
		createGUI();
		createMenu(); // Make the menu here
		new FactoryClientListener(factoryManager, this, socket);
		addActionAdapters();
		setLocationRelativeTo(null);

		// add the KeyListener

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initializeVariables() {
		messageTextArea = new JTextArea(10, 50);
		factoryPanel = new FactoryPanel();
		factoryManager.setFactoryRenderer(factoryPanel);

		productTableModel = new DefaultTableModel(null, Constants.tableColumnNames);
		productTable = new JTable(productTableModel);
		productTable.setEnabled(false);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(productTableModel);
		productTable.setRowSorter(sorter);
		tableScrollPane = new JScrollPane(productTable);
		tableScrollPane.setBounds(Constants.factoryGUIwidth - Constants.tableWidth - 10, 0, Constants.tableWidth,
				Constants.factoryGUIheight - 100);

		tableScrollPane.setFocusable(true); // Set it to be focusable
		//
		// add the KeyListener
		tableScrollPane.addKeyListener(this);

		simulationSpeedController = new JSlider(JSlider.HORIZONTAL, Constants.simulation_0x, Constants.simulation_3x,
				Constants.simulation_1x);
		simulationSpeedController.addChangeListener(factoryManager);
		simulationSpeedController.setMajorTickSpacing(1);
		simulationSpeedController.setMinorTickSpacing(1);
		simulationSpeedController.setPaintTicks(true);
		simulationSpeedController.addChangeListener(factoryController); // Add
																		// the
																		// FC to
																		// the
																		// simulationSpeedController's
																		// changelistener
		// simulationSpeedController.addKeyListener(this);
		viewAuth = new ViewAuth("Pusheen");
		factoryController = new FactoryController(); // Initialize the FC
		addWindowStateListener(factoryController); // Add the window state
													// listener
	}

	private void createGUI() {
		setSize(Constants.factoryGUIwidth, Constants.factoryGUIheight);
		setLayout(new BorderLayout());
		JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);

		Box bottomBox = Box.createHorizontalBox();
		bottomBox.add(messageTextAreaScrollPane);
		bottomBox.add(new FactoryProgressPanel(productTable));
		// bottomBox.add(simulationSpeedController);

		add(factoryPanel, BorderLayout.CENTER);
		add(bottomBox, BorderLayout.SOUTH);
		add(tableScrollPane, BorderLayout.EAST);
	}

	public JTable getTable() {
		return productTable;
	}

	private void addActionAdapters() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void addMessage(String msg) {
		if (messageTextArea.getText().length() != 0) {
			messageTextArea.append("\n");
		}
		messageTextArea.append(msg);
	}

	// New method to create the menu
	private void createMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu controller = new JMenu("Controller"), factoryH = new JMenu("Factory Hierarchy"),
				about = new JMenu("About the Author");
		controller.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				factoryController.setVisible(true);
			}
		});
		factoryH.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				factoryHeir = new FactoryHier();
			}
		});
		JButton view = new JButton("View Author"), setName = new JButton("Set Name"),
				setPic = new JButton("Set Picture");
		about.add(view);
		view.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				viewAuth.setVisible(true);

			}
		});
		setName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(null, "Set the author name:");
				BufferedReader file;
				try {
					file = new BufferedReader(new FileReader(
							"/Users/pietrosette/Documents/workspace/ProgrammingMidterm/resources/factory.txt"));
					String line;
					ArrayList<String> newFile = new ArrayList<String>();
					while ((line = file.readLine()) != null) {
						if (line.startsWith("Author Name|")) {
							newFile.add("Author Name|" + s);
						} else {
							newFile.add(line);
						}
					}
					FileWriter fw = new FileWriter(
							"/Users/pietrosette/Documents/workspace/ProgrammingMidterm/resources/factory.txt", false);
					for (String line2 : newFile) {
						fw.write(line2 + "\n");
					}
					file.close();
					fw.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		setPic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser file_choice = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("picture files", "jpg", "png");
				file_choice.setFileFilter(filter);
				int approve = file_choice.showOpenDialog(file_choice);
				if (approve == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = file_choice.getSelectedFile();
						pictureFile = selectedFile.getAbsolutePath();
						viewAuth.changePic(pictureFile);
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(new JFrame(), null, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		about.add(setName);
		about.add(setPic);
		menu.add(controller);
		menu.add(factoryH, BorderLayout.CENTER);
		menu.add(about, BorderLayout.EAST);
		setJMenuBar(menu);
	}

	// Code for FC
	class FactoryController extends JFrame implements ChangeListener, WindowStateListener {
		private static final long serialVersionUID = 457592347485273489L;
		private Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		private JTabbedPane tabbedPane;
		JPanel buttonBox;
		int speed = Constants.simulation_1x; // Temp variable to store the speed
		int windowSaveSpeed;

		public FactoryController() {
			super("Factory Controller"); // Super call for JFrame
			setSize(320, 240); // Set the size of the frame
			tabbedPane = new JTabbedPane();
			createTimePanel();
			createOtherPanel();

			add(tabbedPane);
			setVisible(false); // Is this frame visible at the start? Nope
			setLocationRelativeTo(null); // sets the location of the JFrame with
											// respect to another Component,
											// passing null to this centers the
											// JFrame on the screen.
		}

		private void createTimePanel() {
			JPanel timePanel = new JPanel(); // Make a new JPanel
			timePanel.setLayout(new BorderLayout()); // Set the layout

			// timePanel.add(simulationSpeedController); //Add the speed
			// controller

			labelTable.put(Constants.simulation_0x, new JLabel("Paused")); // Populate
																			// the
																			// hashtable
																			// with
																			// JLabels
			labelTable.put(Constants.simulation_1x, new JLabel("Normal"));
			labelTable.put(Constants.simulation_2x, new JLabel("Double"));
			labelTable.put(Constants.simulation_3x, new JLabel("Triple"));

			simulationSpeedController.setLabelTable(labelTable); // set the
																	// labels
			simulationSpeedController.setPaintLabels(true); // paint them
			simulationSpeedController.setBorder(new TitledBorder("Speed Controller")); // set
																						// the
																						// border
																						// and
																						// the
																						// title
			timePanel.add(simulationSpeedController, BorderLayout.SOUTH); // add
																			// the
																			// simulationSpeedController
			// simulationSpeedController.setFocusable(true);
			// simulationSpeedController.addKeyListener(this);
			buttonBox = new JPanel(); // Make another JPanel for the buttonbox

			buttonBox.setLayout(new GridBagLayout()); // The layout will be
														// gridbaglayout
			// buttonBox.setFocusable(true);
			// buttonBox.addKeyListener(this);
			GridBagConstraints gbc = new GridBagConstraints(); // Make some new
																// gbc
																// constraints
			gbc.fill = GridBagConstraints.HORIZONTAL; // "Yo, these buttons are
														// gonna be standing on
														// top of each other"
			gbc.weighty = 1; // Extrude the buttons so they will fill the entire
								// box vertically, not horizontally

			pauseButton = new JButton("Pause"); // First box
			pauseButton.addActionListener(new ActionListener() { // Anonymous
																	// inner
																	// class for
																	// PB
				@Override
				public void actionPerformed(ActionEvent ae) {
					doPause();
				}
			});
			gbc.gridy = 1; // Position is on top
			buttonBox.add(pauseButton, gbc); // add it using the gbc

			continueButton = new JButton("Continue"); // second box
			continueButton.setEnabled(false);// Set default to false
			continueButton.addActionListener(new ActionListener() { // Anonymous
																	// inner
																	// class for
																	// CB
				@Override
				public void actionPerformed(ActionEvent ae) {
					doContinue();
				}
			});

			gbc.gridy = 2; // position is middle
			buttonBox.add(continueButton, gbc); // add it

			resetButton = new JButton("Reset"); // third box
			resetButton.addActionListener(new ActionListener() { // Anonymous
																	// inner
																	// class for
																	// RB
				@Override
				public void actionPerformed(ActionEvent ae) {
					doReset();
				}
			});
			gbc.gridy = 3; // position is middle
			buttonBox.add(resetButton, gbc); // add it

			timePanel.add(buttonBox); // add the whole buttonbox shebang to the
										// timePanel
			timePanel.setFocusable(true);
			tabbedPane.add("Time", timePanel); // add the entire turducken to
												// the tabbed pane
		}

		private void createOtherPanel() {
			JPanel otherPanel = new JPanel(); // blank JPanel
			tabbedPane.add("Other", otherPanel); // add it
		}

		public void doPause() {
			windowSaveSpeed = simulationSpeedController.getValue();
			continueButton.setEnabled(true);
			pauseButton.setEnabled(false);
			speed = simulationSpeedController.getValue();
			simulationSpeedController.setValue(Constants.simulation_0x);
		}

		public void doContinue() {
			continueButton.setEnabled(false);
			pauseButton.setEnabled(true);
			simulationSpeedController.setValue(windowSaveSpeed);
		}

		public void doReset() {
			continueButton.setEnabled(false);
			pauseButton.setEnabled(true);
			speed = Constants.simulation_1x;
			simulationSpeedController.setValue(Constants.simulation_1x);
			factoryManager.resetFactory();
		}

		public void upTickSpeed() {
			speed = simulationSpeedController.getValue(); // get the current
															// value
			if (speed < Constants.simulation_3x) // As long as the speed isn't
													// maxed
			{
				if (speed == 0) // If the speed was 0, we gotta toggle the
								// buttons
				{
					continueButton.setEnabled(false);
					pauseButton.setEnabled(true);
				}
				speed++; // up the speed by 1
				simulationSpeedController.setValue(speed); // update the speed
			}
		}

		public void downTickSpeed() {
			speed = simulationSpeedController.getValue();
			if (speed > Constants.simulation_0x) // As long as the speed isn't
													// paused
			{
				speed--;
				if (speed == Constants.simulation_0x) // Now if the speed is 0
				{
					doPause(); // Pause it
				} else {
					simulationSpeedController.setValue(speed); // update the
																// speed
				}
			}
		}

		@Override
		public void stateChanged(ChangeEvent ce) {
			int state = ((JSlider) ce.getSource()).getValue();
			if (state == Constants.simulation_0x) {
				continueButton.setEnabled(true);
				pauseButton.setEnabled(false);
			} else {
				continueButton.setEnabled(false);
				pauseButton.setEnabled(true);
			}
		}

		@Override
		public void windowStateChanged(WindowEvent we) {
			int state = we.getNewState();
			if ((state = Frame.ICONIFIED) == Frame.ICONIFIED) {
				setVisible(false);
				windowSaveSpeed = simulationSpeedController.getValue();
				simulationSpeedController.setValue(Constants.simulation_0x);
			} else {
				simulationSpeedController.setValue(windowSaveSpeed);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		if (ke.getKeyChar() == 'p') {
			factoryController.doPause();
		} else if (ke.getKeyChar() == 'c') {
			System.out.println("I am continuing");
			factoryController.doContinue();
		} else if (ke.getKeyChar() == 'r') {
			factoryController.doReset();
		} else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
			factoryController.downTickSpeed();
		} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
			factoryController.upTickSpeed();
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		// Do nothing
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
