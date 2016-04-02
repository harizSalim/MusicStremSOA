package com.musicstream.homeTabs;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.musicstream.utils.AppUtils;

/**
 * @author Malek Home Frame : A Tabbed JFrame that contains the different
 *         Tabs(Tracks, PlayLists, Research, ...)
 */
public class HomeFrame extends JFrame {

	private JTabbedPane tabsPanel;
	// Menu Items
	public AppUtils appU;

	public HomeFrame() {
		this.setTitle("Music Stream -- EL Kamel Malek & Salim Hariz");
		appU = new AppUtils();
		tabsPanel = new JTabbedPane();
		initTabs();
		this.setVisible(true);
		this.setState(Frame.NORMAL);

		this.setSize(appU.getScreenWidth(), appU.getScreenHeight());
		this.setContentPane(tabsPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Initializing the Frame's Tabs ; The Tabs are developed in separate files
	 * and extends the JPanel Object
	 */
	private void initTabs() {
		// Tracks Tab
		ImageIcon iconTracks = new ImageIcon("images/tracks.png");
		TraksPan trackTab = new TraksPan();
		this.tabsPanel.addTab("Tracks", iconTracks, trackTab, "Tracks Tab");

		// Playlists Tab
		ImageIcon iconPlaylist = new ImageIcon("images/playlist.png");
		PlaylistsPan playlistTab = new PlaylistsPan();
		this.tabsPanel.addTab("Playlists", iconPlaylist, playlistTab,
				"playlist Tab");

		// Research Tab
		ImageIcon iconSearch = new ImageIcon("images/search.png");
		SearchPan searchTab = new SearchPan();
		this.tabsPanel.addTab("Search", iconSearch, searchTab, "search Tab");
	}

}
