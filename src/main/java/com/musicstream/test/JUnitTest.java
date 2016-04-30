package com.musicstream.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.homeTabs.SearchPan;

public class JUnitTest {

	SoundCloudApi soundCloudAPI = new SoundCloudApi();
	DeezerApi deezerAPI = new DeezerApi();

	@Test
	public void testSoundCloudApi() {
		assertNotNull("Soundcloud API instanci� ", soundCloudAPI);
	}

	@Test
	public void testDeezerApi() {
		assertNotNull("Deezer API instanci� ", deezerAPI);
	}

	@Test
	public void tesGetSCUser() {
		assertNotNull("Session Soundcloud ouverte: ", soundCloudAPI.getUser());
	}

	@Test
	public void tesGetDZUser() {
		assertNotNull("Session Deezer ouverte: ", deezerAPI.getUser());
	}

	@Test
	public void testGetTracksByUser() {
		assertNotNull("Liste des chansons charg�es de Soundcloud ",
				soundCloudAPI.getTracksByUser());
		assertNotNull("Liste des chansons charg�es de Deezer ",
				deezerAPI.getTracksByUser());
	}

	@Test
	public void testGetPlaylistByUser() {
		assertNotNull("Liste des playlists charg�es de Soundcloud ",
				soundCloudAPI.getPlaylistByUser());
		assertNotNull("Liste des playlists charg�es de Deezer ",
				deezerAPI.getPlaylistByUser());
	}

	@Test
	public void testGetTracksSearch() {
		assertNotNull("Liste des chansons recherch�es charg�es de Soundcloud ",
				soundCloudAPI.getTrack(SearchPan.textField.getText()));
		assertNotNull("Liste des chansons recherch�es charg�es de Deezer ",
				deezerAPI.getTrack(SearchPan.textField.getText()));
	}

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("com.musicstream.test.JUnitTest");
	}
}
