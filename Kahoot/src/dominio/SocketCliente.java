package dominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketCliente {
    private Socket socket;
    private ObjectInputStream inSocket;
    private ObjectOutputStream outSocket;

    public SocketCliente(Socket socket,ObjectInputStream inSocket, ObjectOutputStream outSocket){
        this.socket = socket;
        this.inSocket = inSocket;
        this.outSocket = outSocket;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public ObjectOutputStream getObjectOutputStream(){
        return this.outSocket;
    }

    public ObjectInputStream getObjectInputStream(){
        return this.inSocket;
    }
}
