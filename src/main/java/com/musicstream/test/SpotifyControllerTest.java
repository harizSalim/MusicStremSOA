package com.musicstream.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicstream.homeTabs.SearchPan;

public class SpotifyControllerTest {

	// Required to Generate JSON content from Java objects
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	// Test RestTemplate to invoke the APIs.
	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testScUserTracks() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/spusertracks", Map.class);

		assertNotNull("Spotify User Tracks : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserTracksStream() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/spusertracksstream", Map.class);

		assertNotNull("Spotify User Tracks Stream : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("urlStream");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserPlaylist() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/spuserplaylists", Map.class);

		assertNotNull("Spotify User Playlists : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserSearch() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/spuserplaylists"
						+ SearchPan.textField.getText(), Map.class);

		assertNotNull("Spotify Search : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserSearchStream() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/spsearchtracksstream"
						+ SearchPan.textField.getText(), Map.class);

		assertNotNull("Spotify Search Stream : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("urlStream");
		assertTrue(booksList.size() != 0);

	}
}