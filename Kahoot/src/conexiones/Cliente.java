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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;

public class Cliente {
    private static String idSala;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    public static void main(String[] args) {
        int opcion = 0;
        try(Scanner teclado = new Scanner(System.in);
            Socket cliente = new Socket("localhost", 8000);
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
                switch (opcion) {
                    case 1:
                        String salaCreada = inSocket.readLine();
                        if(salaCreada.equals("Has creado la sala.")){
                            localPort = cliente.getLocalPort();
                            System.out.println(salaCreada);
                            idSala = inSocket.readLine();
                            Sala sala = new Sala(idSala);
                            System.out.println("El ID de la sala es: " + idSala);
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
                            cliente.close();
                            hostearSala(sala);
                        }else{
                            System.out.println(salaCreada);
                        }
                    break;
                
                    case 2:
                        HashMap<String, Persona> salas = (HashMap<String, Persona>) inSocket.readObject();
                        System.out.println("Introduce el ID de la sala:");
                        String idSala = teclado.nextLine();
                        if(salas.containsKey(idSala)){
                            Persona p = salas.get(idSala);
                            cliente.close();
                            jugarEnSala(p);
                        }else{
                            System.out.println("La sala no existe.");
                        }
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
    }

    private static void hostearSala(Sala sala){
        try(ServerSocket ss = new ServerSocket(localPort)){
            // ExecutorService pool = Executors.newFixedThreadPool(30);
            // CountDownLatch count = new CountDownLatch(30);

            // while (true) {
            //     try{
            //         Socket cliente = ss.accept();
            //         //mandar las preguntas a los jugadores y recoger sus respuestas 
            //         pool.execute(new Runnable(){
            //             public void run(){
            //                 atenderCliente(cliente, sala);
            //             }
            //         });
                    
            //     }catch(IOException ex){
            //         ex.printStackTrace();
            //     }
            //     count.await();
            // }

            try{
                Socket cliente = ss.accept();
                Thread hilo = new Thread(new Runnable() {
                    public void run() {
                        atenderCliente(cliente, sala);
                    }
                }); 

            }catch(IOException e){
                e.printStackTrace();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


    private static int atenderCliente(Socket cliente, Sala sala){
        int puntos = 0;
        try(ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());){
            
            //Falta añadir coundowns para que empiecen a la vez
            List<Pregunta> listaPreguntas = sala.getPreguntas();
            outSocket.writeInt(listaPreguntas.size());
            outSocket.flush();
            Calendar start;
            for(Pregunta pregunta: listaPreguntas){
                start = Calendar.getInstance();
                outSocket.writeObject(pregunta);
                outSocket.flush();
                Respuesta respuesta = (Respuesta) inSocket.readObject();
                if(pregunta.esCorrecta(respuesta.getRespuesta())){
                    long segundos = Math.round((start.getTimeInMillis()-respuesta.getTiempoRespuesta().getTimeInMillis())/1000);
                    puntos += segundos;
                }
                System.out.println(inSocket.readLine());
                outSocket.writeInt(puntos);
                outSocket.flush();
            } 
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return puntos;
    }


    private static void jugarEnSala(Persona p){
        try(Socket s = new Socket(p.getIp(), p.getPuerto());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            Scanner teclado = new Scanner(System.in);){
            
            int puntos = 0;
            int numPreg = inSocket.readInt();
            Pregunta pregunta;
            Respuesta respuesta;
            String opcion;
            for (int i = 0; i<numPreg; i++){
                pregunta = (Pregunta) inSocket.readObject();
                System.out.println(pregunta.toString());
                System.out.println("Introduce la respuesta:");
                do{
                    opcion = teclado.nextLine();
                }while (opcion!="a" && opcion!="b" && opcion!="c" && opcion!="d");
                respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
                outSocket.writeObject(respuesta);
                outSocket.flush();
                outSocket.writeBytes("He respondido");
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
}
