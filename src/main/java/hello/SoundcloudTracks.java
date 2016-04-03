package hello;

import java.util.ArrayList;

public class SoundcloudTracks {

	private ArrayList<String> title;
	private ArrayList<String> urlCover;
	private ArrayList<String> urlStream;
	private ArrayList<Integer> length;

	public SoundcloudTracks(ArrayList<String> title, ArrayList<String> urlCover,
			ArrayList<String> urlStream, ArrayList<Integer> length) {
		this.title = title;
		this.urlCover = urlCover;
		this.urlStream = urlStream;
		this.length = length;
	}

	public ArrayList<String> getTitle() {
		return title;
	}

	public ArrayList<String> getUrlCover() {
		return urlCover;
	}

	public ArrayList<String> getUrlStream() {
		return urlStream;
	}

	public ArrayList<Integer> getLength() {
		return length;
	}

}
