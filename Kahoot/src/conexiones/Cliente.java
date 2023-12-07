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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import dominio.SocketCliente;
import utils.CountDown;
import utils.Empezar;

public class Cliente {
    private static Sala sala = null;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    private static String ipLocal = "";
    private static HashMap<String, Persona> salas;
    private static boolean listo = false;
    private static Persona persona;
    //private static HashMap<Persona, Socket> clientesConectados = new HashMap<>();
    private static HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
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
                conectarConSala(teclado, opcion);
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
                    System.out.println(cliente.getInetAddress().getHostAddress()); 
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
        boolean otraPreg = true;
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

            do{
                System.out.println("¿Quieres añadir otra pregunta? (S/N s/n)");
                seguir = teclado.nextLine();
            }while(!seguir.equals("S") && !seguir.equals("N") && !seguir.equals("s") && !seguir.equals("n"));

            if(seguir.equals("N")|| seguir.equals("n")) otraPreg=false;
        } while(otraPreg);

        System.out.println(localPort);
        hostearSala(sala);       
    }

    private static void hostearSala(Sala sala){
        try(ServerSocket ss = new ServerSocket(localPort)){
            //ExecutorService pool = Executors.newFixedThreadPool(20);
            //CountDownLatch count = new CountDownLatch(20);
            //List<Future<HashMap<Persona,Integer>>> resultados = new ArrayList<>();
            //List<AtenderCliente> listaEjecuciones = new ArrayList<>();
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            boolean listo = false;
            Empezar empezar = new Empezar(clientesConectados, sala, listo);
            //EnviarPreguntas enviarPreguntas = new EnviarPreguntas(clientesConectados, sala, listo);
            empezar.start();
            //enviarPreguntas.start();
            int i = 20;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
                    ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());

                    SocketCliente socketCliente = new SocketCliente(cliente, inSocket, outSocket);
                    if(!listo){
                        outSocket.writeBoolean(true);
                        outSocket.flush();
                        Persona p = (Persona) inSocket.readObject();
                        System.out.println(p.getAlias() + " se ha conectado");
                        tablaPuntuaciones.put(p, 0);
                        //clientesConectados.put(p, cliente);
                        clientesConectados.put(p, socketCliente);
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

    private static void conectarConSala(Scanner teclado, int opcion){
        Persona personaHost = null;
        Persona personaJugador = null;
        try(Socket cliente = new Socket("localhost", 8000);
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());){
            
            outSocket.writeInt(opcion);
            outSocket.flush();

            salas = (HashMap<String, Persona>) inSocket.readObject();
            System.out.println("Introduce el ID de la sala:");
            String idSala = teclado.nextLine();
            if(salas.containsKey(idSala)){
                personaHost = salas.get(idSala);
                System.out.println("Puerto: "+personaHost.getPuerto());

                System.out.println("Introduce tu alias:");
                String alias = teclado.nextLine();
                personaJugador = new Persona(cliente.getInetAddress().getHostAddress(), cliente.getLocalPort(), alias);
            }else{
                System.out.println("La sala no existe.");
            }
                    
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(personaHost!=null && personaJugador!=null) jugarEnSala(personaHost, personaJugador);
    }

    private static void jugarEnSala(Persona personaHost, Persona personaJugador){
        System.out.println("he entrado al método");
        try(Socket s = new Socket(personaHost.getIp(), personaHost.getPuerto());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());){
            
            boolean conectado = inSocket.readBoolean();
            if(conectado) System.out.println("Me he conectado");
            System.out.println("He llegado a aqui");
            outSocket.writeObject(personaJugador);
            outSocket.flush();
            int puntos = 0;
            int numPreg = inSocket.readInt();
            Pregunta pregunta;
            Respuesta respuesta;

            for (int i = 0; i<numPreg; i++){
                System.out.println("Esperando pregunta");
                pregunta = (Pregunta) inSocket.readObject();
                Timer timer = new Timer();
                CyclicBarrier barrera = new CyclicBarrier(2);
                int segundos = 10;
                Calendar init = Calendar.getInstance();
                ResponderPregunta responder = new ResponderPregunta(pregunta);
                responder.start();
                timer.scheduleAtFixedRate(new CountDown(segundos, timer, responder, barrera), init.getTime(), 1000);
                // System.out.println(pregunta.toString());
                // do{
                //     System.out.println("Introduce la respuesta correcta (a, b, c, d):");
                //     opcion = teclado.nextLine();
                //     System.out.println(opcion);
                // }while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d"));
                // respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
                barrera.await();
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
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
