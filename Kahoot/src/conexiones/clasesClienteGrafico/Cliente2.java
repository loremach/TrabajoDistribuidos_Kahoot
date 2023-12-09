package conexiones.clasesClienteGrafico;

import java.awt.Color;
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
import javax.swing.JTextPane;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import dominio.SocketCliente;
import utils.CountDown;

public class Cliente2 {
    private Sala sala = null;
    private int localPort=0;
    private HashMap<String, Persona> salas;
    private HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    private Respuesta respuestaJugador;
    private ArrayList<String> posiblesRespuestas;
    private int puntosFinales=-1;
    private boolean listo = false;

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

    public void hostearSala(JTextArea textPuntuaciones){//Empezar2 empezar){
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
                    System.out.println("Listo vale "+this.listo);//empezar.getListo());
                    if(!this.listo){//!empezar.getListo()){
                        outSocket.writeBoolean(true);
                        outSocket.flush();
                        Persona p = (Persona) inSocket.readObject();
                        if(textPuntuaciones.getText().equals("Debe haber un jugador en la sala como mínimo")) textPuntuaciones.setText("");
                        textPuntuaciones.setText(textPuntuaciones.getText() + "\n" + p.getAlias());
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
        ArrayList<Persona> devolver = null;
        if(personaHost!=null && personaJugador!=null){
            devolver = new ArrayList<>();
            devolver.add(personaHost); devolver.add(personaJugador);
        }
        return devolver;
        //if(personaHost!=null && personaJugador!=null) jugarEnSala(personaHost, personaJugador);
    }

    public void jugarEnSala(Persona personaHost, Persona personaJugador, JPanel panelPreguntaPuntos, JPanel panelBotonesRespuesta){
        try(Socket s = new Socket(personaHost.getIp(), personaHost.getPuerto());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());){
            
            boolean conectado = inSocket.readBoolean();
            System.out.println("Me he conectado correctamente: "+conectado);
            if(conectado) {
                outSocket.writeObject(personaJugador);
                outSocket.flush();
                int puntos = 0;
                int numPreg = inSocket.readInt();
                Pregunta pregunta;
                Respuesta respuesta;
                ArrayList<Color> coloresBotones= new ArrayList<>();
                coloresBotones.add(new Color(41, 167, 241));
                coloresBotones.add(new Color(255, 0, 0));
                coloresBotones.add(new Color(255, 255, 0));
                coloresBotones.add(new Color(0, 187, 94));

                for (int i = 0; i<numPreg; i++){
                    pregunta = (Pregunta) inSocket.readObject();
                    this.posiblesRespuestas = pregunta.getRespuestasDesordenadas();
                    for(int k = 0; k<posiblesRespuestas.size(); k++){
                        JButton boton = (JButton) panelBotonesRespuesta.getComponent(k);
                        boton.setBackground(coloresBotones.get(k));
                        boton.setEnabled(true);
                        boton.setText(this.posiblesRespuestas.get(k));
                    }
                    JTextPane textPregunta = (JTextPane) panelPreguntaPuntos.getComponent(0);
                    textPregunta.setText(pregunta.getPregunta());

                    Timer timer = new Timer();
                    CyclicBarrier barrera = new CyclicBarrier(2);
                    int segundos = 30;
                    Calendar init = Calendar.getInstance();
                    
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
                this.puntosFinales = puntos;
            }else{
                JTextPane textPregunta = (JTextPane) panelPreguntaPuntos.getComponent(0);
                textPregunta.setText("La partida ya ha comenzado");
                for(int k = 0; k<4; k++){
                    JButton boton = (JButton) panelBotonesRespuesta.getComponent(k);
                    boton.setVisible(false);
                }
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

    public int getPuntosFinales(){
        return this.puntosFinales;
    }

    public boolean getListo(){
        return this.listo;
    }

    public void setListo(boolean listo){
        this.listo = listo;
    }
}
