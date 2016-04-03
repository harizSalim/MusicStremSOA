package com.musicstream.soa;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeloon.deezer.client.DeezerClient;
import com.zeloon.deezer.domain.User;
import com.zeloon.deezer.domain.internal.UserId;
import com.zeloon.deezer.domain.internal.search.Search;
import com.zeloon.deezer.domain.internal.search.SearchOrder;
import com.zeloon.deezer.io.HttpResourceConnection;

@RestController
public class DeezerController {
	private DeezerClient deezerClient = new DeezerClient(
			new HttpResourceConnection());
	private UserId uID = new UserId(905831863L);
	private User user = deezerClient.get(uID);

	@RequestMapping("/dzusertracks")
	public DeezerModel userTracks() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < deezerClient.getTracks(uID).getData().size(); i++) {
			titles.add(deezerClient.getTracks(uID).getData().get(i).getTitle());
			urlCover.add(deezerClient.getTracks(uID).getData().get(i)
					.getAlbum().getCover());
			urlStream.add(deezerClient.getTracks(uID).getData().get(i)
					.getPreview());
			lengths.add(30);
		}
		return (new DeezerModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/dzsearchtracks")
	public DeezerModel searchTracks(
			@RequestParam(value = "search") String search) {
		Search querry = new Search(search, SearchOrder.RANKING);
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < deezerClient.search(querry).getData().size(); i++) {
			titles.add(deezerClient.search(querry).getData().get(i).getTitle());
			urlCover.add(deezerClient.search(querry).getData().get(i)
					.getAlbum().getCover());
			urlStream.add(deezerClient.search(querry).getData().get(i)
					.getPreview());
			lengths.add(30);
		}
		return (new DeezerModel(titles, urlCover, urlStream, lengths));
	}

	@RequestMapping("/dzuserplaylists")
	public DeezerModel userPlaylists() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < deezerClient.getPlaylists(uID).getData().size(); i++) {
			titles.add(deezerClient.getPlaylists(uID).getData().get(i)
					.getTitle());
			urlCover.add(deezerClient.getPlaylists(uID).getData().get(i)
					.getPicture());
		}
		return (new DeezerModel(titles, urlCover));
	}
}
