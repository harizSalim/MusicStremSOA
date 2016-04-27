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
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import de.voidplus.soundcloud.User;

@RestController
public class SpotifyController implements ErrorController {
	
	final Api spotify = Api.builder().clientId("b09165e64a9447efafd9b995af73c2ee")
			.clientSecret("bb561155b00945e689fdaa472c3c0bff")
			.accessToken(
					"BQAAuP8TGF10qKD5nrIBPYhDXczHaBJ_00gvliV0-NQWq7xr63UIJQi6Va6T9BgGPAhOwXKqilPY_Fwxrn1AIf1PFNfQWqRlEzuK9Ong277slhAw_dd0cJHAgelWzFkyxztw-J5UylREqgHtJceNn76dK1IGj82e5IuqU6s6TWrHq9qlkd4-018qJZiKxInlK_G5lFfJagUa8BDSSmU7dXat-QK0bO6_Slj9jOBkfWdu5gcd41pPfa2Be0PKqp91vys88BjRiw2morYiWrDLKjFRGAS__7i33YhNao3kr6DxuqGT-tUtb1z_")
			.build();
	final UserRequest userReq = spotify.getUser("salimharris").build();
	private static final String PATH = "/errorSP";

	@RequestMapping("/spusertracks")
	public SpotifyModel userTracks() throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<LibraryTrack> tracks = spotify.getMySavedTracks().build().get().getItems();
		for (int i = 0; i < tracks.size(); i++) {
			titles.add(tracks.get(i).getTrack().getName());
			urlCover.add(tracks.get(i).getTrack().getAlbum().getImages().get(0).getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles, urlCover, null, null));
	}

	@RequestMapping("/spusertracksstream")
	public SpotifyModel userTracksStream() throws IOException, WebApiException {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		List<LibraryTrack> tracks = spotify.getMySavedTracks().build().get().getItems();
		for (int i = 0; i < tracks.size(); i++) {
			urlStream.add(tracks.get(i).getTrack().getPreviewUrl());
			lengths.add(tracks.get(i).getTrack().getDuration());
		}
		return (new SpotifyModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/spsearchtracks")
	public SpotifyModel searchTracks(@RequestParam(value = "search") String search)
			throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		List<Track> tracks = spotify.searchTracks(search).build().get().getItems();
		if (tracks.size() > 0) {
			for (int i = 0; i < 5; i++) {
				titles.add(tracks.get(i).getName());
				urlCover.add(tracks.get(i).getAlbum().getImages().get(0).getUrl());
			}
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles, urlCover, null, null));
	}

	@RequestMapping("/spsearchtracksstream")
	public SpotifyModel searchTracksStream(@RequestParam(value = "search") String search)
			throws IOException, WebApiException {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		List<Track> tracks = spotify.searchTracks(search).build().get().getItems();
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
		List<SimplePlaylist> playlists = spotify.getPlaylistsForUser("salimharris").build().get().getItems();
		for (int i = 0; i < playlists.size(); i++) {
			titles.add(playlists.get(i).getName());
			urlCover.add(playlists.get(i).getImages().get(0).getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles, urlCover));
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
