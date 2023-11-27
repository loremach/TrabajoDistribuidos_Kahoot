package utils;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.*;

public class Countdown implements Runnable {
	private int countdownStarter = 60;

	public void run() {
		System.out.println(countdownStarter);
		countdownStarter--;

		if (countdownStarter < 0) {
			System.out.println("Timer Over!");
		}
	}
}
