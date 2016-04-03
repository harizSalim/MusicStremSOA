package com.musicstream.main;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.musicstream.utils.JsonReader;
 

/**
 * @author Malek Main Function
 */
public class StartApp {

	public static void main(String[] args) {
		// MainAuthFrame auth = new MainAuthFrame();

		JsonReader js= new JsonReader();
		 JSONObject json;
		try {
			json = js.readJsonFromUrl("http://localhost:8080/dzuserplaylists");
			 System.out.println(json.toString());
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		     
	  
	}

}
