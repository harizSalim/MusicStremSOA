package com.musicstream.authentification;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.JSONException;

import com.musicstream.homeTabs.HomeFrame;
import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.utils.AppUtils;
import com.musicstream.utils.CryptoException;
import com.musicstream.utils.CryptoUtils;

/**
 * @author Malek The Main JFrame of the Authentication Page , The Class
 *         implements a JDesktopPanel to switch between the login frames of the
 *         services , when we select a service to log into, its Frame goes to
 *         front. The JDesktopPane was used to have the possibility to navigate
 *         between frames inside one global Frame and to make navigation more
 *         Appropriate.
 */
public class MainAuthFrame extends JFrame implements WindowListener {

	private JDesktopPane desktop = new JDesktopPane();
	private Boolean statusDeezer = false;
	private Boolean statusSC = false;
	// Frames
	public FrameLoginSoundCloud loginSoundCloud = new FrameLoginSoundCloud();
	public FrameLoginDeezer loginDeezer = new FrameLoginDeezer();
	public Authentication mainAuthForm = new Authentication();
	public FrameSuccessLogin succWindow = new FrameSuccessLogin();
	// Listeners
	private connectWithSoundCloudListener conScListner = new connectWithSoundCloudListener();
	private connectWithDeezerListener conDzListener = new connectWithDeezerListener();
	private backWithSoundCloudListener backList = new backWithSoundCloudListener();
	private goToHomeListener goHListener = new goToHomeListener();
	private loginSoundCloudListener logScListner = new loginSoundCloudListener();
	private loginDeezerListener logDzListener = new loginDeezerListener();
	private goToOtherListener goBackListener = new goToOtherListener();
	// GUI Components
	public JButton btnLogSc, btnLogDz, btnBackSc, btnBackDeez;
	public JButton goToHome, goBack;
	public SoundCloudApi sc;
	public DeezerApi dz;
	public JPasswordField passwordTextSc, passwordTextDz;
	public JTextField userTextSc, userTextDz;
	private AppUtils appU;

	public MainAuthFrame() {

		initDesktop();
		// Action Listners
		this.mainAuthForm.soundCloud.addActionListener(conScListner);
		this.mainAuthForm.deezer.addActionListener(conDzListener);

		appU = new AppUtils();
		// Element Init & Actions
		// Sound Cloud
		btnLogSc = this.loginSoundCloud.loginButton;
		btnLogDz = this.loginDeezer.loginButton;
		goToHome = this.succWindow.goToHome;
		goBack = this.succWindow.goToBack;
		btnBackSc = this.loginSoundCloud.backBtn;
		btnBackDeez = this.loginDeezer.deezerBack;
		goBack.addActionListener(goBackListener);
		btnBackSc.addActionListener(backList);
		btnBackDeez.addActionListener(backList);
		goToHome.addActionListener(goHListener);
		btnLogSc.addActionListener(logScListner);
		btnLogDz.addActionListener(logDzListener);
		passwordTextSc = this.loginSoundCloud.passwordText;
		passwordTextDz = this.loginDeezer.passwordText;
		userTextSc = this.loginSoundCloud.userText;
		userTextDz = this.loginDeezer.userText;
		sc = new SoundCloudApi();
		dz = new DeezerApi();

		// Spotify
		// TODO Authentication using Spotify

		this.getContentPane().add(this.desktop);
		this.setTitle("Music Stream");
		this.setSize(397, 371);
		this.setVisible(true);
		this.setLocation(470, 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	/**
	 * Initializing the JDesktopPane by adding the services authentication forms
	 */
	private void initDesktop() {

		desktop.add(mainAuthForm);
		desktop.add(loginSoundCloud);
		desktop.add(loginDeezer);
		desktop.add(succWindow);
	}

	/**
	 * @author Malek Action Listener when the connect with SoundSloud button was
	 *         clicked , it puts the SoundCloud Login frame in front
	 */
	class connectWithSoundCloudListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// if (statusDeezer == true) {
			// loginSoundCloud.setEnabled(false);
			// loginSoundCloud.setBackground(Color.GRAY);
			loginSoundCloud.setVisible(true);
			loginSoundCloud.toFront();
			// }
		}
	}

	class backWithSoundCloudListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			mainAuthForm.setVisible(true);
			mainAuthForm.toFront();
		}

	}

	/**
	 * @author Salim Action Listener when the connect with Deezer button was
	 *         clicked , it puts the Deezer Login frame in front
	 */
	class connectWithDeezerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			loginDeezer.setVisible(true);
			loginDeezer.toFront();
		}
	}

	/**
	 * @author Malek / Action Listener when the SoundCloud login button was
	 *         clicked , it calls the SoundCloud Api and connects to SoundCloud
	 *         using the provided login and password.
	 */
	class loginSoundCloudListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent elem) {
			JOptionPane jop = new JOptionPane();

			if (elem.getSource().equals(btnLogSc)) {

				if (userTextSc.getText().equals("")
						|| passwordTextSc.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Type Your credentials",
							"MusicStream - Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (sc.userAuthentication(userTextSc.getText(),
							passwordTextSc.getText())) {
						statusSC = true;
						if (statusDeezer == false) {
							succWindow.setVisible(true);
							succWindow.toFront();
						} else {
							getOuter().setVisible(false);
							HomeFrame homeFrame;
							try {
								homeFrame = new HomeFrame();
								homeFrame.setVisible(true);
								homeFrame.toFront();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						// Data storage and encryption
						String key = "Mary has one cat";
						PrintWriter writer;

						try {
							writer = new PrintWriter("cred.txt", "UTF-8");
							writer.println(userTextSc.getText());
							writer.println(passwordTextSc.getText());
							writer.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						File inputFile = new File("cred.txt");
						File encryptedFile = new File("document.encrypted");

						try {
							CryptoUtils.encrypt(key, inputFile, encryptedFile);

						} catch (CryptoException ex) {
							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
						inputFile.delete();
					} else {
						JOptionPane.showMessageDialog(null,
								"Please Verify Your credentials",
								"MusicStream - Login Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		}
	}

	/**
	 * @author Salim / Action Listener when the Deezer login button was clicked
	 *         , it calls the Deezer Api and connects to SoundCloud using the
	 *         provided login and password.
	 */
	class loginDeezerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent elem) {
			JOptionPane jop = new JOptionPane();

			if (elem.getSource().equals(btnLogDz)) {

				if (userTextDz.getText().equals("")
						|| passwordTextDz.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Type Your credentials",
							"MusicStream - Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (dz.userAuthentication(userTextDz.getText(),
							passwordTextDz.getText())) {
						statusDeezer = true;
						if (statusSC == false) {
							succWindow.setVisible(true);
							succWindow.toFront();
						} else {
							getOuter().setVisible(false);
							HomeFrame homeFrame;
							try {
								homeFrame = new HomeFrame();
								homeFrame.setVisible(true);
								homeFrame.toFront();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// Data storage and encryption
						String key = "Mary has one cat";
						PrintWriter writer;
						try {
							writer = new PrintWriter("cred.txt", "UTF-8");
							writer.println(userTextDz.getText());
							writer.println(passwordTextDz.getText());
							writer.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						File inputFile = new File("cred.txt");
						File encryptedFile = new File("document.encrypted");

						try {
							CryptoUtils.encrypt(key, inputFile, encryptedFile);

						} catch (CryptoException ex) {
							System.out.println(ex.getMessage());
							ex.printStackTrace();
						}
						inputFile.delete();
					} else {
						JOptionPane.showMessageDialog(null,
								"Please Verify Your credentials",
								"MusicStream - Login Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		}
	}

	/**
	 * @author Malek Action Listener that would be triggered when the home
	 *         Button is clicked
	 */
	class goToHomeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			getOuter().setVisible(false);
			try {
				HomeFrame home = new HomeFrame();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author Malek Displayed the login screen that has the option to connect
	 *         to other services
	 */
	class goToOtherListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			mainAuthForm.setVisible(true);
			mainAuthForm.toFront();
		}
	}

	/**
	 * @return mainAuthFrame class to be able to access it from an inner class
	 *         (Listeners)
	 */
	public MainAuthFrame getOuter() {
		return MainAuthFrame.this;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		appU.deleteFiles();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
