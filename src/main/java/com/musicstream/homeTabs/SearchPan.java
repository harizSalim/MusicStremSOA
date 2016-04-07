package com.musicstream.homeTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.musicstream.utils.JsonReader;

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
	// private DeezerApi deezerApi;
	private Font fontLabel;
	private String[] nameList;
	private JList list;
	private JScrollPane scroll = null;
	// private SoundCloudApi soundCApi;
	private String[] streamU;
	private int[] tracksLength;
	private MusicPlayer player;
	private String title;
	private String[] tracksSource;
	private JsonReader jsonReader;
	private JSONObject jsonSC, jsonDZ;

	public SearchPan() {
		appU = new AppUtils();
		tracksSource = new String[100];
		// soundCApi = new SoundCloudApi();
		// deezerApi = new DeezerApi();
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

		jsonReader = new JsonReader();
		try {
			jsonSC = jsonReader
					.readJsonFromUrl("http://localhost:8080/scsearchtracks?search="
							+ title);
			jsonDZ = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzsearchtracks?search="
							+ title);
		} catch (IOException | JSONException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		nameList = null;
		imageMap = null;

		try {
			nameList = setNameList(title);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			imageMap = createImageMap(title);
		} catch (MalformedURLException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		list.setListData(nameList);
		list.setCellRenderer(new SearchTrack());
		scroll.setVisible(true);
		try {
			tracksLength = this.getTrackLength();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			streamU = this.getTracksStream();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	 * @throws JSONException
	 */
	private Map<String, ImageIcon> createImageMap(String title)
			throws MalformedURLException, JSONException {
		Map<String, ImageIcon> map = new HashMap<>();
		// ArrayList<Track> tracks = new ArrayList<Track>();
		// ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = new
		// ArrayList<com.zeloon.deezer.domain.Track>();
		// tracks = getSearchResult(title);
		// tracksDeezer = getSearchResultDeezer(title);
		int nbSc = ((int[]) (jsonSC.get("length"))).length;
		int nbDz = ((int[]) (jsonDZ.get("length"))).length;
		String artworkUrl;
		for (int i = 0; i < nbSc; i++) {
			try {
				String url = ((String[]) jsonSC.get("urlCover"))[i];
				map.put(((String[]) jsonSC.get("title"))[i], new ImageIcon(
						new URL(url)));

			} catch (Exception ex) {
				// Managing Exception when track does'nt have a cover Image
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				map.put(((String[]) jsonSC.get("title"))[i], new ImageIcon(
						new URL(artworkUrl)));
			}
		}
		for (int i = 0; i < nbDz; i++) {
			try {
				artworkUrl = ((String[]) jsonDZ.get("urlCover"))[i];
				URL url = new URL(artworkUrl);
				map.put(((String[]) jsonDZ.get("title"))[i], new ImageIcon(url));
			} catch (Exception ex) {
				// ex.printStackTrace();
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(((String[]) jsonDZ.get("title"))[i], new ImageIcon(url));
			}
		}
		return map;
	}

	/**
	 * @param title
	 * @return the research results
	 */
	/*
	 * public ArrayList<Track> getSearchResult(String title) {
	 * 
	 * ArrayList<Track> tracks = new ArrayList<Track>(); tracks =
	 * soundCApi.getTrack(title); return tracks; }
	 * 
	 * public ArrayList<com.zeloon.deezer.domain.Track> getSearchResultDeezer(
	 * String title) { ArrayList<com.zeloon.deezer.domain.Track> tracks = new
	 * ArrayList<com.zeloon.deezer.domain.Track>(); tracks =
	 * deezerApi.getTrack(title); return tracks; }
	 */

	/**
	 * @param title
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 * @throws JSONException
	 */
	public String[] setNameList(String title) throws JSONException {

		// ArrayList<Track> tracks = getSearchResult(title);
		// ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer =
		// getSearchResultDeezer(title);
		int nbSc = ((int[]) (jsonSC.get("length"))).length;
		int nbDz = ((int[]) (jsonDZ.get("length"))).length;
		String[] nameList = new String[nbSc + nbDz];
		for (int i = 0; i < nbSc; i++) {
			nameList[i] = ((String[]) jsonSC.get("title"))[i];
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < nbDz; i++) {
			nameList[i + nbSc] = ((String[]) jsonDZ.get("title"))[i];
			tracksSource[i + nbSc] = "Deezer";
		}
		return nameList;
	}

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 * @throws JSONException
	 */
	private String[] getTracksStream() throws JSONException {
		String[] sc = (String[]) (jsonSC.get("urlStream"));
		String[] deez = (String[]) (jsonDZ.get("urlStream"));
		return (String[]) ArrayUtils.addAll(sc, deez);
	}

	private int[] getTrackLength() throws JSONException {
		int[] sc = (int[]) (jsonSC.get("length"));
		int[] deez = (int[]) (jsonDZ.get("length"));
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