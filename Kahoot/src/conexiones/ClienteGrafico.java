package conexiones;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JFrame;

import dominio.Persona;
import dominio.Pregunta;
import dominio.Respuesta;
import dominio.Sala;
import dominio.SocketCliente;
import Interfaces.VentanaPrincipal;
import utils.CountDown;
import utils.Empezar;

public class ClienteGrafico extends JFrame{
    public static void main(String [] args){
//        VentanaInicio pantalla=new VentanaInicio(); 
//        pantalla.mostrarInterfaz();
    	Cliente2 cliente = new Cliente2();
    	VentanaPrincipal ventana= new VentanaPrincipal(cliente);
        ventana.empezar();
    }


    
}
