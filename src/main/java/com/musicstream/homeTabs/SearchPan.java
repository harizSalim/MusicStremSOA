package com.musicstream.homeTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.ArrayUtils;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;

import de.voidplus.soundcloud.Track;

/**
 * 
 * @author Salim
 * 
 */
public class SearchPan extends JPanel implements ActionListener,
		ListSelectionListener {
	public static JTextField textField;
	private JButton search;
	private Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	private DeezerApi deezerApi;
	private Font fontLabel;
	private String[] nameList;
	private JList list;
	private JScrollPane scroll = null;
	private SoundCloudApi soundCApi;
	private String[] streamU;
	private int[] tracksLength;
	private MusicPlayer player;
	private String title;
	private String[] tracksSource;

	public SearchPan() {
		appU = new AppUtils();
		tracksSource = new String[100];
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		list = new JList();
		list.addListSelectionListener(this);
		player = new MusicPlayer();
		JLabel label = new JLabel("Search Tracks : "/* + getUserName() */);
		label.setFont(fontLabel);
		label.setBounds(10, -15, 250, 75);

		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 60, appU
				.getScreenHeight() - 60));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 40,
				appU.getScreenHeight() - 180);

		this.add(scroll);
		scroll.setVisible(false);
		textField = new JTextField();
		textField.setBounds(700, 7, 250, 40);

		search = new JButton("Search");
		search.setBounds(1000, 7, 80, 40);
		search.addActionListener(this);

		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);

		this.add(label);
		this.add(textField);
		this.add(search);
		this.add(player);
		this.setLayout(null);

	}

	/**
	 * Initializing Search User Interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		title = SearchPan.textField.getText();
		scroll.setVisible(true);

		nameList = null;
		imageMap = null;

		nameList = setNameList(title);
		try {
			imageMap = createImageMap(title);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		list.setListData(nameList);
		list.setCellRenderer(new SearchTrack());
		scroll.setVisible(true);
		tracksLength = this.getTrackLength();
		streamU = this.getTracksStream();
		// tracks = this.getTracks();
	}

	/**
	 * @author Salim
	 * @return JList Renderer
	 */
	public class SearchTrack extends DefaultListCellRenderer {
		String title;
		Font font = new Font("helvitica", Font.BOLD, 24);

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			label.setIcon(imageMap.get(value));
			label.setHorizontalTextPosition(SwingConstants.RIGHT);
			label.setFont(font);
			return label;

		}
	}

	/**
	 * @param title
	 * @return a Map that contains the combination of tracks and it respective
	 *         picture
	 * @throws MalformedURLException
	 */
	private Map<String, ImageIcon> createImageMap(String title)
			throws MalformedURLException {
		Map<String, ImageIcon> map = new HashMap<>();
		ArrayList<Track> tracks = new ArrayList<Track>();
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = new ArrayList<com.zeloon.deezer.domain.Track>();
		tracks = getSearchResult(title);
		tracksDeezer = getSearchResultDeezer(title);
		String artworkUrl;
		for (int i = 0; i < tracks.size(); i++) {
			try {
				artworkUrl = tracks.get(i).getArtworkUrl();
				URL url = new URL(artworkUrl);
				map.put(tracks.get(i).getTitle(), new ImageIcon(url));

			} catch (Exception ex) {
				// Managing Exception when track does'nt have a cover Image
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(tracks.get(i).getTitle(), new ImageIcon(url));
			}
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			try {
				artworkUrl = tracksDeezer.get(i).getAlbum().getCover();
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));
			} catch (Exception ex) {
				// ex.printStackTrace();
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));
			}
		}
		return map;
	}

	/**
	 * @param title
	 * @return the research results
	 */
	public ArrayList<Track> getSearchResult(String title) {

		ArrayList<Track> tracks = new ArrayList<Track>();
		tracks = soundCApi.getTrack(title);
		return tracks;
	}

	public ArrayList<com.zeloon.deezer.domain.Track> getSearchResultDeezer(
			String title) {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = new ArrayList<com.zeloon.deezer.domain.Track>();
		tracks = deezerApi.getTrack(title);
		return tracks;
	}

	/**
	 * @param title
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 */
	public String[] setNameList(String title) {

		ArrayList<Track> tracks = getSearchResult(title);
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getSearchResultDeezer(title);
		String[] nameList = new String[tracks.size() + tracksDeezer.size()];
		for (int i = 0; i < tracks.size(); i++) {
			nameList[i] = tracks.get(i).getTitle();
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			nameList[i + tracks.size()] = tracksDeezer.get(i).getTitle();
			tracksSource[i + tracks.size()] = "Deezer";
		}
		return nameList;
	}

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 */
	private String[] getTracksStream() {
		String[] sc = soundCApi.getStreamUrlSearch(title);
		String[] deez = deezerApi.getStreamUrlSearch(title);
		return (String[]) ArrayUtils.addAll(sc, deez);
	}

	private int[] getTrackLength() {
		int[] sc = soundCApi.getLengthSearch(title);
		int[] deez = deezerApi.getLengthSearch(title);
		return (int[]) ArrayUtils.addAll(sc, deez);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				player.setVisible(true);
				String source = tracksSource[list.getSelectedIndex()];
				player.getTrackToPlayLength(
						tracksLength[list.getSelectedIndex()], source);
				player.getTrackToPlay(streamU[list.getSelectedIndex()]);
			}
		});
	}

}