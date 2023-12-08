package conexiones;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.SocketCliente;

public class EnviadorPregunta implements Callable<HashMap<Persona, Integer>>{
    
    private SocketCliente cliente;
    private Persona persona;
    private Pregunta pregunta;
    private CyclicBarrier barrera; 
    private CountDownLatch recoger;


    public EnviadorPregunta(SocketCliente cliente, Persona persona, Pregunta pregunta, CyclicBarrier barrera, CountDownLatch recoger){
        this.cliente = cliente;
        this.persona = persona;
        this.pregunta = pregunta;
        this.barrera = barrera;
        this.recoger = recoger;
    }

    @Override
    public HashMap<Persona, Integer> call() throws Exception {
        barrera.await();
        int puntos = 0;

        ObjectOutputStream outSocket = cliente.getObjectOutputStream();
        ObjectInputStream inSocket = cliente.getObjectInputStream();
        
        outSocket.writeObject(pregunta);
        outSocket.flush();
        Calendar init = Calendar.getInstance();
        Respuesta respuesta = (Respuesta) inSocket.readObject();
        if(!respuesta.getRespuesta().equals("")){
            if(pregunta.esCorrecta(respuesta.getRespuesta())){
                puntos = Math.round(30 - (respuesta.getTiempoRespuesta().getTimeInMillis()-init.getTimeInMillis())/1000);
            }
        }

        outSocket.writeInt(puntos);
        outSocket.flush();

        HashMap<Persona, Integer> personaResultado = new HashMap<>();
        personaResultado.put(this.persona, puntos);
        recoger.countDown();
        return personaResultado;
    }
}
