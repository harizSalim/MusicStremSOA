package com.musicstream.authentification;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Malek Login Confirmation Screen
 */
public class FrameSuccessLogin extends JInternalFrame {
	JButton goToHome;
	JButton goToBack;
	BufferedImage myPicture;

	public FrameSuccessLogin() {
		JPanel panel = new JPanel();
		try {
			myPicture = ImageIO.read(new File("images/tick.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panel.setLayout(null);
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));

		picLabel.setSize(320, 70);
		picLabel.setBounds(60, 20, 250, 75);
		panel.add(picLabel);

		JLabel msg = new JLabel("You Have Successfully logged in ");
		msg.setBounds(100, 80, 250, 75);
		panel.add(msg);

		goToHome = new JButton("Home");
		goToHome.setBounds(240, 160, 80, 25);
		goToBack = new JButton("Connect to Other Services");
		goToBack.setPreferredSize(new Dimension(120, 70));
		goToBack.setBounds(30, 160, 190, 25);

		panel.add(goToHome);
		panel.add(goToBack);
		panel.setLocation(50, 100);

		this.setClosable(false);
		this.setResizable(false);
		this.isMaximum = true;
		this.setLocation(0, 0);
		this.setRootPaneCheckingEnabled(false);
		javax.swing.plaf.InternalFrameUI ui = this.getUI();
		((javax.swing.plaf.basic.BasicInternalFrameUI) ui).setNorthPane(null);
		this.getContentPane().add(panel);
		this.setVisible(true);
	}

}
