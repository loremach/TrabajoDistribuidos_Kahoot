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
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import utils.Countdown;

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

public class Cliente2 {
    private Sala sala = null;
    private int localPort=0;
    private HashMap<String, Persona> salas;
    private HashMap<Persona, SocketCliente> clientesConectados = new HashMap<>();
    private Respuesta respuestaJugador;
    private ArrayList<String> posiblesRespuestas;
    private int puntosFinales=-1;
    private boolean listo = false;

    /**
     * Crea una sala en el servidor y devuelve su ID.
     *
     * @param opcion La opción que indica al servidor que se desea crear una sala.
     * @return El ID de la sala creada o una cadena vacía si la creación falla.
     */
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

    /**
     * Aloja una sala de juego, aceptando conexiones de clientes y gestionando su interacción.
     *
     * @param textPuntuaciones El área de texto de la interfaz donde se muestran las puntuaciones y jugadores.
     */
    public void hostearSala(JTextArea textPuntuaciones){//Empezar2 empezar){
        try(ServerSocket ss = new ServerSocket(this.localPort)){
            HashMap<Persona, Integer> tablaPuntuaciones = new HashMap<>();
            int i = 20; // Número máximo de conexiones permitidas
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
                    ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());

                    SocketCliente socketCliente = new SocketCliente(cliente, inSocket, outSocket);

                    if(!this.listo){ // Si la sala no ha empezado el juego
                        outSocket.writeBoolean(true);
                        outSocket.flush();
                        Persona p = (Persona) inSocket.readObject();

                        // Mostrar el alias del jugador en el área de texto de puntuaciones
                        if(textPuntuaciones.getText().equals("Debe haber un jugador en la sala como mínimo")) textPuntuaciones.setText("");
                        textPuntuaciones.setText(textPuntuaciones.getText() + "\n        " + p.getAlias());
                         // Registrar al jugador y guardar su objeto socketCliente
                        tablaPuntuaciones.put(p, 0);
                        clientesConectados.put(p, socketCliente);
                        // Enviar al cliente la cantidad de preguntas disponibles en la sala de juego
                        outSocket.writeInt(sala.getPreguntas().size());
                        outSocket.flush();
                    }else{
                        // Si la sala ha empezado el juego, rechazar la conexión del cliente
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

    /**
     * Conecta un cliente a una sala específica en el servidor y devuelve las personas involucradas
     * en la conexión con la sala, que son el cliente que hace de host y el proprio jugador.
     *
     * @param opcion La opción para que el servidor sepa que se quiere entrar una sala.
     * @param idSala El identificador de la sala a la que se desea conectar.
     * @param alias El alias del jugador que desea unirse a la sala.
     * @return Un ArrayList de Personas que contiene al anfitrión de la sala y al jugador conectado,
     *         o null si no se puede realizar la conexión.
     */
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
    }

    /**
     * Gestiona la interacción del jugador con una sala de juego, permitiendo la jugabilidad.
     * 
     * @param personaHost El anfitrión de la sala de juego.
     * @param personaJugador El jugador que se une a la sala de juego.
     * @param panelPreguntaPuntos El panel que muestra la pregunta y los puntos del jugador.
     * @param panelBotonesRespuesta El panel que contiene los botones de respuesta para el jugador.
     */
    public void jugarEnSala(Persona personaHost, Persona personaJugador, JPanel panelPreguntaPuntos, JPanel panelBotonesRespuesta){
        try(Socket s = new Socket(personaHost.getIp(), personaHost.getPuerto());
            ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(s.getInputStream());){
            
            boolean conectado = inSocket.readBoolean(); // Verifica la conexión con el anfitrión
            System.out.println("Me he conectado correctamente: "+conectado);
            if(conectado) {
                outSocket.writeObject(personaJugador); // Envía al servidor la información del jugador
                outSocket.flush();
                int puntos = 0;
                int numPreg = inSocket.readInt(); // Lee la cantidad de preguntas que habrá
                Pregunta pregunta;
                Respuesta respuesta;
                ArrayList<Color> coloresBotones= new ArrayList<>();
                // Colores para los botones de respuesta
                coloresBotones.add(new Color(41, 167, 241));
                coloresBotones.add(new Color(255, 0, 0));
                coloresBotones.add(new Color(255, 255, 0));
                coloresBotones.add(new Color(0, 187, 94));

                for (int i = 0; i<numPreg; i++){
                    // Recoje la pregunta y sus respuestas desordenadas
                    pregunta = (Pregunta) inSocket.readObject();
                    this.posiblesRespuestas = pregunta.getRespuestasDesordenadas();
                    // Actualiza los botones de respuesta con colores y texto
                    for(int k = 0; k<posiblesRespuestas.size(); k++){
                        JButton boton = (JButton) panelBotonesRespuesta.getComponent(k);
                        boton.setBackground(coloresBotones.get(k));
                        boton.setEnabled(true);
                        boton.setText(this.posiblesRespuestas.get(k));
                    }
                    JTextPane textPregunta = (JTextPane) panelPreguntaPuntos.getComponent(0);
                    textPregunta.setText(pregunta.getPregunta()); // Muestra la pregunta

                    // Configuración del temporizador para la pregunta
                    Timer timer = new Timer();
                    CyclicBarrier barrera = new CyclicBarrier(2);
                    int segundos = 20;
                    Calendar init = Calendar.getInstance();
                    
                    timer.scheduleAtFixedRate(new Countdown(segundos, timer, barrera, panelPreguntaPuntos), init.getTime(), 1000);
                    barrera.await();
                    if(this.respuestaJugador == null){
                        respuesta = new Respuesta();
                    }else{
                        respuesta = this.respuestaJugador;
                    }
                    
                    outSocket.reset();
                    outSocket.writeObject(respuesta); // Envía la respuesta del jugador al servidor
                    outSocket.flush();

                    puntos = puntos + inSocket.readInt(); // Lee los puntos obtenidos del jugador anfitrión ylos suma a los puntos que ya tenía

                    JLabel lblPuntos = (JLabel) panelPreguntaPuntos.getComponent(1);
                    lblPuntos.setText("Puntos: " + puntos); // Muestra los puntos acumulados del jugador
                }
                this.puntosFinales = puntos; // Almacena los puntos finales obtenidos por el jugador
            }else{
                // Si no se puede conectar, muestra un mensaje en el panel de pregunta y deshabilita los botones de respuesta
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

    public void aniadirPreguntaASala(Pregunta pregunta){
        sala.anadirPregunta(pregunta);       
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
