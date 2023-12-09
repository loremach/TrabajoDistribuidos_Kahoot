package conexiones;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import conexiones.clasesClienteConsola.Empezar;
import conexiones.clasesClienteConsola.HostearSala;
import conexiones.clasesClienteConsola.ResponderPregunta;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import dominio.SocketCliente;
import utils.CountDown;

public class ClienteConsola {
    private static Sala sala = null;
    private static int localPort=0;
    private static HashMap<String, Persona> salas;
    private static HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    public static void main(String[] args) {
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
                hostearSala(sala);
                System.out.println("HE PASADO POR AQUI QUE ES EL FINAL");
                break;
            case 2:
                conectarConSala(teclado, opcion);
                break;
            default:
                teclado.close();
                return;
        }
        teclado.close();
        System.out.println("YA NO HAY MAS");
        return;
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
    }

    private static void hostearSala(Sala sala){
        try{
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            Empezar empezar = new Empezar(clientesConectados, sala);
            HostearSala hostear = new HostearSala(localPort, clientesConectados, sala, tablaPuntuaciones, empezar);
            hostear.start();
            empezar.start();
            empezar.join();
            hostear.interrupt();
            System.out.println("HE PASADO POR AQUI");
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        try(Socket s = new Socket(personaHost.getIp(), personaHost.getPuerto());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());){
            
            boolean conectado = inSocket.readBoolean();
            if(conectado) {
                System.out.println("Te has conectado");
                outSocket.writeObject(personaJugador);
                outSocket.flush();
                int puntos = 0;
                int numPreg = inSocket.readInt();
                Pregunta pregunta;
                Respuesta respuesta;

                for (int i = 0; i<numPreg; i++){
                    System.out.println("Esperando pregunta...");
                    pregunta = (Pregunta) inSocket.readObject();
                    Timer timer = new Timer();
                    CyclicBarrier barrera = new CyclicBarrier(2);
                    int segundos = 30;
                    Calendar init = Calendar.getInstance();
                    ResponderPregunta responder = new ResponderPregunta(pregunta);
                    responder.start();
                    timer.scheduleAtFixedRate(new CountDown(segundos, timer, responder, barrera), init.getTime(), 1000);

                    barrera.await();
                    respuesta = responder.getRespuesta();
                    
                    outSocket.writeObject(respuesta);
                    outSocket.flush();

                    puntos = puntos + inSocket.readInt();
                    System.out.println("Tienes "+ puntos + " puntos!");
                }
            }else{
                System.out.println("La partida ya ha empezado");
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
