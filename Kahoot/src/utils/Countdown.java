package utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import conexiones.clasesClienteConsola.ResponderPregunta;

public class Countdown extends TimerTask{

	private int seconds;
	private Timer timer;
	private ResponderPregunta responder;
	private CyclicBarrier barrera;
	private JLabel tiempo;

	public Countdown(int sec, Timer t, ResponderPregunta responder, CyclicBarrier barrera){
		this.seconds = sec;
		this.timer = t;
		this.responder = responder;
		this.barrera = barrera;
		this.tiempo = null;
	}

	public Countdown(int sec, Timer t, CyclicBarrier barrera, JPanel panelPreguntaPuntos){
		this.seconds = sec;
		this.timer = t;
		this.responder = null;
		this.barrera = barrera;
		this.tiempo = (JLabel) panelPreguntaPuntos.getComponent(2);
	}

	public void run() {
		if(this.tiempo!=null){
			this.tiempo.setText(String.valueOf(seconds));
		}else{
			System.out.println(this.seconds);
		}
		this.seconds--;
		if (this.seconds == 0) {
			try {
				if(this.tiempo!=null){
					this.tiempo.setText(String.valueOf(seconds));
				}else{
					System.out.println(this.seconds);
				}
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
