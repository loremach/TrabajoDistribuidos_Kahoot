package conexiones.clasesClienteGrafico;

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

import javax.swing.JTextArea;

import conexiones.ClienteConsola;
import conexiones.clasesComunes.AtenderCliente;
import conexiones.clasesComunes.EnviadorPregunta;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;
import dominio.SocketCliente;

public class Empezar2 extends Thread{
	//private HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    //private Sala sala;
    private JTextArea textPuntuaciones;
    private Cliente2 cliente;
	
	// public Empezar2(HashMap<Persona, SocketCliente> clientes, Sala sala, JTextArea textPuntuaciones, boolean listo) {
	// 	this.clientesConectados = clientes;
    //     this.sala = sala;
    //     this.listo = listo;
    //     this.textPuntuaciones = textPuntuaciones;
	// }

    public Empezar2(Cliente2 cliente, JTextArea textPuntuaciones){
        this.cliente = cliente;
        //this.clientesConectados = cliente.getClientesConectados();
        //this.sala = cliente.getSala();
        this.textPuntuaciones = textPuntuaciones;
    }
	
	public void run() {
		this.cliente.setListo(true);
		int numJugadores = this.cliente.getClientesConectados().size();
        if(numJugadores>0){
            ExecutorService pool = Executors.newFixedThreadPool(20);
            try{
                HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
                ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) this.cliente.getSala().getPreguntas();
                int numPreg = preguntas.size();
                
                for(Persona p: this.cliente.getClientesConectados().keySet()){
                    tablaPuntuaciones.put(p, 0);
                }

                for (int i = 0; i<numPreg; i++){
                    ArrayList<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
                    CyclicBarrier barrera = new CyclicBarrier(numJugadores+1);
                    CountDownLatch recoger = new CountDownLatch(numJugadores);
                    for (Persona p: this.cliente.getClientesConectados().keySet()){
                        resultados.add(pool.submit(new EnviadorPregunta(this.cliente.getClientesConectados().get(p), p, preguntas.get(i), barrera, recoger)));
                    }
                    barrera.await();
                    recoger.await();

                    HashMap<Persona, Integer> aux;
                    ArrayList<Persona> auxPersona;
                    for(int j = 0; j < resultados.size(); j++){
                        aux = resultados.get(j).get();
                        auxPersona = new ArrayList<Persona> (aux.keySet());
                        tablaPuntuaciones.put(auxPersona.get(0), tablaPuntuaciones.get(auxPersona.get(0))+ aux.get(auxPersona.get(0)));
                    }
                    
                    if(i==numPreg-1){
                        Persona ganador = calcularGanador(tablaPuntuaciones);
                        this.textPuntuaciones.setText(escribirTablaPuntuaciones(tablaPuntuaciones) + 
                        "\n\n" + "¡Ha ganado " + ganador.getAlias() + " con " + tablaPuntuaciones.get(ganador) + " puntos!");  
                    }else{
                        this.textPuntuaciones.setText(escribirTablaPuntuaciones(tablaPuntuaciones));
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
                pool.shutdown();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                pool.shutdown();
            } catch (ExecutionException e) {
                e.printStackTrace();
                pool.shutdown();
            }
        }
	}

	private static String escribirTablaPuntuaciones(HashMap<Persona, Integer> tablaPuntuaciones){
        String mostrar ="Nombre                 Puntos       \n------------------------------------------------\n";
        for(Persona p : tablaPuntuaciones.keySet()){
            mostrar = mostrar + p.getAlias() + "                       " + tablaPuntuaciones.get(p) + "    \n";
        }
        return mostrar;
    }

    private static Persona calcularGanador(HashMap<Persona, Integer> tablaPuntuaciones){
        ArrayList<Persona> auxPersona = new ArrayList<Persona> (tablaPuntuaciones.keySet());
        Persona ganadora = auxPersona.get(0);
        int puntosGanador = tablaPuntuaciones.get(ganadora);
        for(int i = 1; i<auxPersona.size(); i++){
            if(tablaPuntuaciones.get(auxPersona.get(i))>puntosGanador){
                ganadora = auxPersona.get(i);
                puntosGanador = tablaPuntuaciones.get(ganadora);
            }
        }
        return ganadora;
    }

    // public boolean getListo(){
    //     return listo;
    // }
}
