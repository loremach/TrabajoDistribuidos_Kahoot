package conexiones;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import Interfaces.VentanaInicio;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;

public class ClienteGrafico extends JFrame{
    private static Sala sala = null;
    private List<Pregunta> preguntasSala = new ArrayList<>();
    private static int localPort=0;
    private static HashMap<String, Persona> salas;
    private static boolean listo = false;

    public static void main(String [] args){
//        VentanaInicio pantalla=new VentanaInicio(); 
//        pantalla.mostrarInterfaz();
        
        
    }
}
