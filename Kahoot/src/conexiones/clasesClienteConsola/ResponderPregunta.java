package conexiones.clasesClienteConsola;

import java.util.Scanner;

import dominio.Pregunta;
import dominio.Respuesta;

public class ResponderPregunta extends Thread{

    private Pregunta pregunta; 
    private Respuesta respuesta;

    public ResponderPregunta(Pregunta pregunta){
        this.pregunta = pregunta;
        this.respuesta = new Respuesta();
    }

    /**
     * Permite al usuario ingresar la respuesta correcta a una pregunta seleccionada.
     * Muestra la pregunta y solicita al usuario que ingrese la respuesta correcta (a, b, c, d).
     * Captura la respuesta ingresada por el usuario y la almacena para su uso posterior.
     */
    public void run(){
        Scanner teclado = new Scanner(System.in);
        System.out.println(pregunta.toString());
        String opcion ="";
        do{
            System.out.println("Introduce la respuesta correcta (a, b, c, d):");
            opcion = teclado.nextLine();
        }while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d"));
        System.out.println("Has seleccionado la respuesta "+opcion);
        this.respuesta = new Respuesta(pregunta.getRespuestaSeleccionada(opcion));
    }

    public Respuesta getRespuesta(){
        return this.respuesta;
    }
}
