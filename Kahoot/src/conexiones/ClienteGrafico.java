package conexiones;

import javax.swing.JFrame;
import Interfaces.VentanaPrincipal;
import conexiones.clasesClienteGrafico.Cliente2;


public class ClienteGrafico extends JFrame{
    public static void main(String [] args){
    	Cliente2 cliente = new Cliente2();
    	VentanaPrincipal ventana= new VentanaPrincipal(cliente);
        ventana.empezar();
    }
}
