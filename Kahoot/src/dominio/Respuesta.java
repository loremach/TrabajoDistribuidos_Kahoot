package dominio;

import java.security.Timestamp;
import java.util.Calendar;

public class Respuesta {
    private String respuesta;
    private Calendar tiempo;

    public Respuesta(String respuesta){
        this.respuesta=respuesta;
        this.tiempo=Calendar.getInstance();
    }

    public String getRespuesta(){
        return this.respuesta;
    }

    public Calendar getTiempoRespuesta(){
        return this.tiempo;
    }
}
