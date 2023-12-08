package conexiones;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import dominio.SocketCliente;
import utils.CountDown;
import utils.Empezar;
import utils.Empezar2;

public class Cliente2 {
    private Sala sala = null;
    private int localPort=0;
    private HashMap<String, Persona> salas;
    private HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    private Respuesta respuestaJugador;
    private ArrayList<String> posiblesRespuestas;

    public String crearSala(int opcion){
        String idSala="";
        try(Socket cliente = new Socket("localhost", 8000);
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
                outSocket.writeInt(opcion);
                outSocket.flush();
              
                String salaCreada = inSocket.readLine();
                if(salaCreada.equals("Has creado la sala.")){
                    localPort = cliente.getLocalPort();
                    this.sala = new Sala(inSocket.readLine());
                    System.out.println("El ID de la sala es: " + sala.getIdSala());
                    idSala = sala.getIdSala();
                }else{
                    System.out.println(salaCreada);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        return idSala;
    }

    public void aniadirPreguntaASala(Pregunta pregunta){
        sala.anadirPregunta(pregunta);       
    }

    public void hostearSala(JTextArea textPuntuaciones, Empezar2 empezar){
        try(ServerSocket ss = new ServerSocket(this.localPort)){
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            int i = 20;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
                    ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());

                    SocketCliente socketCliente = new SocketCliente(cliente, inSocket, outSocket);
                    if(!empezar.getListo()){
                        outSocket.writeBoolean(true);
                        outSocket.flush();
                        Persona p = (Persona) inSocket.readObject();
                        textPuntuaciones.setText(textPuntuaciones + "\n" + p.getAlias());
                        tablaPuntuaciones.put(p, 0);
                        clientesConectados.put(p, socketCliente);
                        outSocket.writeInt(sala.getPreguntas().size());
                        outSocket.flush();
                    }else{
                        outSocket.writeBoolean(false);
                        outSocket.flush();
                    }                    
                }catch(IOException ex){
                    ex.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<Persona> conectarConSala(int opcion, String idSala, String alias){
        Persona personaHost = null;
        Persona personaJugador = null;
        try(Socket cliente = new Socket("localhost", 8000);
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());){
            
            outSocket.writeInt(opcion);
            outSocket.flush();

            this.salas = (HashMap<String, Persona>) inSocket.readObject();
            
            if(this.salas.containsKey(idSala)){
                personaHost = this.salas.get(idSala);
                personaJugador = new Persona(cliente.getInetAddress().getHostAddress(), cliente.getLocalPort(), alias);
            } 
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Persona> devolver = new ArrayList<>();
        devolver.add(personaHost); devolver.add(personaJugador);
        return devolver;
        //if(personaHost!=null && personaJugador!=null) jugarEnSala(personaHost, personaJugador);
    }

    public void jugarEnSala(Persona personaHost, Persona personaJugador, JPanel panelPreguntaPuntos, JPanel panelBotonesRespuesta){
        try(Socket s = new Socket(personaHost.getIp(), personaHost.getPuerto());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());){
            
            boolean conectado = inSocket.readBoolean();
            if(conectado) {
                outSocket.writeObject(personaJugador);
                outSocket.flush();
                int puntos = 0;
                int numPreg = inSocket.readInt();
                Pregunta pregunta;
                Respuesta respuesta;

                for (int i = 0; i<numPreg; i++){
                    pregunta = (Pregunta) inSocket.readObject();
                    this.posiblesRespuestas = (ArrayList<String>) pregunta.getRespuestasDesordenadas();
                    for(int k = 0; k<posiblesRespuestas.size(); k++){
                        JButton boton = (JButton) panelBotonesRespuesta.getComponent(k);
                        boton.setText(this.posiblesRespuestas.get(k));
                    }

                    Timer timer = new Timer();
                    CyclicBarrier barrera = new CyclicBarrier(2);
                    int segundos = 30;
                    Calendar init = Calendar.getInstance();
                    //ResponderPregunta2 responder = new ResponderPregunta2(pregunta, panelPreguntaPuntos);
                    //responder.start();
                    timer.scheduleAtFixedRate(new CountDown(segundos, timer, barrera, panelPreguntaPuntos), init.getTime(), 1000);
                    barrera.await();
                    respuesta = this.respuestaJugador;
                    
                    outSocket.reset();
                    outSocket.writeObject(respuesta);
                    outSocket.flush();

                    puntos = puntos + inSocket.readInt();

                    JLabel lblPuntos = (JLabel) panelPreguntaPuntos.getComponent(1);
                    lblPuntos.setText("Puntos: " + puntos);
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

    public void setRespuestaJugador(int opcion){
        this.respuestaJugador = new Respuesta(this.posiblesRespuestas.get(opcion-1));
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public Sala getSala() {
        return this.sala;
    }

    public HashMap<Persona, SocketCliente> getClientesConectados() {
        return this.clientesConectados;
    }

    public Object getSalas() {
        return null;
    }

}
