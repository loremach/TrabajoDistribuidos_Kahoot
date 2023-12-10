package conexiones;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dominio.Persona;

public class Servidor {

	private static HashMap<String, Persona> salas = new HashMap<>();

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(8000);) {
			ExecutorService pool = Executors.newCachedThreadPool();
			while (true) {
				try {
					Socket cliente = ss.accept();
					pool.execute(new Runnable() {
						public void run() {
							atenderPeticion(cliente);
						}
					});
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Atiende la petición de un cliente en función de la opción proporcionada.
	 * 
	 * @param cliente El socket del cliente que realiza la petición.
	 */
	private static void atenderPeticion(Socket cliente) {
		try (DataInputStream inSocket = new DataInputStream(cliente.getInputStream());
				ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());) {
			// Lee la opción proporcionada por el cliente
			int opcion = inSocket.readInt();
			switch (opcion) {
			case 1:
				// Si el cliente no está asociado a ninguna sala, crea una nueva y le asigna un ID
				Persona p = new Persona(cliente.getInetAddress().getHostAddress(), cliente.getPort());
				if (!salas.containsValue(p)) {
					String id = "0";
					do {
						id = generarIdAleatorio(); // Genera un ID aleatorio único
					} while (salas.containsKey(id));
					salas.put(id, p);
					outSocket.writeBytes("Has creado la sala.\n");
					outSocket.flush();
					outSocket.writeBytes(id);
					outSocket.flush();
				} else {
					// Si el cliente ya tiene una sala asociada, notifica al cliente
					outSocket.writeBytes("Ya posees una sala.\n");
					outSocket.flush();
				}
				break;
			case 2:
				// Envia al cliente la información de todas las salas disponibles
				outSocket.writeObject(salas);
				outSocket.flush();
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String generarIdAleatorio() {
		byte[] bytearray;
		String id;
		StringBuffer buff;
		int i = 6;

		bytearray = new byte[256];
		new Random().nextBytes(bytearray);

		id = new String(bytearray, Charset.forName("UTF-8"));

		buff = new StringBuffer();

		for (int m = 0; m < id.length(); m++) {
			char n = id.charAt(m);

			if (((n >= 'A' && n <= 'Z') || (n >= '0' && n <= '9')) && (i > 0)) {
				buff.append(n);
				i--;
			}
		}
		return buff.toString();
	}
}
