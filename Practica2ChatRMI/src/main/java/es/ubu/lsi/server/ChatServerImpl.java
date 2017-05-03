package es.ubu.lsi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import es.ubu.lsi.client.ChatClient;
import es.ubu.lsi.common.ChatMessage;

/**
 * Realiza la carga dinámica de clases desde el servidor.
 * 
 * @author Andrés Miguel Terán
 * @author Francisco Saiz Güemes
 *
 */
public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Numero de clientes en el chat.
	 */
	private static int clientesTotales = 0;

	/**
	 * Nombre del servidor.
	 */
	private static String SERVER_NAME = "SERVER";

	/**
	 * Lista de clientes.
	 */
	private ArrayList<ChatClient> listaClientes;

	/**
	 * Mapa para llevar la cuenta de los baneos;
	 */
	private HashMap<String, HashSet<String>> listaBaneos;

	/**
	 * Contruye un servidor
	 * 
	 * @throws RemoteException
	 *             RemoteException
	 */
	public ChatServerImpl() throws RemoteException {
		super();
		listaClientes = new ArrayList<ChatClient>();
		listaBaneos = new HashMap<String, HashSet<String>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#checkIn(es.ubu.lsi.client.ChatClient)
	 */
	public int checkIn(ChatClient client) throws RemoteException {
		int id = -1;
		if (!checkClientExist(client.getNickName())) {
			id = clientesTotales++;
			client.setId(id);
			listaClientes.add(client);
			// Si el cliente se ha vuelto a conectar no cambiamos los baneos que
			// tenia configurados
			if (!listaBaneos.containsKey(client.getNickName().toLowerCase())) {
				listaBaneos.put(client.getNickName().toLowerCase(), new HashSet<String>());
			}
			publish(new ChatMessage(-1, SERVER_NAME, client.getNickName() + " se ha conectado."));
		}
		return id;
	}

	/**
	 * Comprueba si el nombre de usuario ya está escogido.
	 * 
	 * @param nickName
	 *            nombre de usuario a validar
	 * @return true/false si existe o no el nombre
	 */
	private boolean checkClientExist(String nickName) {
		boolean existe = false;
		for (ChatClient client : listaClientes) {
			try {
				if (client.getNickName().toLowerCase().equals(nickName.toLowerCase())) {
					existe = true;
					break;
				}
			} catch (RemoteException e) {
				System.err.println("No se puede acceder a la lista de usuarios activos.");
			}
		}
		return existe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#logout(es.ubu.lsi.client.ChatClient)
	 */
	public void logout(ChatClient client) throws RemoteException {
		listaClientes.remove(client);
		publish(new ChatMessage(-1, SERVER_NAME, client.getNickName() + " se ha desconectado"));
		System.out.println(client.getNickName() + " se ha desconectado");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#privatemsg(java.lang.String,
	 * es.ubu.lsi.common.ChatMessage)
	 */
	public void privatemsg(String tonickname, ChatMessage msg) throws RemoteException {
		ChatClient emisor = null;
		for (ChatClient client : listaClientes) {
			if (msg.getId() == client.getId()) {
				emisor = client;
				break;
			}
		}

		for (ChatClient receptor : listaClientes) {
			if (receptor.getNickName().toLowerCase().equals(tonickname)) {
				receptor.receive(new ChatMessage(msg.getId(), emisor.getNickName(), msg.getMessage() + " (private)"));
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#publish(es.ubu.lsi.common.ChatMessage)
	 */
	public void publish(ChatMessage msg) throws RemoteException {
		// Gestionar el mensaje
		for (ChatClient client : listaClientes) {
			// Si el id es distinto lo mandamos
			if (client.getId() != msg.getId()) {
				// Si el usuario no está baneado por alguno lo mandamos
				if (!checkIsBanned(client.getNickName(), msg.getNickname())) {
					client.receive(msg);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#shutdown(es.ubu.lsi.client.ChatClient)
	 */
	public void shutdown(ChatClient client) throws RemoteException {
		for (ChatClient cliente : listaClientes) {
			if (cliente.getId() != client.getId()) {
				cliente.receive(new ChatMessage(-1, SERVER_NAME, "El servidor se cerrará."));
			}
		}
		listaClientes = null;
		System.exit(0);
	}

	/**
	 * Banea un usuario.
	 * 
	 * @param msg
	 *            mensaje con los datos del usuario a banear
	 */
	public void ban(ChatMessage msg) {
		String userToBan = msg.getMessage().toLowerCase();
		String userWhoBan = msg.getNickname().toLowerCase();

		HashSet<String> baneados = listaBaneos.get(userWhoBan);
		// Comunicamos los baneos al servidor
		if (baneados.add(userToBan)) {
			System.out.println(msg.getNickname() + " ha baneado a " + msg.getMessage() + ".");
		} else {
			System.out.println(msg.getNickname() + " ya había baneado a " + msg.getMessage() + ".");
		}
		listaBaneos.put(userWhoBan, baneados);
	}

	/**
	 * Desbanea un usuario.
	 * 
	 * @param msg
	 *            mensaje con los datos del usuario a desbanear
	 */
	public void unban(ChatMessage msg) {
		String userToUnban = msg.getMessage().toLowerCase();
		String userWhoUnban = msg.getNickname().toLowerCase();

		HashSet<String> baneados = listaBaneos.get(userWhoUnban);
		if (baneados.remove(userToUnban)) {
			System.out.println(msg.getNickname() + " ha desbaneado a " + msg.getMessage() + ".");
		} else {
			System.out.println(
					msg.getNickname() + " no puede desbanear a " + msg.getMessage() + " porque no está baneado.");
		}
		listaBaneos.put(userWhoUnban, baneados);
	}

	/**
	 * Comprueba si un usuario tiene baneado a otro.
	 * 
	 * @param user1
	 *            usuario que igual a baneado al usuario2
	 * @param user2
	 *            usuario que igual tiene baneado el usuario1
	 * @return
	 */
	private boolean checkIsBanned(String user1, String user2) {
		return listaBaneos.get(user1.toLowerCase()).contains(user2);
	}
}
