package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

class FactoryHier extends JFrame {
	JButton refresh = new JButton("Refresh");

	public FactoryHier() {
		super("Factory Hierarchy"); // Super call for JFrame
		setSize(320, 240); // Set the size of the frame
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
		main.add(addResources());
		main.add(addProducts());
		add(main, BorderLayout.CENTER);
		add(addButton(), BorderLayout.SOUTH);
		setVisible(true); // Is this frame visible at the start? Nope
		setLocationRelativeTo(null); // sets the location of the JFrame with
										// respect to another Component,
										// passing null to this centers the
										// JFrame on the screen.

	}

	private JPanel addButton() {
		JPanel main = new JPanel();
		main.setBackground(Color.gray);
		main.add(refresh);
		return main;
	}

	private JTree addTree(boolean isProducts) {
		String[] data1 = { "Motherboard", "Processor", "Hard Drive", "Memory", "Box" };

		String[] data2 = { "Cheap Computer", "Okay Computer", "Good Computer", "Amazing Computer", "Cheap Server",
				"Amazing Server" };
		String[] data = data1;
		if (isProducts) {
			data = data2;
		}
		JTree tree = new JTree(data);
		tree.setRootVisible(true);
		return tree;

	}

	private JPanel addProducts() {
		JPanel main = new JPanel();
		main.setBackground(Color.YELLOW);
		JScrollPane jsp = new JScrollPane(addTree(true));
		main.add(jsp, main.LEFT_ALIGNMENT);
		return main;
	}

	private Component addResources() {
		JPanel main = new JPanel();
		main.setBackground(Color.YELLOW);
		JScrollPane jsp = new JScrollPane(addTree(true));
		main.add(jsp, main.RIGHT_ALIGNMENT);
		return main;
	}

}