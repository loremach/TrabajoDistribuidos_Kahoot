package conexiones;

import java.awt.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;

public class EnviarPreguntas extends Thread{
    private static HashMap<Persona, Socket> clientesConectados = new HashMap<>();
    private static Sala sala;
    private static boolean listo;

    public EnviarPreguntas(HashMap<Persona, Socket> clientes, Sala sala, boolean listo){
        this.clientesConectados = clientes;
        this.sala = sala;
        this.listo = listo;
    }

    public void run(){
        while(!listo){}
        try{
            ExecutorService pool = Executors.newFixedThreadPool(20);
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) sala.getPreguntas();
            int numPreg = preguntas.size();
            int numJugadores = clientesConectados.size();

            for(Persona p: clientesConectados.keySet()){
                tablaPuntuaciones.put(p, 0);
            }

            for (int i = 0; i<numPreg; i++){
                ArrayList<AtenderCliente> listaEjecuciones = new ArrayList<>();
                ArrayList<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
                CyclicBarrier barrera = new CyclicBarrier(numJugadores+1);

                for (Persona p: clientesConectados.keySet()){
                    resultados.add(pool.submit(new EnviadorPregunta(clientesConectados.get(p), p, preguntas.get(i), barrera)));
                }
                barrera.await();

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
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
