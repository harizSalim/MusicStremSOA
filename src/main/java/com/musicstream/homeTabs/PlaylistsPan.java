package com.musicstream.homeTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.musicstream.utils.AppUtils;
import com.musicstream.utils.JsonReader;

/**
 * @author Malek
 * 
 */
public class PlaylistsPan extends JPanel implements ListSelectionListener {
	private final Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	Font fontLabel;
	private JList list;
	private String[] tracksSource;
	private JsonReader jsonReader;
	private JSONObject jsonSC, jsonDZ, jsonSP;
	int nbSc = 0, nbDz = 0, nbSp = 0;

	public PlaylistsPan() throws JSONException {
		appU = new AppUtils();
		jsonReader = new JsonReader();
		tracksSource = new String[100];
		try {
			jsonSC = jsonReader
					.readJsonFromUrl("http://localhost:8080/scuserplaylists");
			jsonDZ = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzuserplaylists");
			jsonSP = jsonReader
					.readJsonFromUrl("http://localhost:8080/spuserplaylists");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] nameList = setNameList();
		imageMap = createImageMap(nameList);
		fontLabel = new Font("helvitica", Font.BOLD, 18);

		list = new JList(nameList);
		list.setCellRenderer(new playlistsListRenderer());
		list.addListSelectionListener(this);

		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 30, appU
				.getScreenHeight() - 30));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 30,
				appU.getScreenHeight() - 180);

		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setFont(fontLabel);
		label.setBounds(10, -15, 250, 75);

		this.add(label);
		this.add(scroll);
		this.setLayout(null);

	}

	/**
	 * @author Malek JList Renderer
	 */
	private class playlistsListRenderer extends DefaultListCellRenderer {

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
	 * @return a Map that contains the combination of the playlist name and it
	 *         respective picture
	 * @throws JSONException
	 */
	private Map<String, ImageIcon> createImageMap(String[] list)
			throws JSONException {
		Map<String, ImageIcon> map = new HashMap<>();

		org.json.JSONArray sc, dz, sp, scCover, dzCover, spCover;
		sc = (org.json.JSONArray) jsonSC.get("playlistName");
		dz = (org.json.JSONArray) jsonDZ.get("playlistName");
		sp = (org.json.JSONArray) jsonSP.get("playlistName");
		scCover = (org.json.JSONArray) jsonSC.get("playlistUrlCover");
		dzCover = (org.json.JSONArray) jsonDZ.get("playlistUrlCover");
		spCover = (org.json.JSONArray) jsonSP.get("playlistUrlCover");
		int nbSc = sc.length();
		int nbDz = dz.length();
		int nbSp = sp.length();

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
				String artworkUrl = dzCover.getString(i);
				URL url = new URL(artworkUrl);
				map.put(dz.getString(i), new ImageIcon(url));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < nbSp; i++) {
			try {
				String artworkUrl = spCover.getString(i);
				URL url = new URL(artworkUrl);
				ImageIcon img = new ImageIcon(url);
				Image imag = img.getImage();
				Image newimg = imag.getScaledInstance(120, 120,
						java.awt.Image.SCALE_SMOOTH);
				map.put(sp.getString(i), new ImageIcon(newimg));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * @return Array that contains the names of the playlists to be associated
	 *         with their images and to be displayed in the JList
	 * @throws JSONException
	 */
	private String[] setNameList() throws JSONException {

		org.json.JSONArray sc, dz, sp;
		sc = (org.json.JSONArray) jsonSC.get("playlistName");
		dz = (org.json.JSONArray) jsonDZ.get("playlistName");
		sp = (org.json.JSONArray) jsonSP.get("playlistName");
		nbSc = sc.length();
		nbDz = dz.length();
		nbSp = sp.length();

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
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			if (tracksSource[list.getSelectedIndex()].equals("Soundcloud")) {
				int index = list.getSelectedIndex();
				try {
					PlaylistTracksPan playlistTracks = new PlaylistTracksPan(
							index, "Soundcloud");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (tracksSource[list.getSelectedIndex()].equals("Deezer")) {
				int index = list.getSelectedIndex() - nbSc;
				try {
					PlaylistTracksPan playlistTracks = new PlaylistTracksPan(
							index, "Deezer");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			if (tracksSource[list.getSelectedIndex()].equals("Spotify")) {
				int index = list.getSelectedIndex() - nbSc - nbDz;
				try {
					PlaylistTracksPan playlistTracks = new PlaylistTracksPan(
							index, "Spotify");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
