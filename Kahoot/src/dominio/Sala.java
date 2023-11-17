package dominio;

import java.util.ArrayList;
import java.util.List;

public class Sala {
    private String idSala;
    private List<Pregunta> preguntas = new ArrayList<>();

    public Sala(String idSala){
        this.idSala = idSala;
    }

    public void anadirPregunta(Pregunta pregunta){
        preguntas.add(pregunta);
    }

    public List<Pregunta> getPreguntas(){
        return this.getPreguntas();
    }

    public String getIdSala(){
        return this.idSala;
    }
}
