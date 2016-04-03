package com.musicstream.main;

import java.io.IOException;

import com.musicstream.utils.HttpConnectionExample;

/**
 * @author Malek Main Function
 */
public class StartApp {

	public static void main(String[] args) {
		// MainAuthFrame auth = new MainAuthFrame();

		/*
		 * httpConnection http = new
		 * httpConnection("http://localhost:8080/tracks"); String resp =
		 * http.getJSON("http://localhost:8080/tracks", 100000);
		 * System.out.println("Resp:" + resp);
		 */

		HttpConnectionExample http = new HttpConnectionExample();
		try {
			http.doGet("http://127.0.0.1:8080/tracks");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
