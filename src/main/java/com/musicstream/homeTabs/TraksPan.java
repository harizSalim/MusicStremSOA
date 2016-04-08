package com.musicstream.homeTabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
 * @author Malek Jpanel that contains the list of user's tracks
 */
public class TraksPan extends JPanel implements ListSelectionListener {
	private final Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	// private SoundCloudApi soundCApi;
	// private DeezerApi deezerApi;
	private MusicPlayer player;
	private Font fontLabel;
	private JList list;
	private String[] nameList;
	private String[] streamU;
	private int[] tracksLength;
	private String[] tracksSource;
	private JsonReader jsonReader;
	private JSONObject jsonSC, jsonDZ, jsonSCStream, jsonDZStream;

	public TraksPan() throws JSONException {
		appU = new AppUtils();
		tracksSource = new String[100];
		jsonReader = new JsonReader();
		try {
			jsonSC = jsonReader
					.readJsonFromUrl("http://localhost:8080/scusertracks");
			jsonDZ = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzusertracks");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// soundCApi = new SoundCloudApi();
		// deezerApi = new DeezerApi();
		player = new MusicPlayer();
		nameList = setNameList();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		imageMap = createImageMap(nameList);

		try {
			jsonSCStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/scusertracksstream");
			jsonDZStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzusertracksstream");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		streamU = this.getTracksStream();
		tracksLength = this.getTrackLength();

		list = new JList(nameList);
		list.setCellRenderer(new tracksListRenderer());
		list.addListSelectionListener(this);

		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 60, appU
				.getScreenHeight() - 60));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 40,
				appU.getScreenHeight() - 180);

		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setBounds(10, -15, 250, 75);
		label.setFont(fontLabel);

		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.add(label, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(player, BorderLayout.SOUTH);

	}

	/**
	 * @author Malek
	 * 
	 */
	private class tracksListRenderer extends DefaultListCellRenderer {

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
	 * @param list
	 * @return a Map that contains the combination of the track name and it
	 *         respective picture
	 * @throws JSONException
	 */
	private Map<String, ImageIcon> createImageMap(String[] list)
			throws JSONException {
		Map<String, ImageIcon> map = new HashMap<>();
		org.json.JSONArray sc, dz, scCover, dzCover;
		sc = (org.json.JSONArray) jsonSC.get("title");
		dz = (org.json.JSONArray) jsonDZ.get("title");
		scCover = (org.json.JSONArray) jsonSC.get("urlCover");
		dzCover = (org.json.JSONArray) jsonDZ.get("urlCover");
		int nbSc = sc.length();
		int nbDz = dz.length();
		// ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer =
		// getUserTracksDeezer();
		for (int i = 0; i < nbSc; i++) {
			try {
				String url = scCover.getString(i);
				map.put(sc.getString(i), new ImageIcon(new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < nbDz; i++) {
			try {

				// TrackId tID = new TrackId(tracksDeezer.get(i).getId());
				// deezerApi.getPreviewTrack(tID);
				String artworkUrl = dzCover.getString(i);
				URL url = new URL(artworkUrl);
				map.put(dz.getString(i), new ImageIcon(url));

			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * @return User's Tracks
	 */
	/*
	 * private ArrayList<Track> getUserTracks() {
	 * 
	 * ArrayList<Track> tracks = soundCApi.getTracksByUser(); return tracks; }
	 */

	/*
	 * private ArrayList<com.zeloon.deezer.domain.Track> getUserTracksDeezer() {
	 * ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi
	 * .getTracksByUser(); return tracks; }
	 */

	/**
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 * @throws JSONException
	 */
	private String[] setNameList() throws JSONException {

		// ArrayList<Track> tracks = getUserTracks();
		org.json.JSONArray sc, dz;
		sc = (org.json.JSONArray) jsonSC.get("title");
		dz = (org.json.JSONArray) jsonDZ.get("title");
		int nbSc = sc.length();
		int nbDz = dz.length();
		// ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer =
		// getUserTracksDeezer();
		String[] nameList = new String[nbSc + nbDz];
		for (int i = 0; i < nbSc; i++) {
			nameList[i] = sc.getString(i);
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < nbDz; i++) {
			nameList[i + nbSc] = dz.getString(i);
			tracksSource[i + nbSc] = "Deezer";
		}
		return nameList;
	}

	/**
	 * @return User's User name to be displayed
	 */
	private String getUserName() throws JSONException {
		String username = "";
		org.json.JSONArray sc, dz;
		sc = (org.json.JSONArray) jsonSC.get("name");
		dz = (org.json.JSONArray) jsonDZ.get("name");
		if (sc.getString(0) != null)
			username = username + sc.getString(0) + "(Soundcloud)";
		if (dz.getString(0) != null)
			username = username + dz.getString(0) + "(Deezer)";
		return username;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				int index = list.getSelectedIndex();
				String source = tracksSource[index];
				player.setVisible(true);
				player.getTrackToPlayLength(tracksLength[index], source);
				player.getTrackToPlay(streamU[index]);
			}
		});
	}

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 * @throws JSONException
	 */
	private String[] getTracksStream() throws JSONException {
		org.json.JSONArray sc, dz;

		sc = (org.json.JSONArray) jsonSCStream.get("urlStream");
		dz = (org.json.JSONArray) jsonDZStream.get("urlStream");
		String[] scS = new String[sc.length()];
		String[] dzS = new String[dz.length()];
		for (int i = 0; i < sc.length(); i++) {
			scS[i] = sc.getString(i);
		}
		for (int i = 0; i < dz.length(); i++) {
			dzS[i] = dz.getString(i);
		}

		return (String[]) ArrayUtils.addAll(scS, dzS);
	}

	private int[] getTrackLength() throws JSONException {
		org.json.JSONArray sc, dz;

		sc = (org.json.JSONArray) jsonSCStream.get("length");
		dz = (org.json.JSONArray) jsonDZStream.get("length");
		int[] scS = new int[sc.length()];
		int[] dzS = new int[dz.length()];
		for (int i = 0; i < sc.length(); i++) {
			scS[i] = sc.getInt(i);
		}
		for (int i = 0; i < dz.length(); i++) {
			dzS[i] = dz.getInt(i);
		}
		return (int[]) ArrayUtils.addAll(scS, dzS);
	}
}
