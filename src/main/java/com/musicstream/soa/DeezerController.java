package com.musicstream.soa;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeloon.deezer.client.DeezerClient;
import com.zeloon.deezer.domain.User;
import com.zeloon.deezer.domain.internal.TrackId;
import com.zeloon.deezer.domain.internal.UserId;
import com.zeloon.deezer.domain.internal.search.Search;
import com.zeloon.deezer.domain.internal.search.SearchOrder;
import com.zeloon.deezer.io.HttpResourceConnection;

@RestController
public class DeezerController implements ErrorController {
	private DeezerClient deezerClient = new DeezerClient(
			new HttpResourceConnection());
	private UserId uID = new UserId(905831863L);
	private User user = deezerClient.get(uID);
	private static final String PATH = "/errorDZ";

	@RequestMapping("/dzusertracks")
	public DeezerModel userTracks() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < deezerClient.getTracks(uID).getData().size(); i++) {
			titles.add(deezerClient.getTracks(uID).getData().get(i).getTitle());
			urlCover.add(deezerClient.getTracks(uID).getData().get(i)
					.getAlbum().getCover());
		}
		return (new DeezerModel(deezerClient.get(uID).getName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/dzusertracksstream")
	public DeezerModel userTracksStream() {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < deezerClient.getTracks(uID).getData().size(); i++) {
			TrackId tID = new TrackId(deezerClient.getTracks(uID).getData()
					.get(i).getId());
			urlStream.add(deezerClient.get(tID).getPreview());
			lengths.add(30);
		}
		return (new DeezerModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping("/dzsearchtracks")
	public DeezerModel searchTracks(
			@RequestParam(value = "search") String search) {
		Search querry = new Search(search, SearchOrder.RANKING);
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < deezerClient.search(querry).getData().size(); i++) {
			titles.add(deezerClient.search(querry).getData().get(i).getTitle());
			urlCover.add(deezerClient.search(querry).getData().get(i)
					.getAlbum().getCover());
		}
		return (new DeezerModel(deezerClient.get(uID).getName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/dzsearchtracksstream")
	public DeezerModel searchTracksStream(
			@RequestParam(value = "search") String search) {
		Search querry = new Search(search, SearchOrder.RANKING);
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < deezerClient.search(querry).getData().size(); i++) {
			urlStream.add(deezerClient.search(querry).getData().get(i)
					.getPreview());
			lengths.add(30);
		}
		return (new DeezerModel(null, null, null, urlStream, lengths));
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
		return (new DeezerModel(deezerClient.get(uID).getName(), titles,
				urlCover));
	}

	@RequestMapping("/dzplaylistinfo")
	public DeezerModel playlistInfo(@RequestParam(value = "index") int index) {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> urlCover = new ArrayList<>();
		for (int i = 0; i < deezerClient.getPlaylists(uID).getData().get(index)
				.getTracks().getData().size(); i++) {
			titles.add(deezerClient.getPlaylists(uID).getData().get(index)
					.getTracks().getData().get(i).getTitle());
			urlCover.add(deezerClient.getPlaylists(uID).getData().get(index)
					.getTracks().getData().get(i).getAlbum().getCover());
		}
		return (new DeezerModel(deezerClient.get(uID).getName(), titles,
				urlCover, null, null));
	}

	@RequestMapping("/dzplaylistinfoStream")
	public DeezerModel playlistInfoStream(
			@RequestParam(value = "index") int index) {
		ArrayList<String> urlStream = new ArrayList<>();
		ArrayList<Integer> lengths = new ArrayList<>();
		for (int i = 0; i < deezerClient.getPlaylists(uID).getData().get(index)
				.getTracks().getData().size(); i++) {
			urlStream.add(deezerClient.getPlaylists(uID).getData().get(index)
					.getTracks().getData().get(i).getPreview());
			lengths.add(30);
		}
		return (new DeezerModel(null, null, null, urlStream, lengths));
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "Error handling Deezer";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return PATH;
	}
}
