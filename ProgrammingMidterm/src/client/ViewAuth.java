package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

class ViewAuth extends JFrame {
	JButton refresh = new JButton("Refresh");
	JLabel label = null;

	public ViewAuth(String name) {
		super("View Author"); // Super call for JFrame
		setBackground(Color.lightGray);
		setSize(320, 240); // Set the size of the frame

		add(new JLabel("Author of this factory:" + name), BorderLayout.NORTH);
		try {
			BufferedImage img = ImageIO
					.read(new File("/Users/pietrosette/Documents/workspace/ProgrammingMidterm/pusheen.png"));
			ImageIcon icon = new ImageIcon(img);
			label = new JLabel(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(label, BorderLayout.CENTER);
	}

	public void changePic(String pictureFile) {
		try {
			BufferedImage img = ImageIO.read(new File(pictureFile));
			ImageIcon icon = new ImageIcon(img);
			label = new JLabel(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}