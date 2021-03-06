package com.musicstream.homeTabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
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
	private Font fontLabel;
	private String[] nameList;
	private JList list;
	private JScrollPane scroll = null;
	private String[] streamU;
	private int[] tracksLength;
	private MusicPlayer player;
	private String title;
	private String[] tracksSource;
	private JsonReader jsonReader;
	private JSONObject jsonSC, jsonDZ, jsonSP, jsonSCStream, jsonDZStream,
			jsonSPStream;

	public SearchPan() {
		appU = new AppUtils();
		fontLabel = new Font("helvitica", Font.BOLD, 18);
		list = new JList();
		list.addListSelectionListener(this);
		player = new MusicPlayer();
		JLabel label = new JLabel("Search Tracks : ");
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
		textField.setBounds(700, 5, 350, 200);
		textField.setPreferredSize(new Dimension(100, 30));
		search = new JButton("Search");
		search.setBounds(1000, 7, 80, 40);
		search.addActionListener(this);

		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		JPanel headPanel = new JPanel();
		headPanel.setLayout(new FlowLayout());
		headPanel.add(label);
		headPanel.add(textField);
		headPanel.add(search);
		this.add(headPanel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(player, BorderLayout.SOUTH);

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
			jsonSP = jsonReader
					.readJsonFromUrl("http://localhost:8080/spsearchtracks?search="
							+ title);
			nameList = null;
			imageMap = null;
			nameList = setNameList(title);
			imageMap = createImageMap(title);
		} catch (JSONException | IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		list.setListData(nameList);
		list.setCellRenderer(new SearchTrack());
		scroll.setVisible(true);

		try {
			jsonSCStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/scsearchtracksstream?search="
							+ title);
			jsonDZStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/dzsearchtracksstream?search="
							+ title);
			jsonSPStream = jsonReader
					.readJsonFromUrl("http://localhost:8080/spsearchtracksstream?search="
							+ title);
			streamU = this.getTracksStream();
			tracksLength = this.getTrackLength();
		} catch (IOException | JSONException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
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
		String artworkUrl;
		for (int i = 0; i < nbSc; i++) {
			try {
				String url = scCover.getString(i);
				map.put(sc.getString(i), new ImageIcon(new URL(url)));

			} catch (Exception ex) {
				// Managing Exception when track does'nt have a cover Image
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				map.put(sc.getString(i), new ImageIcon(new URL(artworkUrl)));
			}
		}
		for (int i = 0; i < nbDz; i++) {
			try {
				artworkUrl = dzCover.getString(i);
				URL url = new URL(artworkUrl);
				map.put(dz.getString(i), new ImageIcon(url));
			} catch (Exception ex) {
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(dz.getString(i), new ImageIcon(url));
			}
		}
		for (int i = 0; i < nbSp; i++) {
			try {
				artworkUrl = spCover.getString(i);
				URL url = new URL(artworkUrl);
				ImageIcon img = new ImageIcon(url);
				Image imag = img.getImage();
				Image newimg = imag.getScaledInstance(120, 120,
						java.awt.Image.SCALE_SMOOTH);
				map.put(sp.getString(i), new ImageIcon(newimg));
			} catch (Exception ex) {
				// ex.printStackTrace();
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(sp.getString(i), new ImageIcon(url));
			}
		}
		return map;
	}

	/**
	 * @param title
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 * @throws JSONException
	 */
	public String[] setNameList(String title) throws JSONException {

		org.json.JSONArray sc, dz, sp;
		sc = (org.json.JSONArray) jsonSC.get("title");
		dz = (org.json.JSONArray) jsonDZ.get("title");
		sp = (org.json.JSONArray) jsonSP.get("title");
		int nbSc = sc.length();
		int nbDz = dz.length();
		int nbSp = sp.length();
		String[] nameList = new String[nbSc + nbDz + nbSp];
		tracksSource = new String[nbSc + nbDz + nbSp];
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
				int index = list.getSelectedIndex();
				String source = tracksSource[index];
				player.setVisible(true);
				player.getTrackToPlayLength(tracksLength[index], source);
				player.getTrackToPlay(streamU[index]);
			}
		});
	}

}