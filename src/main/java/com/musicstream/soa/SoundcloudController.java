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
	private static final String PATH = "/errorSC";

	@RequestMapping("/scusertracks")
	public SoundcloudModel userTracks() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			titles.add(soundcloud.getMeFavorites().get(i).getTitle());
			urlCover.add(soundcloud.getMeFavorites().get(i).getArtworkUrl());
		}
		return (new SoundcloudModel(soundcloud.getMe().getUsername(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/scusertracksstream")
	public SoundcloudModel userTracksStream() {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			urlStream.add(soundcloud.getMeFavorites().get(i).getStreamUrl());
			lengths.add(soundcloud.getMeFavorites().get(i).getDuration());
		}
		return (new SoundcloudModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/scsearchtracks")
	public SoundcloudModel searchTracks(
			@RequestParam(value = "search") String search) {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		if (soundcloud.findTrack(search).size() > 0) {
			for (int i = 0; i < 7; i++) {
				titles.add(soundcloud.findTrack(search).get(i).getTitle());
				urlCover.add(soundcloud.findTrack(search).get(i)
						.getArtworkUrl());
			}
		}
		return (new SoundcloudModel(soundcloud.getMe().getUsername(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/scsearchtracksstream")
	public SoundcloudModel searchTracksStream(
			@RequestParam(value = "search") String search) {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			System.out.println(soundcloud.findTrack(search).get(i).getTitle());
			urlStream.add(soundcloud.findTrack(search).get(i).getStreamUrl());
			lengths.add(soundcloud.findTrack(search).get(i).getDuration());
		}
		return (new SoundcloudModel(null, null, null, urlStream, lengths));
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
		return (new SoundcloudModel(soundcloud.getMe().getUsername(), titles,
				urlCover));
	}

	@RequestMapping("/scplaylistinfo")
	public SoundcloudModel playlistInfo(@RequestParam(value = "index") int index) {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < (soundcloud.getMePlaylists().get(index).getTracks())
				.size(); i++) {
			titles.add(soundcloud.getMePlaylists().get(index).getTracks()
					.get(i).getTitle());
			urlCover.add(soundcloud.getMePlaylists().get(index).getTracks()
					.get(i).getArtworkUrl());
		}
		return (new SoundcloudModel(soundcloud.getMe().getUsername(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/scplaylistinfoStream")
	public SoundcloudModel playlistInfoStream(
			@RequestParam(value = "index") int index) {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < (soundcloud.getMePlaylists().get(index).getTracks())
				.size(); i++) {
			try {
				urlStream.add(soundcloud.getMePlaylists().get(index)
						.getTracks().get(i).getStreamUrl());
			} catch (Exception ex) {
			}
			lengths.add(soundcloud.getMePlaylists().get(index).getTracks()
					.get(i).getDuration());
		}
		return (new SoundcloudModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "Error handling SoundCloud";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return PATH;
	}
}