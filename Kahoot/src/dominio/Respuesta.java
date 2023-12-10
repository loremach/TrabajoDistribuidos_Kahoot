package dominio;

import java.io.Serializable;
import java.util.Calendar;

public class Respuesta implements Serializable{
    private String respuesta;
    private Calendar tiempo;
    private static final long serialVersionUID = 111L;

    public Respuesta(String respuesta){
        this.respuesta=respuesta;
        this.tiempo=Calendar.getInstance();
    }

    public Respuesta(){
        this.respuesta="";
        this.tiempo=Calendar.getInstance();
    }

    public String getRespuesta(){
        return this.respuesta;
    }

    public Calendar getTiempoRespuesta(){
        return this.tiempo;
    }
}
