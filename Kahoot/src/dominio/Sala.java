package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sala implements Serializable{
    private String idSala;
    private List<Pregunta> preguntas = new ArrayList<>();
    private static final long serialVersionUID = 111L;

    public Sala(String idSala){
        this.idSala = idSala;
    }

    public void anadirPregunta(Pregunta pregunta){
        preguntas.add(pregunta);
    }

    public List<Pregunta> getPreguntas(){
        return this.preguntas;
    }

    public String getIdSala(){
        return this.idSala;
    }
}
