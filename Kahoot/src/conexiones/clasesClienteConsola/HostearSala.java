package conexiones.clasesClienteConsola;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import dominio.Persona;
import dominio.Sala;
import dominio.SocketCliente;

public class HostearSala extends Thread{
    private int localPort;
    private HashMap<Persona, SocketCliente> clientesConectados;
    private Sala sala;
    private HashMap<Persona, Integer> tablaPuntuaciones;
    private Empezar empezar;

    public HostearSala(int localPort, HashMap<Persona, SocketCliente> clientesConectados, Sala sala, HashMap<Persona, Integer> tablaPuntuaciones, Empezar empezar){
        this.localPort = localPort;
        this.clientesConectados = clientesConectados;
        this.sala = sala;
        this.tablaPuntuaciones = tablaPuntuaciones;
        this.empezar = empezar;
    }

    public void run(){
        try(ServerSocket ss = new ServerSocket(localPort)){
            int i = 20;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    i--;
                    ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());
                    ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());

                    SocketCliente socketCliente = new SocketCliente(cliente, inSocket, outSocket);
                    if(!empezar.getListo()){
                        outSocket.writeBoolean(true);
                        outSocket.flush();
                        Persona p = (Persona) inSocket.readObject();
                        System.out.println(p.getAlias() + " se ha conectado");
                        tablaPuntuaciones.put(p, 0);
                        clientesConectados.put(p, socketCliente);
                        outSocket.writeInt(sala.getPreguntas().size());
                        outSocket.flush();
                    }else{
                        outSocket.writeBoolean(false);
                        outSocket.flush();
                    }                  
                }catch(IOException ex){
                    ex.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
