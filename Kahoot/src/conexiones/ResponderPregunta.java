package conexiones;

import java.util.Scanner;
import java.util.Timer;

import dominio.Pregunta;
import dominio.Respuesta;

public class ResponderPregunta extends Thread{

    private Pregunta pregunta; 
    private Respuesta respuesta;


    public ResponderPregunta(Pregunta pregunta){
        this.pregunta = pregunta;
        this.respuesta = new Respuesta();
    }

    public void run(){
        Scanner teclado = new Scanner(System.in);
        System.out.println(pregunta.toString());
        String opcion ="";
        do{
            System.out.println("Introduce la respuesta correcta (a, b, c, d):");
            opcion = teclado.nextLine();
            System.out.println("Has seleccionado la respuesta "+opcion);
        }while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d"));
        teclado.close();
        this.respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
    }

    public Respuesta getRespuesta(){
        return this.respuesta;
    }
}
