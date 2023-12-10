package conexiones.clasesClienteConsola;

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

import conexiones.clasesComunes.EnviadorPregunta;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;
import dominio.SocketCliente;

public class Empezar extends Thread{
	private boolean listo;
	private HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    private Sala sala;
	
	public Empezar(HashMap<Persona, SocketCliente> clientes, Sala sala) {
		this.clientesConectados = clientes;
        this.sala = sala;
        this.listo = false;
	}
	
    /**
     * Ejecuta el juego interactivo de preguntas y respuestas.
     * El método controla el flujo del juego, enviando preguntas a los jugadores,
     * recolectando sus respuestas, calculando las puntuaciones y anunciando al ganador.
     */
	public void run() {
		Scanner teclado = new Scanner(System.in);
		System.out.println("Pulsa ENTER cuando estés listo");
		String entrada = "A";
		boolean permitido = false;

        // Espera hasta que se presione ENTER y al menos un jugador esté conectado
		do{
			entrada = teclado.nextLine();
			if(entrada.equals("")) {
				if(clientesConectados.size()==0){
					System.out.println("Debe haber al menos un jugador conectado");
				}else{
					permitido=true;
				}
			}
		}while(!entrada.equals("") || !permitido);
		System.out.println("Has pulsado ENTER");
		this.listo = true;
		
		ExecutorService pool = Executors.newFixedThreadPool(20);
		try{
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) sala.getPreguntas();
            int numPreg = preguntas.size();
            int numJugadores = clientesConectados.size();

            // Inicialización de la tabla de puntuaciones para cada jugador
            for(Persona p: clientesConectados.keySet()){
                tablaPuntuaciones.put(p, 0);
            }

            // Bucle para enviar las preguntas de una en una a todos los jugadores al mismo tiempo
            for (int i = 0; i<numPreg; i++){
                ArrayList<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
                CyclicBarrier barrera = new CyclicBarrier(numJugadores+1);
				CountDownLatch recoger = new CountDownLatch(numJugadores);

                // Envío de la pregunta a cada jugador 
                for (Persona p: clientesConectados.keySet()){
                    resultados.add(pool.submit(new EnviadorPregunta(clientesConectados.get(p), p, preguntas.get(i), barrera, recoger)));
                }
                barrera.await();
				recoger.await();

                // Procesamiento de respuestas una vez están listas y actualización de puntuaciones
                HashMap<Persona, Integer> aux;
                ArrayList<Persona> auxPersona;
                for(int j = 0; j < resultados.size(); j++){
                    aux = resultados.get(j).get();
                    auxPersona = new ArrayList<Persona> (aux.keySet());
                    tablaPuntuaciones.put(auxPersona.get(0), tablaPuntuaciones.get(auxPersona.get(0))+ aux.get(auxPersona.get(0)));
                }

                // Mostrar tabla de puntuaciones actual
                System.out.println(escribirTablaPuntuaciones(tablaPuntuaciones));
                
                //Si hay más preguntas, enviar la siguiente cuando se pulse ENTER
                //Si no hay más preguntas, mostrar tabla final de puntuaciones y anunciar al ganador
                if(i!=numPreg-1){
                    System.out.println("Pulsa ENTER cuando quieras enviar la siguiente pregunta");
                    entrada = "A";
                    do{
                        entrada = teclado.nextLine();
                    }while(!entrada.equals(""));
                    System.out.println("Siguiente pregunta enviada");
                }else{
                    Persona ganador = calcularGanador(tablaPuntuaciones);
                    System.out.println("¡Ha ganado " + ganador.getAlias() + " con " + tablaPuntuaciones.get(ganador) + " puntos!");
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
        teclado.close();
	}

    /**
     * Genera una representación en forma de tabla de las puntuaciones de los jugadores.
     * 
     * @param tablaPuntuaciones HashMap que asigna objetos Persona a sus respectivas puntuaciones.
     * @return Una cadena de texto representando la tabla de puntuaciones.
     */
	private static String escribirTablaPuntuaciones(HashMap<Persona, Integer> tablaPuntuaciones){
        String mostrar ="Nombre                 Puntos       \n-------------------------------------\n";
        for(Persona p : tablaPuntuaciones.keySet()){
            mostrar = mostrar + p.getAlias() + "                     " + tablaPuntuaciones.get(p) + "    \n";
        }
        return mostrar;
    }

    /**
     * Calcula y devuelve la persona con la puntuación más alta, es decir, la ganadora.
     *
     * @param tablaPuntuaciones HashMap que asigna objetos Persona a sus respectivas puntuaciones.
     * @return La Persona con la puntuación más alta (ganadora).
     */
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

    public boolean getListo(){
        return this.listo;
    }
}
