package conexiones;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cliente {
    private static String idSala;

    public static void main(String[] args) {
        int opcion = 0;
        try(Scanner teclado = new Scanner(System.in);
            Socket cliente = new Socket("localhost", 8000);
            ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
            DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
                do{
                    System.out.println("Elija una opción:");
                    System.out.println("1. Crear una sala.");
                    System.out.println("2. Unirse a una sala.");
                    System.out.println("3. Salir.");
                    opcion = teclado.nextInt();
                }while(opcion!=1 && opcion!=2 && opcion!=3);

                switch (opcion) {
                    case 1:
                        idSala = inSocket.readLine();
                        hostearSala();
                    break;
                
                    case 2:
                        jugarEnSala();
                    break;

                    case 3:
                    break;
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
    }

    private static void hostearSala(){
        try(ServerSocket ss = new ServerSocket()){
            ExecutorService pool = Executors.newFixedThreadPool(30);
            int i = 30;
            while (i>0) {
                try{
                    Socket cliente = ss.accept();
                    //mandar las preguntas a los jugadores y recoger sus respuestas 
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void jugarEnSala(){

    }
}
