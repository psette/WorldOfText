package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LoadingWindow extends JFrame {

	private static final long serialVersionUID = -6979002892334120038L;

	public LoadingWindow() {
		super("Loading");
		add(addPanel());
		setSize(250, 225);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private JPanel addPanel() {
		JPanel main = new JPanel();
		JLabel label = new JLabel("Loading, Please be patient", SwingConstants.CENTER);
		Font font = new Font("Arial", Font.BOLD, 18);
		label.setFont(font);
		main.setBackground(Color.black);
		label.setForeground(Color.WHITE);
		main.add(label, BorderLayout.NORTH);
		ImageIcon loading = new ImageIcon("output_C2R8U3.gif");
		JLabel image = new JLabel(loading);
		main.add(image);
		return main;
	}

}
