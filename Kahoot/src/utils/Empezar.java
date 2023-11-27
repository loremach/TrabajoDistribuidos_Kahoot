package utils;

import java.net.ServerSocket;

public class Empezar extends Thread{
	private boolean listo;
	private ServerSocket ss;
	
	public Empezar(ServerSocket ss) {
		this.listo = false;
		this.ss=ss;
	}
	
	public void run() {
		while(!listo) {
		}
		try {
			ss.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setListo(boolean listo) {
		this.listo=listo;
	}
}
