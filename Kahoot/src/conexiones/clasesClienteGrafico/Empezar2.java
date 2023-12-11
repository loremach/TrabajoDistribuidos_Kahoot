package conexiones.clasesClienteGrafico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JTextArea;

import conexiones.clasesComunes.EnviadorPregunta;
import dominio.Persona;
import dominio.Pregunta;

public class Empezar2 extends Thread{

    private JTextArea textPuntuaciones;
    private Cliente2 cliente;

    public Empezar2(Cliente2 cliente, JTextArea textPuntuaciones){
        this.cliente = cliente;
        this.textPuntuaciones = textPuntuaciones;
    }
	
    /**
     * Ejecuta el juego con una serie de preguntas para los jugadores conectados.
     * - Configura al jugador anfitrión como listo para comenzar el juego.
     * - Organiza la ejecución de preguntas para los jugadores y calcula las puntuaciones finales.
     * - Muestra las puntuaciones y al ganador del juego en un área de texto.
     */
	public void run() {
        //Configura al jugador anfitrión como listo para comenzar le juego.
		this.cliente.setListo(true);
		int numJugadores = this.cliente.getClientesConectados().size(); // Obtiene el número de jugadores conectados
        
        ExecutorService pool = Executors.newFixedThreadPool(20); // Crea un pool de hilos para manejar las preguntas que se envían a cada jugador
        try{
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();  // Almacena las puntuaciones de los jugadores
            ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) this.cliente.getSala().getPreguntas();
            int numPreg = preguntas.size(); // Calcula el número total de preguntas
            
            // Inicializa la tabla de puntuaciones para cada jugador
            for(Persona p: this.cliente.getClientesConectados().keySet()){
                tablaPuntuaciones.put(p, 0);
            }

            for (int i = 0; i<numPreg; i++){
                ArrayList<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();

                // Establece una barrera para sincronizar a los jugadores para responder la pregunta actual
                CyclicBarrier barrera = new CyclicBarrier(numJugadores+1);
                CountDownLatch recoger = new CountDownLatch(numJugadores);

                // Envía la pregunta a cada jugador y espera sus respuestas
                for (Persona p: this.cliente.getClientesConectados().keySet()){
                    resultados.add(pool.submit(new EnviadorPregunta(this.cliente.getClientesConectados().get(p), p, preguntas.get(i), barrera, recoger)));
                }
                barrera.await(); 
                recoger.await(); 

                // Calcula las puntuaciones para cada jugador basadas en sus respuestas
                HashMap<Persona, Integer> aux;
                ArrayList<Persona> auxPersona;
                for(int j = 0; j < resultados.size(); j++){
                    aux = resultados.get(j).get();
                    auxPersona = new ArrayList<Persona> (aux.keySet());
                    tablaPuntuaciones.put(auxPersona.get(0), tablaPuntuaciones.get(auxPersona.get(0))+ aux.get(auxPersona.get(0)));
                }
                
                // Muestra las puntuaciones después de cada pregunta 
                // Si es la última pregunta, muestra las puntuaciones y el ganador de la partida
                if(i==numPreg-1){
                    Persona ganador = calcularGanador(tablaPuntuaciones);
                    this.textPuntuaciones.setText(escribirTablaPuntuaciones(tablaPuntuaciones) + 
                    "\n\n       " + "¡Ha ganado " + ganador.getAlias() + " con " + tablaPuntuaciones.get(ganador) + " puntos!");  
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

    /**
     * Genera y devuelve una representación en formato de tabla de las puntuaciones de los jugadores.
     * 
     * @param tablaPuntuaciones Un mapa que contiene las puntuaciones de los jugadores.
     * @return Una cadena de caracteres que representa las puntuaciones en formato de tabla.
     */
	private static String escribirTablaPuntuaciones(HashMap<Persona, Integer> tablaPuntuaciones){
        String mostrar ="\n       Nombre                        Puntos       \n       --------------------------------------------------\n";
        for(Persona p : tablaPuntuaciones.keySet()){
            int longAlias = p.getAlias().length();
            String espExtra = "";
            for(int j = 0; j<30-longAlias; j++){
                espExtra += " ";
            }
            mostrar = mostrar + "       " + p.getAlias() + espExtra + "          " + tablaPuntuaciones.get(p) + "    \n";
        }
        return mostrar;
    }

    /**
     * Calcula y devuelve al jugador ganador basado en las puntuaciones proporcionadas.
     * 
     * @param tablaPuntuaciones Un mapa que contiene las puntuaciones de los jugadores.
     * @return El objeto Persona que representa al jugador ganador.
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
}
