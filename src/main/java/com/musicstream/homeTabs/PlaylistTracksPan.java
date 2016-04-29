package com.musicstream.homeTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.musicstream.utils.JsonReader;

import de.voidplus.soundcloud.Playlist;
import de.voidplus.soundcloud.Track;

public class PlaylistTracksPan extends JFrame implements ListSelectionListener {

	private final Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	private SoundCloudApi soundCApi;
	private DeezerApi deezerApi;
	private MusicPlayer player;
	private Font fontLabel;
	private JList list;
	private String[] nameList;
	private String[] streamU;
	private int[] tracksLength;
	private String[] tracksSource;
	private Object playlist;
	private JScrollPane scroll;
	private String title = "";
	private JsonReader jsonReader;
	private JSONObject jsonInfo, jsonInfoStream;
	String source;

	public PlaylistTracksPan(int index, String source) throws JSONException {
		// this.playlist = playlist;
		this.source = source;
		if (source.equals("Soundcloud")) {
			try {
				jsonInfo = jsonReader
						.readJsonFromUrl("http://localhost:8080/scplaylistinfo?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (source.equals("Deezer")) {
			try {
				jsonInfo = jsonReader
						.readJsonFromUrl("http://localhost:8080/dzplaylistinfo?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*if (source.equals("Spotify")) {
			try {
				jsonInfo = jsonReader
						.readJsonFromUrl("http://localhost:8080/spplaylistinfo?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		appU = new AppUtils();
		tracksSource = new String[100];
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		player = new MusicPlayer();
		nameList = setNameList();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		imageMap = createImageMap(nameList);

		if (source.equals("Soundcloud")) {
			try {
				jsonInfoStream = jsonReader
						.readJsonFromUrl("http://localhost:8080/scplaylistinfoStream?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (source.equals("Deezer")) {
			try {
				jsonInfoStream = jsonReader
						.readJsonFromUrl("http://localhost:8080/dzplaylistinfoStream?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*if (source.equals("Spotify")) {
			try {
				jsonInfoStream = jsonReader
						.readJsonFromUrl("http://localhost:8080/spplaylistinfoStream?index="
								+ index);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

		// streamU = this.getTracksStream();
		tracksLength = this.getTrackLength();

		list = new JList(nameList);
		list.setCellRenderer(new tracksListRenderer());
		list.addListSelectionListener(this);

		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 60, appU
				.getScreenHeight() - 60));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 40,
				appU.getScreenHeight() - 180);

		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setBounds(10, -15, 250, 75);
		label.setFont(fontLabel);

		this.setSize(appU.getScreenWidth(), appU.getScreenHeight());
		this.setVisible(true);

		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);

		this.add(label);
		this.add(scroll);
		this.add(player);
	}

	/**
	 * @param list
	 * @return : a Map that contains the combination of the playlist name and
	 *         its picture
	 * @throws JSONException
	 */
	private Map<String, ImageIcon> createImageMap(String[] list)
			throws JSONException {
		Map<String, ImageIcon> map = new HashMap<>();
		org.json.JSONArray tr, trCover;
		tr = (org.json.JSONArray) jsonInfo.get("title");
		trCover = (org.json.JSONArray) jsonInfo.get("urlCover");
		int nb = tr.length();

		for (int i = 0; i < nb; i++) {
			try {
				String url = trCover.getString(i);
				map.put(tr.getString(i), new ImageIcon(new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return map;
	}

	private ArrayList<com.zeloon.deezer.domain.Track> getPlaylistTracksDeezer(
			com.zeloon.deezer.domain.Playlist pl) {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi
				.getTracksofPlaylist(pl);
		return tracks;
	}

	private ArrayList<Track> getPlaylistTracks(Playlist playlist) {
		ArrayList<Track> tracks = soundCApi.getTracksofPlaylist(playlist);
		return tracks;
	}

	/**
	 * @return Array that contains the names of the playlist's tracks
	 * @throws JSONException
	 */
	private String[] setNameList() throws JSONException {
		org.json.JSONArray tr, trCover;
		tr = (org.json.JSONArray) jsonInfo.get("title");
		trCover = (org.json.JSONArray) jsonInfo.get("urlCover");
		int nb = tr.length();
		String[] nameListTemp = new String[nb];
		for (int i = 0; i < nb; i++) {
			nameListTemp[i] = tr.getString(i);
			tracksSource[i] = source;
		}
		return nameListTemp;
	}

	/**
	 * @return User's Name
	 * @throws JSONException
	 */
	private String getUserName() throws JSONException {
		String username = "";
		username = username + jsonInfo.getString("name") + source;
		return username;
	}

	/**
	 * @param playlist
	 * @return string containing the stream url of the playlis's Tracks
	 * @throws JSONException
	 */
	private String[] getTracksStream(Object playlist) throws JSONException {
		org.json.JSONArray tr;
		tr = (org.json.JSONArray) jsonInfoStream.get("urlStream");
		String[] trS = new String[tr.length()];
		for (int i = 0; i < tr.length(); i++) {
			trS[i] = tr.getString(i);
		}
		return trS;
	}

	private int[] getTrackLength() throws JSONException {
		org.json.JSONArray tr;
		tr = (org.json.JSONArray) jsonInfoStream.get("length");
		int[] trS = new int[tr.length()];
		for (int i = 0; i < tr.length(); i++) {
			trS[i] = tr.getInt(i);
		}
		return trS;
	}

	/**
	 * @author Malek JList Renderer
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
}
