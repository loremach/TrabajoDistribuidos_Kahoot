package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pregunta implements Serializable{
	
	private static final long serialVersionUID = 111L;
	private String pregunta;
    private String respuestaCorrecta;
    private String respuestaIncA;
    private String respuestaIncB;
    private String respuestaIncC;
    private List<String> respuestas;
	
	public Pregunta(String pregunta, String respuestaCorrecta, String respuestaIncA, String respuestaIncB, String respuestaIncC) {
		this.pregunta=pregunta;
        this.respuestaCorrecta=respuestaCorrecta;
        this.respuestaIncA=respuestaIncA;
        this.respuestaIncB=respuestaIncB;
        this.respuestaIncC=respuestaIncC;
        this.respuestas = Arrays.asList(respuestaCorrecta, respuestaIncA,
                                                respuestaIncB, respuestaIncB);
	}

	public boolean esCorrecta(String respuestaSeleccionada){
        return respuestaCorrecta.equals(respuestaSeleccionada);
    }

    @Override
    public String toString(){
        List<String> respuestas = Arrays.asList(respuestaCorrecta, respuestaIncA,
                                                respuestaIncB, respuestaIncB);
        Collections.shuffle(respuestas);
        this.respuestas = respuestas;
        return pregunta + "\n" + "a) " + respuestas.get(0) + "\n" + "b) " + respuestas.get(1) + 
                "\n" + "c) " + respuestas.get(2) + "\n" + "d) " + respuestas.get(3);
    }

    public String getRespuestaSeleccionada(String seleccion){
        String respuesta = "";
        switch (seleccion) {
            case "a":
                respuesta = respuestas.get(0);
                break;
            case "b":
                respuesta = respuestas.get(1);
                break;
            case "c":
                respuesta = respuestas.get(2);
                break;
            case "d":
                respuesta = respuestas.get(3);
                break;
        }
        return respuesta;
    }

    public String getPregunta(){
        return this.pregunta;
    }
    

    public ArrayList<String> getRespuestasDesordenadas(){
        List<String> respuestas = Arrays.asList(respuestaCorrecta, respuestaIncA,
                                                respuestaIncB, respuestaIncB);

        Collections.shuffle(respuestas);
        ArrayList<String> devolver = new ArrayList<>(respuestas);
        return devolver;
    }
}