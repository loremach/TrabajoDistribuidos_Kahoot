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

	private static void atenderPeticion(Socket cliente) {
		try (DataInputStream inSocket = new DataInputStream(cliente.getInputStream());
				ObjectOutputStream outSocket = new ObjectOutputStream(cliente.getOutputStream());) {
			int opcion = inSocket.readInt();
			switch (opcion) {
			case 1:
				Persona p = new Persona(cliente.getInetAddress().getHostAddress(), cliente.getPort());
				if (!salas.containsValue(p)) {
					String id = "0";
					do {
						id = generarIdAleatorio();
					} while (salas.containsKey(id));
					salas.put(id, p);
					outSocket.writeBytes("Has creado la sala.\n");
					outSocket.flush();
					outSocket.writeBytes(id);
					outSocket.flush();
				} else {
					outSocket.writeBytes("Ya posees una sala.\n");
					outSocket.flush();
				}
				break;
			case 2:
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
