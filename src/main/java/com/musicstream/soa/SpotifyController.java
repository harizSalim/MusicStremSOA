package com.musicstream.soa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.LibraryTrack;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

@RestController
public class SpotifyController implements ErrorController {

	Api spotify = Api
			.builder()
			.clientId("b09165e64a9447efafd9b995af73c2ee")
			.clientSecret("bb561155b00945e689fdaa472c3c0bff")
			.accessToken(
					"BQCONpSSOUe7y8njodgiX_ZqMXMNiVZ6HyaUorZZF-rbYGmxsrI-d05HOCTkMPluXuw9_1RxgipRcG-WnsZBkxPzcDMH1dleeON04LR5EEISYLSogzZ7XtrlRF_uxwx3ifg3FabATJt_K61YsrdmpH7sncdD9d7SeIyreT-1X-Nee4eh_pko0fq_tghlcBbszxzHAvk9ECbmDoNHKHQim7AMM-7_dzKKnvQ1-oeYilN2evJw0z3u3W-8WgV2BM54O35nzehAdHI6-bH9JxmQR9RGUHcvtn4zjm7zsLM4rO9kGBQTvo0UZ0l5")
			.build();

	String userID = "salimharris";
	UserRequest userReq = spotify.getUser(userID).build();
	private static final String PATH = "/errorSP";
	List<SimplePlaylist> simplePlay = new ArrayList<SimplePlaylist>();

	@RequestMapping("/spusertracks")
	public SpotifyModel userTracks() throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<LibraryTrack> tracks = spotify.getMySavedTracks().build().get()
				.getItems();
		for (int i = 0; i < tracks.size(); i++) {
			titles.add(tracks.get(i).getTrack().getName());
			urlCover.add(tracks.get(i).getTrack().getAlbum().getImages().get(0)
					.getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/spusertracksstream")
	public SpotifyModel userTracksStream() throws IOException, WebApiException {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		List<LibraryTrack> tracks = spotify.getMySavedTracks().build().get()
				.getItems();
		for (int i = 0; i < tracks.size(); i++) {
			urlStream.add(tracks.get(i).getTrack().getPreviewUrl());
			lengths.add(30);
		}
		return (new SpotifyModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/spsearchtracks")
	public SpotifyModel searchTracks(
			@RequestParam(value = "search") String search) throws IOException,
			WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<Track> tracks = spotify.searchTracks(search).build().get()
				.getItems();
		if (tracks.size() > 0) {
			for (int i = 0; i < 5; i++) {
				titles.add(tracks.get(i).getName());
				urlCover.add(tracks.get(i).getAlbum().getImages().get(0)
						.getUrl());
			}
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/spsearchtracksstream")
	public SpotifyModel searchTracksStream(
			@RequestParam(value = "search") String search) throws IOException,
			WebApiException {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		List<Track> tracks = spotify.searchTracks(search).build().get()
				.getItems();
		if (tracks.size() > 0) {
			for (int i = 0; i < 5; i++) {
				urlStream.add(tracks.get(i).getPreviewUrl());
				lengths.add(30);
			}
		}
		return (new SpotifyModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/spuserplaylists")
	public SpotifyModel userPlaylists() throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<SimplePlaylist> playlists = spotify.getPlaylistsForUser(userID)
				.build().get().getItems();
		for (int i = 0; i < playlists.size(); i++) {
			if (spotify.getPlaylistsForUser(userID).build().get().getItems()
					.get(i).getOwner().getId().equals(userID)) {
				simplePlay.add(playlists.get(i));
				titles.add(playlists.get(i).getName());
				try {
					urlCover.add(playlists.get(i).getImages().get(0).getUrl());
				} catch (Exception ex) {
					urlCover.add("https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg");
				}
			}
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover));
	}

	@RequestMapping("/spplaylistinfo")
	public SpotifyModel playlistInfo(@RequestParam(value = "index") int index)
			throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		String id = simplePlay.get(index).getId();
		PlaylistTracksRequest req = spotify.getPlaylistTracks(userID, id)
				.build();
		List<PlaylistTrack> tracks = req.get().getItems();
		for (int i = 0; i < tracks.size(); i++) {
			titles.add(tracks.get(i).getTrack().getName());
			urlCover.add(tracks.get(i).getTrack().getAlbum().getImages().get(0)
					.getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/spplaylistinfoStream")
	public SpotifyModel playlistInfoStream(
			@RequestParam(value = "index") int index) throws IOException,
			WebApiException {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		String id = simplePlay.get(index).getId();
		List<PlaylistTrack> tracks = spotify.getPlaylistTracks(userID, id)
				.build().get().getItems();
		for (int i = 0; i < tracks.size(); i++) {
			urlStream.add(tracks.get(i).getTrack().getPreviewUrl());
			lengths.add(30);
		}
		return (new SpotifyModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "Error handling Spotify";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return PATH;
	}

}
