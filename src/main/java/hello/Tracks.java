package hello;

import java.util.ArrayList;

public class Tracks {

	private ArrayList<String> title;
	private double length;

	public Tracks(ArrayList<String> title) {
		this.title = title;
	}

	public ArrayList<String> getTitle() {
		return title;
	}

	public double getLength() {
		return length;
	}

}
