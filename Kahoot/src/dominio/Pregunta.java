package dominio;

import java.io.Serializable;

public class Pregunta implements Serializable{
	
	private static final long serialVersionUID = 111L;
	private String pregunta;
    private String respuestaCorrecta;
    private String respuestaIncA;
    private String respuestaIncB;
    private String respuestaIncC;
	
	public Pregunta(String pregunta, String respuestaCorrecta, String respuestaIncA, String respuestaIncB, String respuestaIncC) {
		this.pregunta=pregunta;
        this.respuestaCorrecta=respuestaCorrecta;
        this.respuestaIncA=respuestaIncA;
        this.respuestaIncB=respuestaIncB;
        this.respuestaIncC=respuestaIncC;
	}

	public boolean esCorrecta(String respuestaSeleccionada){
        return respuestaCorrecta.equals(respuestaSeleccionada);
    }
}