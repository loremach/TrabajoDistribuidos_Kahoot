package conexiones;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;

public class EnviadorPregunta implements Callable<HashMap<Persona, Integer>>{
    
    private Socket cliente;
    private Persona persona;
    private Pregunta pregunta;
    private CyclicBarrier barrera; 


    public EnviadorPregunta(Socket cliente, Persona persona, Pregunta pregunta, CyclicBarrier barrera){
        this.cliente = cliente;
        this.persona = persona;
        this.pregunta = pregunta;
        this.barrera = barrera;
    }

    @Override
    public HashMap<Persona, Integer> call() throws Exception {
        barrera.await();
        int puntos = 0;
        try{
            ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());

            outSocket.writeObject(pregunta);
            outSocket.flush();
            Respuesta respuesta = (Respuesta) inSocket.readObject();
            if(pregunta != null){
                if(pregunta.esCorrecta(respuesta.getRespuesta())){
                puntos = Math.round(60 - respuesta.getTiempoRespuesta().getTimeInMillis()/1000);
                }
            }
            System.out.println(inSocket.readLine());
            outSocket.writeInt(puntos);
            outSocket.flush();
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<Persona, Integer> personaResultado = new HashMap<>();
        personaResultado.put(this.persona, puntos);
        return personaResultado;
    }
}
