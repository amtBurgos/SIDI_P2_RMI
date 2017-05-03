package es.ubu.lsi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import es.ubu.lsi.client.ChatClient;
import es.ubu.lsi.common.ChatMessage;

/**
 * Chat server.
 * 
 * @author Raúl Marticorena
 * @author Joaquin P. Seco
 *
 */
public interface ChatServer extends Remote {

	/**
	 * Registers a new client.
	 * 
	 * @param client
	 *            client
	 * @return client id
	 * @throws RemoteException
	 *             remote error
	 */
	public abstract int checkIn(ChatClient client) throws RemoteException;

	/**
	 * Unregisters a new client.
	 * 
	 * @param client
	 *            current client
	 * @throws RemoteException
	 *             remote error
	 */
	public abstract void logout(ChatClient client) throws RemoteException;

	/**
	 * Sends a private message to a user.
	 * 
	 * @param tonickname
	 *            string
	 * @param msg
	 *            message
	 * @throws RemoteException
	 *             remote error
	 */
	public abstract void privatemsg(String tonickname, ChatMessage msg) throws RemoteException;

	/**
	 * Publishs a received message.
	 * 
	 * @param msg
	 *            message
	 * @throws RemoteException
	 *             remote error
	 */
	public abstract void publish(ChatMessage msg) throws RemoteException;

	/**
	 * Orders of shutdown server.
	 * 
	 * @param client
	 *            current client sending the message
	 * @throws RemoteException
	 *             remote error
	 */
	public abstract void shutdown(ChatClient client) throws RemoteException;

	/**
	 * Ban a user.
	 * 
	 * @param chatMessage
	 *            chatMessage with the name of the user to ban
	 */
	public abstract void ban(ChatMessage msg) throws RemoteException;

	/**
	 * Unban a user.
	 * 
	 * @param chatMessage
	 *            chatMessage with the name of the user to unban
	 */
	public abstract void unban(ChatMessage msg) throws RemoteException;
}