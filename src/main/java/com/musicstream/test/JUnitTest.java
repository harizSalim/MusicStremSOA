package com.musicstream.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicstream.soa.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class JUnitTest {

	// Required to Generate JSON content from Java objects
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	// Test RestTemplate to invoke the APIs.
	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testScUserTracks() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/scusertracks", Map.class);

		assertNotNull("Soundcloud User Tracks : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}

	@Test
	public void testScUserTracksStream() {
		Map<String, Object> scResponse = restTemplate.getForObject(
				"http://localhost:8080/scusertracksstream", Map.class);

		assertNotNull("Soundcloud User Tracks Stream : ", scResponse);

		List<Map<String, Object>> booksList = (List<Map<String, Object>>) scResponse
				.get("title");
		assertTrue(booksList.size() != 0);

	}
}
