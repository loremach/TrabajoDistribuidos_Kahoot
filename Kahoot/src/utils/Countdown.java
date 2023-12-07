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
	private CyclicBarrier barrera;

	public CountDown(int sec, Timer t, ResponderPregunta responder, CyclicBarrier barrera){
		this.seconds = sec;
		this.timer = t;
		this.responder = responder;
		this.barrera = barrera;
	}

	public void run() {
		System.out.println(this.seconds);
		this.seconds--;

		if (this.seconds == 0) {
			
			try {
				responder.interrupt();
				barrera.await();
				timer.cancel();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
