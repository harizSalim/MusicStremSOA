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

public class DeezerControllerTest {

	// Required to Generate JSON content from Java objects
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	// Test RestTemplate to invoke the APIs.
	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testScUserTracks() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/dzusertracks", Map.class);

		assertNotNull("Deezer User Tracks : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserTracksStream() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/dzusertracksstream", Map.class);

		assertNotNull("Deezer User Tracks Stream : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("urlStream");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserPlaylist() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/dzuserplaylists", Map.class);

		assertNotNull("Deezer User Playlists : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserSearch() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/dzuserplaylists"
						+ SearchPan.textField.getText(), Map.class);

		assertNotNull("Deezer Search : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserSearchStream() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/dzsearchtracksstream"
						+ SearchPan.textField.getText(), Map.class);

		assertNotNull("Deezer Search Stream : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("urlStream");
		assertTrue(booksList.size() != 0);

	}
}