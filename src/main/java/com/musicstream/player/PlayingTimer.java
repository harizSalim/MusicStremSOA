package com.musicstream.player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * This class counts playing time in the form of HH:mm:ss It also updates the
 * time slider
 * 
 */
public class PlayingTimer extends Thread {
	private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
	private boolean isRunning = false;
	private boolean isPause = false;
	private boolean isReset = false;
	private long startTime;
	private long pauseTime;
	private int seconds = 0;
	private int pauseSeconds = 0;

	private JLabel labelRecordTime;
	private JSlider slider;

	public void setActualSeconds(int seconds) {
		this.seconds = seconds;
	}

	PlayingTimer(JLabel labelRecordTime, JSlider slider) {
		this.labelRecordTime = labelRecordTime;
		this.slider = slider;
	}

	@Override
	public void run() {
		isRunning = true;

		startTime = System.currentTimeMillis();

		while (isRunning) {
			try {
				Thread.sleep(100);
				if (!isPause) {
					labelRecordTime.setText(toTimeString());
					int currentSecond = seconds;
					slider.setValue(currentSecond);

				} else {
					pauseTime += 100;
				}
			} catch (InterruptedException ex) {
				// ex.printStackTrace();
				if (isReset) {
					slider.setValue(0);
					labelRecordTime.setText("00:00:00");
					isRunning = false;
					break;
				}
			}
		}
	}

	/**
	 * Reset counting to "00:00:00"
	 */
	void reset() {
		isReset = true;
		isRunning = false;
	}

	void pauseTimer(int pauseSec) {
		isPause = true;
		pauseSeconds = pauseSec;
	}

	void resumeTimer() {
		isPause = false;
	}

	/**
	 * Generate a String for time counter in the format of "HH:mm:ss"
	 * 
	 * @return the time counter
	 */
	private String toTimeString() {
		long now = System.currentTimeMillis();
		Date current = new Date(now - startTime - pauseTime);
		dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeCounter = dateFormater.format(current);
		return timeCounter;
	}
}