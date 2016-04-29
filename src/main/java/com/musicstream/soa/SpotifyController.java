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
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.LibraryTrack;
import com.wrapper.spotify.models.PlaylistTracksInformation;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import de.voidplus.soundcloud.User;

@RestController
public class SpotifyController implements ErrorController {

	Api spotify = Api
			.builder()
			.clientId("b09165e64a9447efafd9b995af73c2ee")
			.clientSecret("bb561155b00945e689fdaa472c3c0bff")
			.accessToken(
					"BQAIL2I4fum347sWW-Mpqzuv6P5O9yZ9Wj5gbvYH5Dqbz3wQh1x-hOHSd_jAG9d7TM0wR_GBxwUk6AN0Mjbu1qnMxw4NglWh0mV2ssSq9UrXuR4_xh5cGo4cJTk2lqY6_0gw850-pKNkcqX8728b1faCq8tFHSg_Kjo1DGQBtlDqh0EewwCuzBCPBCqWOdEjZ6N5lMmKBQAcMtcwB4FQAKWyW3MM-yOUDB8sSv_Gyl4aBTZpa9vCsyC7iX9Ozmd4nAeFyHqGU77wTgj8JZDR6JWwYRbL3BgWKk08yGXy5zkeXcyg6y_XY16Z")
			.build();

	UserRequest userReq = spotify.getUser("salimharris").build();
	private static final String PATH = "/errorSP";

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
			lengths.add(tracks.get(i).getTrack().getDuration());
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
				lengths.add(tracks.get(i).getDuration());
			}
		}
		return (new SpotifyModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/spuserplaylists")
	public SpotifyModel userPlaylists() throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<SimplePlaylist> playlists = spotify
				.getPlaylistsForUser("salimharris").build().get().getItems();
		for (int i = 0; i < playlists.size(); i++) {
			titles.add(playlists.get(i).getName());
			urlCover.add(playlists.get(i).getImages().get(0).getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover));
	}

	/*
	 * @RequestMapping("/spplaylistinfo") public SpotifyModel
	 * playlistInfo(@RequestParam(value = "index") int index) throws
	 * IOException, WebApiException { ArrayList<String> titles = new
	 * ArrayList<>(); ArrayList<String> urlCover = new ArrayList<>();
	 * PlaylistTracksInformation playlist = spotify
	 * .getPlaylistsForUser("salimharris").build().get().getItems()
	 * .get(index).getTracks();
	 * 
	 * }
	 */

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
