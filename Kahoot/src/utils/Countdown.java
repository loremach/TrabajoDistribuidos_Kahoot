package utils;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import conexiones.ResponderPregunta;
import conexiones.ResponderPregunta2;

public class CountDown extends TimerTask{

	private int seconds;
	private Timer timer;
	private ResponderPregunta responder;
	private CyclicBarrier barrera;
	private JLabel tiempo;

	public CountDown(int sec, Timer t, ResponderPregunta responder, CyclicBarrier barrera){
		this.seconds = sec;
		this.timer = t;
		this.responder = responder;
		this.barrera = barrera;
		this.tiempo = null;
	}

	public CountDown(int sec, Timer t, CyclicBarrier barrera, JPanel panelPreguntaPuntos){
		this.seconds = sec;
		this.timer = t;
		this.responder = null;
		this.barrera = barrera;
		this.tiempo = (JLabel) panelPreguntaPuntos.getComponent(2);
	}

	public void run() {
		System.out.println(this.seconds);
		this.seconds--;
		if(this.tiempo!=null){
			this.tiempo.setText(String.valueOf(seconds));
		}
		if (this.seconds == 0) {
			try {
				if(responder!=null) responder.interrupt();
				barrera.await();
				timer.cancel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			
		}
	}
}
