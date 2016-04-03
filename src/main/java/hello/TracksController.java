package hello;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.voidplus.soundcloud.SoundCloud;

@RestController
public class TracksController {
	private SoundCloud soundcloud = new SoundCloud(
			"481ca2dc5f8ff18044c77239659a5b59",
			"1067035a0baee3932e79847bae144fa1", "testing.kamel@gmail.com",
			"azerty123");

	@RequestMapping("/tracks")
	public Tracks tracks() {
		ArrayList<String> t = new ArrayList<>();
		for (int i = 0; i < soundcloud.getMeFavorites().size(); i++) {
			t.add(soundcloud.getMeFavorites().get(i).getTitle());
		}
		return (new Tracks(t));
	}
}
