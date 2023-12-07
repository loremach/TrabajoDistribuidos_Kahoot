package utils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import conexiones.AtenderCliente;
import conexiones.EnviadorPregunta;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;
import dominio.SocketCliente;

public class Empezar extends Thread{
	private boolean listo;
	//private static HashMap<Persona, Socket> clientesConectados = new HashMap<>();
	private static HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    private static Sala sala;
	
	public Empezar(HashMap<Persona, SocketCliente> clientes, Sala sala, boolean listo) {
		this.clientesConectados = clientes;
        this.sala = sala;
        this.listo = listo;
	}
	
	public void run() {
		Scanner teclado = new Scanner(System.in);
		System.out.println("Pulsa ENTER cuando estés listo");
		String entrada = "A";
		boolean permitido = false;
		do{
			entrada = teclado.nextLine();
			if(entrada.equals("")) {
				if(clientesConectados.size()==0){
					System.out.println("Debe haber al menos un jugador conectado");
				}else{
					permitido=true;
				}
			}
		}while(!entrada.equals("") && !permitido);
		System.out.println("Has pulsado ENTER");
		this.listo = true;
		
		ExecutorService pool = Executors.newFixedThreadPool(20);
		try{
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) sala.getPreguntas();
            int numPreg = preguntas.size();
            int numJugadores = clientesConectados.size();

            for(Persona p: clientesConectados.keySet()){
                tablaPuntuaciones.put(p, 0);
            }

            for (int i = 0; i<numPreg; i++){
                ArrayList<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
                CyclicBarrier barrera = new CyclicBarrier(numJugadores+1);
				CountDownLatch recoger = new CountDownLatch(numJugadores);
                for (Persona p: clientesConectados.keySet()){
                    resultados.add(pool.submit(new EnviadorPregunta(clientesConectados.get(p), p, preguntas.get(i), barrera, recoger)));
                }
                barrera.await();
				recoger.await();

                HashMap<Persona, Integer> aux;
                ArrayList<Persona> auxPersona;
                for(int j = 0; j < resultados.size(); j++){
                    aux = resultados.get(j).get();
                    auxPersona = new ArrayList<Persona> (aux.keySet());
                    tablaPuntuaciones.put(auxPersona.get(0), aux.get(auxPersona.get(0)));
                }

                escribirTablaPuntuaciones(tablaPuntuaciones);
			}
		}catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
			pool.shutdown();
			
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
			pool.shutdown();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
			pool.shutdown();
        }

	}

	private static String escribirTablaPuntuaciones(HashMap<Persona, Integer> tablaPuntuaciones){
        String mostrar ="Nombre                 Puntos       \n---------------------------------------\n";
        for(Persona p : tablaPuntuaciones.keySet()){
            mostrar = mostrar + p.getAlias() + "            " + tablaPuntuaciones.get(p) + "    \n";
        }
        return mostrar;
    }
}
