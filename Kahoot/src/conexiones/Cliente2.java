package conexiones;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import utils.CountDown;
import utils.Empezar;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Cliente2{
    private Sala sala = null;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    private HashMap<String, Persona> salas;
    private List<AtenderCliente> listaEjecuciones = new ArrayList<>();
    private ExecutorService pool = Executors.newFixedThreadPool(30);
    private Empezar empezar;
    
    public String crearSala() {
    	String idSala ="";
		try(Socket cliente = new Socket("localhost", 8000);
                ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
                DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
			outSocket.writeInt(1);
            outSocket.flush();
            
			String salaCreada = inSocket.readLine();
            if(salaCreada.equals("Has creado la sala.")){
                localPort = cliente.getLocalPort();
                idSala = inSocket.readLine();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
		this.sala = new Sala(idSala);
		return idSala;
    }
    
    public int getLocalPort() {
    	return localPort;
    }
    
    public void aniadirPreguntaASala(Pregunta p) {
    	if(this.sala!=null) {
    		this.sala.anadirPregunta(p);
    	}
    }
    /*
    public void setListo(boolean listo) {
    	this.empezar.setListo(true);
    }
     */
    public void hostearSala(){
        try(ServerSocket ss = new ServerSocket(localPort)){
            ExecutorService pool = Executors.newFixedThreadPool(30);
            //this.empezar = new Empezar(ss);
            //this.empezar.start();
            //CountDownLatch count = new CountDownLatch(30);
            //List<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
            //List<AtenderCliente> listaEjecuciones = new ArrayList<>();
            //HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            int i = 30;
            while (true) {
                try{
                	//mandar las preguntas a los jugadores y recoger sus respuestas 
//                    if(i==0 || listo){
//                        resultados=pool.invokeAll(listaEjecuciones);
//                        HashMap<Persona, Integer> aux;
//                        for (int j = 0; j<resultados.size(); j++){
//                            tablaPuntuaciones.putAll((HashMap<Persona, Integer>) resultados.get(j));
//                        }
//                        TreeMap<Persona, Integer> sorted = new TreeMap<>(tablaPuntuaciones);
//                        System.out.println("Puntuaciones:");
//                        for(Persona p: sorted.keySet()){
//                            System.out.println(p.getIp() + " : " + sorted.get(p));
//                        }
//                    }
                	if(i>0) {
                		Socket cliente = ss.accept();
                		i--;
                		listaEjecuciones.add(new AtenderCliente(cliente, this.sala));
                	}

                }catch(IOException ex){
                    ex.printStackTrace();
                } 
            }
            // try{
            //     Socket cliente = ss.accept();
            //     Thread hilo = new Thread(){
            //         public void run() {
            //             atenderCliente(cliente, sala);
            //         }
            //     }; 
            //     hilo.start();
            // }catch(IOException e){
            //     e.printStackTrace();
            // }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public HashMap<Persona, Integer> enviarPreguntasRecogerRespuestas() {
    	
    	List<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
    	HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
    	try {
			resultados=pool.invokeAll(listaEjecuciones);

			HashMap<Persona, Integer> aux;
			for (int j = 0; j<resultados.size(); j++){
			    tablaPuntuaciones.putAll((HashMap<Persona, Integer>) resultados.get(j));
			}
			
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return tablaPuntuaciones;
    }
    
    public HashMap<String, Persona> getSalas(){
    	return this.salas;
    }
    
    public String unirseASala() {
    	String idSala ="";
		try(Socket cliente = new Socket("localhost", 8000);
                ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
                DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
			
			outSocket.writeInt(2);
			salas = (HashMap<String, Persona>) inSocket.readObject();
 
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sala = new Sala(idSala);
		return idSala;
    }

    public static void jugarEnSala(Persona p){
        try(Socket s = new Socket(p.getIp(), p.getPuerto());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            Scanner teclado = new Scanner(System.in);){
        	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            outSocket.writeObject(p);
            int puntos = 0;
            int numPreg = inSocket.readInt();
            Pregunta pregunta;
            Respuesta respuesta;
            String opcion;
            for (int i = 0; i<numPreg; i++){
                pregunta = (Pregunta) inSocket.readObject();
                //CountDown countdown = new CountDown();
                //scheduler.scheduleAtFixedRate(countdown, 0, 1, SECONDS);
                System.out.println(pregunta.toString());
                do{
                    System.out.println("Introduce la respuesta correcta (a, b, c, d):");
                    opcion = teclado.nextLine();
                    System.out.println(opcion);
                }while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d"));
                respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
                outSocket.writeObject(respuesta);
                outSocket.flush();
                outSocket.writeBytes("He respondido\n");
                outSocket.flush();
                puntos = inSocket.readInt();
                System.out.println("Tienes "+ puntos + " puntos!");
                scheduler.shutdown();
            }

        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Sala getSala() {
    	return this.sala;
    }
}
