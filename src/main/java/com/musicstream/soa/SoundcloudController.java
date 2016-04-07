package com.musicstream.soa;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.User;

@RestController
public class SoundcloudController implements ErrorController {
	private SoundCloud soundcloud = new SoundCloud(
			"481ca2dc5f8ff18044c77239659a5b59",
			"1067035a0baee3932e79847bae144fa1", "testing.kamel@gmail.com",
			"azerty123");
	private static final String PATH = "/error";

	@RequestMapping("/scusertracks")
	public SoundcloudModel userTracks() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			titles.add(soundcloud.getMeFavorites().get(i).getTitle());
			urlCover.add(soundcloud.getMeFavorites().get(i).getArtworkUrl());
			// urlStream.add(soundcloud.getMeFavorites().get(i).getStreamUrl());
			// lengths.add(soundcloud.getMeFavorites().get(i).getDuration());
		}
		return (new SoundcloudModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/scusertracksstream")
	public SoundcloudModel userTracksStream() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			// titles.add(soundcloud.getMeFavorites().get(i).getTitle());
			// urlCover.add(soundcloud.getMeFavorites().get(i).getArtworkUrl());
			urlStream.add(soundcloud.getMeFavorites().get(i).getStreamUrl());
			lengths.add(soundcloud.getMeFavorites().get(i).getDuration());
		}
		return (new SoundcloudModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/scsearchtracks")
	public SoundcloudModel searchTracks(
			@RequestParam(value = "search") String search) {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.findTrack(search).size(); i++) {
			titles.add(soundcloud.findTrack(search).get(i).getTitle());
			urlCover.add(soundcloud.findTrack(search).get(i).getArtworkUrl());
			// urlStream.add(soundcloud.findTrack(search).get(i).getStreamUrl());
			// lengths.add(soundcloud.findTrack(search).get(i).getDuration());
		}
		return (new SoundcloudModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/scsearchtracksstream")
	public SoundcloudModel searchTracksStream(
			@RequestParam(value = "search") String search) {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.findTrack(search).size(); i++) {
			// titles.add(soundcloud.findTrack(search).get(i).getTitle());
			// urlCover.add(soundcloud.findTrack(search).get(i).getArtworkUrl());
			urlStream.add(soundcloud.findTrack(search).get(i).getStreamUrl());
			lengths.add(soundcloud.findTrack(search).get(i).getDuration());
		}
		return (new SoundcloudModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/scuserplaylists")
	public SoundcloudModel userPlaylists() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		User me = soundcloud.getMe();
		Integer count = me.getPlaylistCount();
		for (int i = 0; i < count; i++) {
			titles.add(soundcloud.getMePlaylists().get(i).getTitle());
			urlCover.add(soundcloud.getMePlaylists().get(i).getArtworkUrl());
		}
		return (new SoundcloudModel(titles, urlCover));
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "Error handling";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return PATH;
	}
}
