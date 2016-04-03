package hello;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.voidplus.soundcloud.SoundCloud;

@RestController
public class SoundcloudTracksController {
	private SoundCloud soundcloud = new SoundCloud(
			"481ca2dc5f8ff18044c77239659a5b59",
			"1067035a0baee3932e79847bae144fa1", "testing.kamel@gmail.com",
			"azerty123");

	@RequestMapping("/sctracks")
	public SoundcloudTracks tracks() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			titles.add(soundcloud.getMeFavorites().get(i).getTitle());
			urlCover.add(soundcloud.getMeFavorites().get(i).getArtworkUrl());
			urlStream.add(soundcloud.getMeFavorites().get(i).getStreamUrl());
			lengths.add(soundcloud.getMeFavorites().get(i).getDuration());
		}
		return (new SoundcloudTracks(titles, urlCover, urlStream, lengths));
	}
}
