package com.musicstream.authentification;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Salim The Main JFrame of the Authentication Page , The Class
 *         implements a JDesktopPanel to switch between the login frames of the
 *         services , when we select a service to log into, its Frame goes to
 *         front. The JDesktopPane was used to have the possibility to navigate
 *         between frames inside one global Frame and to make navigation more
 *         Appropriate.
 */
public class FrameLoginDeezer extends JInternalFrame {
	BufferedImage myPicture;
	public JPasswordField passwordText;
	public JTextField userText;
	public JButton loginButton,deezerBack;

	public FrameLoginDeezer() {
		initComponent();
	}

	private void initComponent() {
		try {
			myPicture = ImageIO.read(new File("images/deezer.png"));
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
		userLabel.setBounds(30, 90, 80, 25);
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
		deezerBack=new JButton("Back");
		deezerBack.setBounds(5, 10, 80, 25);
		loginButton = new JButton("login");
		loginButton.setBounds(180, 190, 80, 25);
		panel.add(deezerBack);
		panel.add(loginButton);
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
