package com.musicstream.authentification;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Malek Authentication Window
 */
public class Authentication extends JInternalFrame {
	// Setting up The Frame and its content
	// JFrame authenticationWindow = new JFrame();
	JPanel authenticationPanel = new JPanel(new FlowLayout());
	public JButton spotify = new JButton(new ImageIcon(
			"images/log_in-desktop-medium.png"));
	public JButton deezer = new JButton(new ImageIcon("images/deezer.png"));
	public JButton soundCloud = new JButton(new ImageIcon(
			"images/soundCloud.png"));
	BufferedImage myPicture;

	public Authentication() {

		try {
			myPicture = ImageIO.read(new File("images/logo_small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spotify.setEnabled(false);
		// Panel Design
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		JPanel buttonsPan = new JPanel();
		buttonsPan.setLayout(new BoxLayout(buttonsPan, BoxLayout.Y_AXIS));
		buttonsPan.setBorder(BorderFactory.createEmptyBorder(35, 60, 15, 0));

		authenticationPanel.setLayout(new BorderLayout());
		authenticationPanel.add(picLabel, BorderLayout.NORTH);

		//buttonsPan.add(spotify);
		buttonsPan.add(soundCloud);
		buttonsPan.add(deezer);
		

		authenticationPanel.add(buttonsPan, BorderLayout.CENTER);

		this.setSize(397, 371);
		javax.swing.plaf.InternalFrameUI ui = this.getUI();
		((javax.swing.plaf.basic.BasicInternalFrameUI) ui).setNorthPane(null);
		this.setVisible(true);
		this.setContentPane(authenticationPanel);

		this.setResizable(false);

	}
}
