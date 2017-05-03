/**
 * 
 */
package es.ubu.lsi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServer;
import es.ubu.lsi.server.ChatServerImpl;

/**
 * Inicializa un cliente, recibe la entradas de texto e invoca a los métodos del
 * servidor.
 * 
 * @author Andrés Miguel Terán
 * @author Francisco Saiz Güemes
 *
 */
public class ChatClientStarter {

	/**
	 * Bandera para mandar mensajes o no.
	 */
	boolean sendMessages = true;

	/**
	 * Inicializa y configura el cliente.
	 * 
	 * @param args
	 *            argumentos
	 */
	public ChatClientStarter(String[] args) {

		String nickName = null;
		String host = "localhost";
		ChatClientImpl client = null;
		ChatServer server = null;
		System.out.println("Argumentos: " + args.length);
		if (args.length > 0 && args.length < 3) {
			nickName = args[0];
			if (args.length == 2) {
				host = args[1];
			}
		} else {
			// Cerrar aplicación
			System.out.println("Debes introducir el nombre de usuario y si quieres el del host.");
			cerrarCliente(1);
		}

		try {
			client = new ChatClientImpl(nickName);
		} catch (RemoteException e) {
			// Si no se puede iniciar, cerramos.
			System.err.println("No se puede iniciar el cliente");
			cerrarCliente(1);
		}
		try {
			try {
				// Obtenemos la clase del servidor.
				System.out.println("Conectando con el servidor...");
				server = (ChatServer) Naming.lookup("rmi://" + host + "/ChatServerImpl");
			} catch (MalformedURLException e) {
				System.err.println("Direccion de host no válida.");
				throw e;
			} catch (RemoteException e) {
				System.err.println("Fallo remoto.");
				throw e;
			} catch (NotBoundException e) {
				System.err.println("No se encuentra la clase servidor.");
				throw e;
			}
		} catch (Exception e) {
			System.err.println("El servidor no está activo.");
			e.printStackTrace();
			cerrarCliente(1);
		}

		try {
			// Registramos el cliente y le damos un id.
			if (server.checkIn(client) == -1) {
				// Si el nombre ya lo tiene registrado, el servidor no nos
				// dejará entrar
				System.err.println("El nombre de usuario ya existe, introduce otro.");
				cerrarCliente(0);
			} else {
				mensajeBienvenida(nickName);
			}
		} catch (RemoteException e) {
			System.err.println("No se puede registrar el cliente, cerrando...");
			cerrarCliente(1);
		}

		Scanner scan = new Scanner(System.in);
		// Manejo de mensajes
		while (sendMessages) {
			System.out.print("> ");
			String msg = scan.nextLine();

			if (msg.toLowerCase().matches("^\\s*logout\\s*")) {
				// Logout
				try {
					server.logout(client);
				} catch (RemoteException e) {
					System.err.println("Error intentando desconectar cliente.");
				} finally {
					sendMessages = false;
				}
			} else if (msg.toLowerCase().matches("^\\s*shutdown\\s*")) {
				try {
					server.shutdown(client);
				} catch (RemoteException e) {
					System.out.println("Has apagado el servidor.");
				} finally {
					sendMessages = false;
				}
			} else if (msg.toLowerCase().matches("^\\s*ban\\s+\\S+\\s*")) {
				String userToBan = banManager(msg);
				try {
					server.ban(new ChatMessage(client.getId(), client.getNickName(), userToBan));
				} catch (RemoteException e) {
					System.err.println("Error al intentar banear usuario.");
				}
			} else if (msg.toLowerCase().matches("^\\s*unban\\s+\\S+\\s*")) {
				String userToUnban = banManager(msg);
				try {
					server.unban(new ChatMessage(client.getId(), client.getNickName(), userToUnban));
				} catch (RemoteException e) {
					System.err.println("Error al intentar desbanear usuario.");
				}
			} else {
				try {
					server.publish(new ChatMessage(client.getId(), client.getNickName(), msg));
				} catch (RemoteException e) {
					System.err.println("El servidor se ha apagado.");
					sendMessages = false;
				}
			}
		}

		// Cerrar scanner y salir del programa
		scan.close();
		cerrarCliente(0);
	}

	/**
	 * Recibe un comando de baneo y devuelve el usuario al que hay que
	 * aplicarselo.
	 * 
	 * @param message
	 * @return
	 */
	private String banManager(String message) {
		String[] command = message.split("\\s");
		String username = null;
		if (command.length >= 2) {
			int counter = 0;
			for (String word : command) {
				if (!word.equals("")) {
					counter++;
					if (counter == 2) {
						username = word;
						break;
					}
				}
			}
		}
		return username;
	}

	/**
	 * Mensaje de bienvenida para el usuario.
	 * 
	 * @param name
	 *            nombre de usuario
	 */
	public void mensajeBienvenida(String name) {
		System.out.println("\n\n            · BIENVENIDO ·\n");
		System.out.println("Te has logeado correctamente en el chat.\n");
		System.out.println("Tu nombre de usuario es: " + name);
		System.out.println("Los comandos disponibles son: \n");
		System.out.println("    - BAN ");
		System.out.println("    - UNBAN ");
		System.out.println("    - LOGOUT ");
		System.out.println("________________________________________\n");
	}

	/**
	 * Desconecta el cliente.
	 * 
	 * @param code
	 *            1 si es un error, 0 si desconexión normarl
	 */
	public void cerrarCliente(int code) {
		System.out.println("Desconectando...");
		System.exit(code);
	}

}
