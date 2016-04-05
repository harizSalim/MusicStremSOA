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

import com.musicstream.api.DeezerApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.musicstream.utils.JsonReader;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

/**
 * @author Malek Jpanel that contains the list of user's tracks
 */
public class TraksPan extends JPanel implements ListSelectionListener {
	private final Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	// private SoundCloudApi soundCApi;
	private DeezerApi deezerApi;
	private MusicPlayer player;
	private Font fontLabel;
	private JList list;
	private String[] nameList;
	private String[] streamU;
	private int[] tracksLength;
	private String[] tracksSource;
	private JsonReader jsonReader;
	private JSONObject jsonSC;

	public TraksPan() throws JSONException {
		appU = new AppUtils();
		tracksSource = new String[100];
		jsonReader = new JsonReader();
		try {
			jsonSC = jsonReader
					.readJsonFromUrl("http://localhost:8080/scusertracks");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		player = new MusicPlayer();
		nameList = setNameList();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		imageMap = createImageMap(nameList);
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

		JLabel label = new JLabel("Welcome : "/* + getUserName() */);
		label.setBounds(10, -15, 250, 75);
		label.setFont(fontLabel);

		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);

		this.add(label);
		this.add(scroll);
		this.add(player);
		this.setLayout(null);

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
	private Map<String, ImageIcon> createImageMap(String[] list) throws JSONException {
		Map<String, ImageIcon> map = new HashMap<>();
		// ArrayList<Track> tracks = getUserTracks();
		int nbSc = ((int[]) (jsonSC.get("length"))).length;
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getUserTracksDeezer();
		for (int i = 0; i < nbSc; i++) {
			try {
				String url = ((String[]) jsonSC.get("urlCover"))[i];
				map.put(((String[]) jsonSC.get("title"))[i], new ImageIcon(
						new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			try {

				TrackId tID = new TrackId(tracksDeezer.get(i).getId());
				deezerApi.getPreviewTrack(tID);
				String artworkUrl = tracksDeezer.get(i).getAlbum().getCover();
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));

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

	private ArrayList<com.zeloon.deezer.domain.Track> getUserTracksDeezer() {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi
				.getTracksByUser();
		return tracks;
	}

	/**
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 * @throws JSONException
	 */
	private String[] setNameList() throws JSONException {

		// ArrayList<Track> tracks = getUserTracks();
		int nbSc = ((int[]) (jsonSC.get("length"))).length;
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getUserTracksDeezer();
		String[] nameList = new String[nbSc + tracksDeezer.size()];
		for (int i = 0; i < nbSc; i++) {
			nameList[i] = ((String[]) jsonSC.get("title"))[i];
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			nameList[i + nbSc] = tracksDeezer.get(i).getTitle();
			tracksSource[i + nbSc] = "Deezer";
		}
		return nameList;
	}

	/**
	 * @return User's User name to be displayed
	 */
	/*
	 * private String getUserName() { User UserData = soundCApi.getUser();
	 * return UserData.getUsername(); }
	 */

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

		// if (currentThreadId == 0) { // CHeck if it the first Thread to be
		// created
		// player.setVisible(true);
		// player.getTrackToPlay(streamU[list.getSelectedIndex()]);

		/*
		 * t = new Thread(new Runnable() { public void run() {
		 * appU.readAudioFeed(streamU[list.getSelectedIndex()]); }
		 * 
		 * }); t.start(); } else { // If it is not the first Thread created, we
		 * stop the first one // and start a new Thread to listen to the
		 * selected Song (To // Avoid playing all the songs Simultaneously)
		 * t.stop(); t = new Thread(new Runnable() { public void run() {
		 * appU.readAudioFeed(streamU[list.getSelectedIndex()]); //
		 * currentThreadId=t.currentThread().getId(); } }); t.start();
		 */
		// }
		// currentThreadId++;

	}

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 * @throws JSONException
	 */
	private String[] getTracksStream() throws JSONException {
		String[] sc = (String[]) (jsonSC.get("urlStream"));
		String[] deez = deezerApi.getStreamUrl();
		return (String[]) ArrayUtils.addAll(sc, deez);
	}

	private int[] getTrackLength() throws JSONException {
		int[] sc = (int[]) (jsonSC.get("length"));
		int[] deez = deezerApi.getLength();
		return (int[]) ArrayUtils.addAll(sc, deez);
	}
}
