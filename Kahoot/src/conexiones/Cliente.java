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

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;

public class Cliente extends JFrame implements KeyListener{
    private static Sala sala = null;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    private static HashMap<String, Persona> salas;
    private static boolean listo = false;
    public static void main(String[] args) {
        int opcion = 0;
        Sala sala;
        Scanner teclado = new Scanner(System.in);
        String idSala = "";
        try(Socket cliente = new Socket("localhost", 8000);
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
                do{
                    System.out.println("Elija una opción:");
                    System.out.println("1. Crear una sala.");
                    System.out.println("2. Unirse a una sala.");
                    System.out.println("3. Salir.");
                    opcion = teclado.nextInt();
                    teclado.nextLine();
                }while(opcion!=1 && opcion!=2 && opcion!=3);
                outSocket.writeInt(opcion);
                outSocket.flush();
                switch (opcion) {
                    case 1:
                        String salaCreada = inSocket.readLine();
                        if(salaCreada.equals("Has creado la sala.")){
                            localPort = cliente.getLocalPort();
                            System.out.println(salaCreada);
                            idSala = inSocket.readLine();
                            
                            System.out.println("El ID de la sala es: " + idSala);
                            
                        }else{
                            System.out.println(salaCreada);
                        }
                        break;
                    case 2:
                        salas = (HashMap<String, Persona>) inSocket.readObject();
                        break;
                    case 3:
                        break;
                }
            }catch(IOException ex){
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            switch (opcion) {
                case 1:
                    sala = new Sala(idSala);
                    String seguir;
                    String pregunta; String resCorrecta; String resInc1; String resInc2; String resInc3;
                    do{
                        System.out.println("Introduce la pregunta:");
                        pregunta = teclado.nextLine();
                        System.out.println("Introduce la respuesta correcta:");
                        resCorrecta = teclado.nextLine();
                        System.out.println("Introduce la respuesta incorrecta 1");
                        resInc1 = teclado.nextLine();
                        System.out.println("Introduce la respuesta incorrecta 2");
                        resInc2 = teclado.nextLine();
                        System.out.println("Introduce la respuesta incorrecta 3");
                        resInc3 = teclado.nextLine();
                        
                        Pregunta p = new Pregunta(pregunta, resCorrecta, resInc1, resInc2, resInc3);
                        sala.anadirPregunta(p);
                        System.out.println("Has añadido la pregunta");

                        System.out.println("¿Quieres añadir otra pregunta? (S/N)");
                        seguir = teclado.nextLine();
                    }while(!seguir.equals("S") && !seguir.equals("N"));
                    System.out.println(localPort);
                    hostearSala(sala);
                    
                break;
            
                case 2: 
                    System.out.println("Introduce el ID de la sala:");
                    idSala = teclado.nextLine();
                    if(salas.containsKey(idSala)){
                        Persona p = salas.get(idSala);
                        System.out.println("Puerto: "+p.getPuerto());
                        jugarEnSala(p);
                    }else{
                        System.out.println("La sala no existe.");
                    }
                break;

                case 3:
                break;
            }


            
    }

    private static void hostearSala(Sala sala){

        JFrame myJFrame = new JFrame();

        myJFrame.addKeyListener(new KeyAdapter() 
        {
            public void keyPressed(KeyEvent evt)
            {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    listo=true;
                }
            }
        });

        try(ServerSocket ss = new ServerSocket(localPort)){
            ExecutorService pool = Executors.newFixedThreadPool(30);
            //CountDownLatch count = new CountDownLatch(30);
            List<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
            List<AtenderCliente> listaEjecuciones = new ArrayList<>();
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            int i = 2;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    listaEjecuciones.add(new AtenderCliente(cliente, sala));
                    //mandar las preguntas a los jugadores y recoger sus respuestas 
                    if(i==0 || listo){
                        resultados=pool.invokeAll(listaEjecuciones);
                    }
                    HashMap<Persona, Integer> aux;
                    for (int j = 0; j<resultados.size(); j++){
                        tablaPuntuaciones.putAll((HashMap<Persona, Integer>) resultados.get(j));
                    }
                    TreeMap<Persona, Integer> sorted = new TreeMap<>(tablaPuntuaciones);
                    System.out.println("Puntuaciones:");
                    for(Persona p: sorted.keySet()){
                        System.out.println(p.getIp() + " : " + sorted.get(p));
                    }
                    
                }catch(IOException ex){
                    ex.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

    private static void jugarEnSala(Persona p){
        try(Socket s = new Socket(p.getIp(), p.getPuerto());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            Scanner teclado = new Scanner(System.in);){
            
            outSocket.writeObject(p);
            int puntos = 0;
            int numPreg = inSocket.readInt();
            Pregunta pregunta;
            Respuesta respuesta;
            String opcion;
            for (int i = 0; i<numPreg; i++){
                pregunta = (Pregunta) inSocket.readObject();
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
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            listo = true;
        }
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }
}
