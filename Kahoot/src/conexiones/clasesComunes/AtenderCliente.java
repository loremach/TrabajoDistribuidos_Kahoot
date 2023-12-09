package conexiones.clasesComunes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;

public class AtenderCliente implements Callable<HashMap<Persona,Integer>> {
    private Persona persona;
    private int puntos;
    private Sala sala;
    private Socket conexion;

    public AtenderCliente(Socket conexion,  Sala sala){
        this.conexion=conexion;
        this.sala=sala;
        this.puntos = 0;
    }

    @Override
    public HashMap<Persona, Integer> call() throws IOException {
        try{
            ObjectOutputStream outSocket = new ObjectOutputStream(conexion.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(conexion.getInputStream());
            
            this.persona = (Persona) inSocket.readObject();
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
                    long segundos = Math.round(60 - respuesta.getTiempoRespuesta().getTimeInMillis()/1000 - start.getTimeInMillis()/1000);
                    this.puntos += segundos;
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
        HashMap<Persona, Integer> personaResultado = new HashMap<>();
        personaResultado.put(this.persona, this.puntos);
        return personaResultado;
    }

}
