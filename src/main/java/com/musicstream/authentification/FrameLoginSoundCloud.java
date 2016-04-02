package com.musicstream.authentification;

import javax.swing.JInternalFrame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Malek Sound Cloud Login Form (Internal Frame )
 */
public class FrameLoginSoundCloud extends JInternalFrame {
	BufferedImage myPicture;
	public JPasswordField passwordText;
	public JTextField userText;
	public JButton loginButton,backBtn;

	public FrameLoginSoundCloud() {
		initComponent();
	}

	/**
	 * Initialize the JFrame's components
	 */
	private void initComponent() {
		try {
			myPicture = ImageIO.read(new File("images/soundCloud.png"));// Logo
																		// Display
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setSize(320, 70);
		picLabel.setBounds(60, 40, 250, 75);
		panel.add(picLabel);
		JLabel userLabel = new JLabel("Email :");
		userLabel.setBounds(30, 120, 80, 25);
		panel.add(userLabel);

		userText = new JTextField(20);
		userText.setBounds(150, 120, 160, 25);
		panel.add(userText);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(30, 150, 80, 25);
		panel.add(passwordLabel);

		passwordText = new JPasswordField(20);
		passwordText.setBounds(150, 150, 160, 25);
		panel.add(passwordText);

		loginButton = new JButton("login");
		loginButton.setBounds(180, 190, 80, 25);
		panel.add(loginButton);
		backBtn = new JButton("Back");
		backBtn.setBounds(5, 10, 80, 25);
		panel.add(backBtn);
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
