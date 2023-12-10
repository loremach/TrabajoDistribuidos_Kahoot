package conexiones.clasesClienteGrafico;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dominio.Pregunta;
import dominio.Respuesta;

public class ResponderPregunta2 extends Thread{

    private Pregunta pregunta; 
    private Respuesta respuesta;
    private JPanel panelPreguntaPuntos; 
    private ArrayList<String> respuestas;

    public ResponderPregunta2(Pregunta pregunta, JPanel panelPreguntaPuntos){
        this.pregunta = pregunta;
        this.respuesta = new Respuesta();
        this.panelPreguntaPuntos = panelPreguntaPuntos;
        this.respuestas = (ArrayList<String>) pregunta.getRespuestasDesordenadas();
    }

    /**
     * Ejecuta la espera de una respuesta a una pregunta mostrada en un JLabel.
     * Espera hasta que se proporcione una respuesta válida.
     * 
     * @param pregunta El objeto Pregunta que contiene la pregunta a mostrar.
     */
    public void run(){
        JLabel lblPregunta = (JLabel) this.panelPreguntaPuntos.getComponent(0);
        lblPregunta.setText(pregunta.getPregunta());

        while(this.respuesta.getRespuesta().equals("")){}
    }

    public Respuesta getRespuesta(){
        return this.respuesta;
    }

    public void setRespuesta(int opcion){
        this.respuesta = new Respuesta(respuestas.get(opcion));
    }
}
