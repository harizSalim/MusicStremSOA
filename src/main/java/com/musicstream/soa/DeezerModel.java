package com.musicstream.soa;

import java.util.ArrayList;

public class DeezerModel {

	private String name;
	private ArrayList<String> title, playlistName;
	private ArrayList<String> urlCover, playlistUrlCover;
	private ArrayList<String> urlStream;
	private ArrayList<Integer> length;

	public DeezerModel(String name, ArrayList<String> title,
			ArrayList<String> urlCover, ArrayList<String> urlStream,
			ArrayList<Integer> length) {
		this.name = name;
		this.title = title;
		this.urlCover = urlCover;
		this.urlStream = urlStream;
		this.length = length;
	}

	public DeezerModel(String name, ArrayList<String> playlistName,
			ArrayList<String> playlistUrlCover) {
		this.name = name;
		this.playlistName = playlistName;
		this.playlistUrlCover = playlistUrlCover;
	}

	public String getName() {
		return name;
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

	public ArrayList<String> getPlaylistName() {
		return playlistName;
	}

	public ArrayList<String> getPlaylistUrlCover() {
		return playlistUrlCover;
	}
}
