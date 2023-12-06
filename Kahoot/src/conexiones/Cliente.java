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
import java.util.Timer;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import utils.CountDown;
import utils.Empezar;

public class Cliente {
    private static Sala sala = null;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    private static HashMap<String, Persona> salas;
    private static boolean listo = false;
    private static Persona persona;
    private static HashMap<Persona, Socket> clientesConectados = new HashMap<>();
    public static void main(String[] args) {
        //POR AQUI LO SEPARADO
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;
        do{
            System.out.println("Elija una opción:");
            System.out.println("1. Crear una sala.");
            System.out.println("2. Unirse a una sala.");
            System.out.println("3. Salir.");
            opcion = teclado.nextInt();
            teclado.nextLine();
        }while(opcion!=1 && opcion!=2 && opcion!=3);
        switch (opcion) {
            case 1:
                crearSala(opcion);
                anadirPreguntas(teclado);
                teclado.close();
                hostearSala(sala);
                break;
            case 2:
                conectarConSala(teclado);
                teclado.close();
                break;
            default:
                teclado.close();
                return;
        }
        
        //HASTA AQUI

    }

    private static void crearSala(int opcion){
        try(Socket cliente = new Socket("localhost", 8000);
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
                outSocket.writeInt(opcion);
                outSocket.flush();
              
                String salaCreada = inSocket.readLine();
                if(salaCreada.equals("Has creado la sala.")){
                    localPort = cliente.getLocalPort();
                    System.out.println(salaCreada);
                    sala = new Sala(inSocket.readLine());
                    System.out.println("El ID de la sala es: " + sala.getIdSala());
                }else{
                    System.out.println(salaCreada);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
    }

    public static void anadirPreguntas(Scanner teclado){
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
    }

    private static void hostearSala(Sala sala){
        try(ServerSocket ss = new ServerSocket(localPort)){
            ExecutorService pool = Executors.newFixedThreadPool(20);
            //CountDownLatch count = new CountDownLatch(20);
            List<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
            List<AtenderCliente> listaEjecuciones = new ArrayList<>();
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            Empezar empezar = new Empezar();
            EnviarPreguntas enviarPreguntas = new EnviarPreguntas(clientesConectados, sala, listo);
            empezar.start();
            enviarPreguntas.start();
            int i = 20;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());
                    if(!listo){
                        ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
                        outSocket.writeBoolean(true);
                        Persona p = (Persona) inSocket.readObject();
                        tablaPuntuaciones.put(p, 0);
                        clientesConectados.put(p, cliente);
                        outSocket.writeInt(sala.getPreguntas().size());
                        outSocket.flush();
                    }else{
                        outSocket.writeBoolean(false);
                    }

                    // listaEjecuciones.add(new AtenderCliente(cliente, sala));
                    // //mandar las preguntas a los jugadores y recoger sus respuestas 
                    // if(i==0 || listo){
                    //     resultados=pool.invokeAll(listaEjecuciones);
                    // }
                    // HashMap<Persona, Integer> aux;
                    // for (int j = 0; j<resultados.size(); j++){
                    //     tablaPuntuaciones.putAll((HashMap<Persona, Integer>) resultados.get(j));
                    // }
                    // TreeMap<Persona, Integer> sorted = new TreeMap<>(tablaPuntuaciones);
                    // System.out.println("Puntuaciones:");
                    // for(Persona p: sorted.keySet()){
                    //     System.out.println(p.getIp() + " : " + sorted.get(p));
                    // }
                    
                }catch(IOException ex){
                    ex.printStackTrace();
                // } catch (InterruptedException e) {
                //     // TODO Auto-generated catch block
                //     e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private static void conectarConSala(Scanner teclado){
        try(Socket cliente = new Socket("localhost", 8000);
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
             
            salas = (HashMap<String, Persona>) inSocket.readObject();
            System.out.println("Introduce el ID de la sala:");
            String idSala = teclado.nextLine();
            if(salas.containsKey(idSala)){
                Persona p = salas.get(idSala);
                System.out.println("Puerto: "+p.getPuerto());
                jugarEnSala(p);
            }else{
                System.out.println("La sala no existe.");
            }
                    
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private static void jugarEnSala(Persona p){
        try(Socket s = new Socket(p.getIp(), p.getPuerto());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());){
            
            outSocket.writeObject(p);
            int puntos = 0;
            int numPreg = inSocket.readInt();
            Pregunta pregunta;
            Respuesta respuesta;
            String opcion;
            for (int i = 0; i<numPreg; i++){
                pregunta = (Pregunta) inSocket.readObject();
                Timer timer = new Timer();
                
                int segundos = 60;
                Calendar init = Calendar.getInstance();
                ResponderPregunta responder = new ResponderPregunta(pregunta);
                responder.start();
                timer.scheduleAtFixedRate(new CountDown(segundos, timer, responder), init.getTime(), 10000);
                // System.out.println(pregunta.toString());
                // do{
                //     System.out.println("Introduce la respuesta correcta (a, b, c, d):");
                //     opcion = teclado.nextLine();
                //     System.out.println(opcion);
                // }while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d"));
                // respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
                respuesta = responder.getRespuesta();

                outSocket.writeObject(respuesta);
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
