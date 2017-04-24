/**
 * 
 */
package es.ubu.lsi.server;

import java.rmi.RemoteException;

import es.ubu.lsi.client.ChatClient;
import es.ubu.lsi.common.ChatMessage;

/**
 * Realiza la carga dinámica de clases desde el servidor.
 * 
 * @author Andrés Miguel Terán
 * @author Francisco Saiz Güemes
 *
 */
public class ChatServerImpl implements ChatServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#checkIn(es.ubu.lsi.client.ChatClient)
	 */
	public int checkIn(ChatClient client) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#logout(es.ubu.lsi.client.ChatClient)
	 */
	public void logout(ChatClient client) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#privatemsg(java.lang.String,
	 * es.ubu.lsi.common.ChatMessage)
	 */
	public void privatemsg(String tonickname, ChatMessage msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#publish(es.ubu.lsi.common.ChatMessage)
	 */
	public void publish(ChatMessage msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.server.ChatServer#shutdown(es.ubu.lsi.client.ChatClient)
	 */
	public void shutdown(ChatClient client) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
