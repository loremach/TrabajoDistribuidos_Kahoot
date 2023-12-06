package utils;

import java.net.ServerSocket;
import java.util.Scanner;

public class Empezar extends Thread{
	private boolean listo;
	
	public Empezar() {
		this.listo = false;
	}
	
	public void run() {
		Scanner teclado = new Scanner(System.in);
		System.out.println("Pulsa ENTER cuando estés listo");
		String entrada = "A";
		do{
			entrada = teclado.nextLine();
		}while(entrada!="");
		this.listo = true;
	}
}
