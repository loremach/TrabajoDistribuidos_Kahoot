package utils;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import conexiones.ResponderPregunta;

public class CountDown extends TimerTask{

	private int seconds;
	private Timer timer;
	private ResponderPregunta responder;

	public CountDown(int sec, Timer t, ResponderPregunta responder){
		this.seconds = sec;
		this.timer = t;
		this.responder = responder;
	}

	public void run() {
		System.out.println(this.seconds);
		this.seconds--;

		if (this.seconds == 0) {
			responder.interrupt();
			timer.cancel();
		}
	}
}
