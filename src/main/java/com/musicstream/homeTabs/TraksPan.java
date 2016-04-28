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
	private JSONObject jsonSC, jsonDZ, jsonSP, jsonSCStream, jsonDZStream,
			jsonSPStream;

	public TraksPan() throws JSONException {
		appU = new AppUtils();
		tracksSource = new String[100];
		jsonReader = new JsonReader();
		try {
			jsonSC = jsonReader
					.readJsonFromUrl("http://localhost:8080/scusertracks");
			jsonDZ = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzusertracks");
			jsonSP = jsonReader
					.readJsonFromUrl("http://localhost:8080/spusertracks");
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
			jsonSPStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/spusertracksstream");
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
		org.json.JSONArray sc, dz, sp, scCover, dzCover, spCover;
		sc = (org.json.JSONArray) jsonSC.get("title");
		dz = (org.json.JSONArray) jsonDZ.get("title");
		sp = (org.json.JSONArray) jsonSP.get("title");
		scCover = (org.json.JSONArray) jsonSC.get("urlCover");
		dzCover = (org.json.JSONArray) jsonDZ.get("urlCover");
		spCover = (org.json.JSONArray) jsonSP.get("urlCover");
		int nbSc = sc.length();
		int nbDz = dz.length();
		int nbSp = sp.length();
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
		for (int i = 0; i < nbSp; i++) {
			try {
				String artworkUrl = spCover.getString(i);
				URL url = new URL(artworkUrl);
				map.put(sp.getString(i), new ImageIcon(url));

			} catch (Exception ex) {
				// ex.printStackTrace();
			}

		}
		return map;
	}

	/**
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 * @throws JSONException
	 */
	private String[] setNameList() throws JSONException {

		// ArrayList<Track> tracks = getUserTracks();
		org.json.JSONArray sc, dz, sp;
		sc = (org.json.JSONArray) jsonSC.get("title");
		dz = (org.json.JSONArray) jsonDZ.get("title");
		sp = (org.json.JSONArray) jsonSP.get("title");
		int nbSc = sc.length();
		int nbDz = dz.length();
		int nbSp = sp.length();
		// ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer =
		// getUserTracksDeezer();
		String[] nameList = new String[nbSc + nbDz + nbSp];
		for (int i = 0; i < nbSc; i++) {
			nameList[i] = sc.getString(i);
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < nbDz; i++) {
			nameList[i + nbSc] = dz.getString(i);
			tracksSource[i + nbSc] = "Deezer";
		}
		for (int i = 0; i < nbSp; i++) {
			nameList[i + nbSc + nbDz] = sp.getString(i);
			tracksSource[i + nbSc + nbDz] = "Spotify";
		}
		return nameList;
	}

	/**
	 * @return User's User name to be displayed
	 */
	private String getUserName() throws JSONException {
		String username = "";
		if (jsonSC.getString("name") != null)
			username = username + jsonSC.getString("name") + "(Soundcloud)";
		if (jsonDZ.getString("name") != null)
			username = username + jsonDZ.getString("name") + "(Deezer)";
		if (jsonSP.getString("name") != null)
			username = username + jsonSP.getString("name") + "(Spotify)";
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
		org.json.JSONArray sc, dz, sp;

		sc = (org.json.JSONArray) jsonSCStream.get("urlStream");
		dz = (org.json.JSONArray) jsonDZStream.get("urlStream");
		sp = (org.json.JSONArray) jsonSPStream.get("urlStream");
		String[] scS = new String[sc.length()];
		String[] dzS = new String[dz.length()];
		String[] spS = new String[sp.length()];
		for (int i = 0; i < sc.length(); i++) {
			scS[i] = sc.getString(i);
		}
		for (int i = 0; i < dz.length(); i++) {
			dzS[i] = dz.getString(i);
		}
		for (int i = 0; i < sp.length(); i++) {
			spS[i] = sp.getString(i);
		}

		return (String[]) ArrayUtils.addAll(
				(String[]) ArrayUtils.addAll(scS, dzS), spS);
	}

	private int[] getTrackLength() throws JSONException {
		org.json.JSONArray sc, dz, sp;

		sc = (org.json.JSONArray) jsonSCStream.get("length");
		dz = (org.json.JSONArray) jsonDZStream.get("length");
		sp = (org.json.JSONArray) jsonSPStream.get("length");
		int[] scS = new int[sc.length()];
		int[] dzS = new int[dz.length()];
		int[] spS = new int[sp.length()];
		for (int i = 0; i < sc.length(); i++) {
			scS[i] = sc.getInt(i);
		}
		for (int i = 0; i < dz.length(); i++) {
			dzS[i] = dz.getInt(i);
		}
		for (int i = 0; i < sp.length(); i++) {
			spS[i] = sp.getInt(i);
		}
		return (int[]) ArrayUtils.addAll((int[]) ArrayUtils.addAll(scS, dzS),
				spS);
	}
}
