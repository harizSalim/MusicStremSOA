package com.musicstream.main;

import com.google.gson.Gson;
import com.musicstream.soa.Connect;
import com.musicstream.soa.Greeting;

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

		Connect http = new Connect();
		String data = http.getJSON("http://localhost:8080/greeting", 100000000);
		Greeting msg = new Gson().fromJson(data, Greeting.class);
		System.out.println(msg);
	}

}
