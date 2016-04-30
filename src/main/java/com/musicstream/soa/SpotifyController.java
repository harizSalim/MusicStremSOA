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
import com.zeloon.deezer.domain.Tracks;

@RestController
public class SpotifyController implements ErrorController {

	Api spotify = Api
			.builder()
			.clientId("b09165e64a9447efafd9b995af73c2ee")
			.clientSecret("bb561155b00945e689fdaa472c3c0bff")
			.accessToken(
					"BQB_PNli1VCD-iHhyTxJNsCocdDw4cmhKWuNDbLo53vK5QdSS-oP_ue5QWk3rNNEsXECQ1XfHZprHiL7OvsFqmw5eDKR4htAgAL56MedFyFLkBvoAePmen9f6LN1z6T498ZSgK6pkgEWN-0K6J5eRK5EIKipog5raC7FEHozmqZ3QeNhkURqYqxWaUSGgeGsZzkzy8esb2U_SfVNpP9qJygs7YXT7d3dlRp3s43sX9RJBD8A-7kHFtuVUJskqVwd_yOP9JB90W-YGWXopG3N0MVVJNYPM24iJoeS9Kj2Nk2pwZt7eypyjjPl")
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
		List<SimplePlaylist> playlists = spotify
				.getPlaylistsForUser("salimharris").build().get().getItems();
		for (int i = 0; i < playlists.size(); i++) {
			titles.add(playlists.get(i).getName());
			urlCover.add(playlists.get(i).getImages().get(0).getUrl());
		}
		return (new SpotifyModel(userReq.get().getDisplayName(), titles,
				urlCover));
	}

	@RequestMapping("/spplaylistinfo")
	public SpotifyModel playlistInfo(@RequestParam(value = "index") int index)
			throws IOException, WebApiException {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		String id = spotify.getPlaylistsForUser("salimharris").build().get()
				.getItems().get(index).getId();
		PlaylistTracksRequest req = spotify.getPlaylistTracks(
				userReq.get().getId(), id).build();
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
		String id = spotify.getPlaylistsForUser("salimharris").build().get()
				.getItems().get(index).getId();
		List<PlaylistTrack> tracks = spotify
				.getPlaylistTracks(userReq.get().getId(), id).build().get()
				.getItems();
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
