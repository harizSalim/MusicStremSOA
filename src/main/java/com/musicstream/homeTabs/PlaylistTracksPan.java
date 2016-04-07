package com.musicstream.homeTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import org.apache.commons.lang.ArrayUtils;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Playlist;
import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

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

	public PlaylistTracksPan(Object playlist) {
		this.playlist = playlist;
		appU = new AppUtils();
		tracksSource = new String[100];
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		player = new MusicPlayer();
		nameList = setNameList();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		imageMap = createImageMap(nameList);
		// streamU = this.getTracksStream();
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
	 */
	private Map<String, ImageIcon> createImageMap(String[] list) {
		Map<String, ImageIcon> map = new HashMap<>();
		if (playlist instanceof Playlist) {
			this.title = ((Playlist) playlist).getTitle();
			streamU = this.getTracksStream(playlist);
			ArrayList<Track> tracks = getPlaylistTracks((Playlist) playlist);
			for (int i = 0; i < tracks.size(); i++) {
				try {
					String url = tracks.get(i).getArtworkUrl();

					map.put(tracks.get(i).getTitle(), new ImageIcon(
							new URL(url)));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else {
			ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getPlaylistTracksDeezer((com.zeloon.deezer.domain.Playlist) playlist);
			streamU = this.getTracksStream(playlist);
			for (int i = 0; i < tracksDeezer.size(); i++) {
				try {

					TrackId tID = new TrackId(tracksDeezer.get(i).getId());
					this.title = ((com.zeloon.deezer.domain.Playlist) playlist)
							.getTitle();
					deezerApi.getPreviewTrack(tID);
					String artworkUrl = tracksDeezer.get(i).getAlbum()
							.getCover();
					URL url = new URL(artworkUrl);
					map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));

				} catch (Exception ex) {
					// ex.printStackTrace();
				}
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
	 */
	private String[] setNameList() {
		String[] nameListTemp;
		if (playlist instanceof Playlist) {
			ArrayList<Track> tracks = getPlaylistTracks((Playlist) playlist);
			nameListTemp = new String[tracks.size()];
			for (int i = 0; i < tracks.size(); i++) {
				nameListTemp[i] = tracks.get(i).getTitle();
				// System.out.println(tracks.get(i).getTitle());
				tracksSource[i] = "Soundcloud";
			}
		} else {
			ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getPlaylistTracksDeezer((com.zeloon.deezer.domain.Playlist) playlist);
			nameListTemp = new String[tracksDeezer.size()];
			for (int i = 0; i < tracksDeezer.size(); i++) {
				nameListTemp[i] = tracksDeezer.get(i).getTitle();
				tracksSource[i] = "Deezer";
			}
		}

		return nameListTemp;
	}

	/**
	 * @return User's Name
	 */
	private String getUserName() {
		User UserData = soundCApi.getUser();
		return UserData.getUsername();
	}

	/**
	 * @param playlist
	 * @return string containing the stream url of the playlis's Tracks
	 */
	private String[] getTracksStream(Object playlist) {
		if (playlist instanceof Playlist) {
			return (soundCApi.getStreamUrlPlaylist(playlist));
		} else {
			return deezerApi.getStreamUrlPlaylist(playlist);
		}

	}

	private int[] getTrackLength() {
		int[] sc = soundCApi.getLength();
		int[] deez = deezerApi.getLength();
		return (int[]) ArrayUtils.addAll(sc, deez);
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
