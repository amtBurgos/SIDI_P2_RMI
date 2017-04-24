/**
 * 
 */
package es.ubu.lsi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.ubu.lsi.common.ChatMessage;

/**
 * @author Andrés Miguel Terán
 * @author Francisco Saiz Güemes
 *
 */
public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

	/**
	 * Nombre de usuario.
	 */
	private String nickName;

	/**
	 * Id de usuario.
	 */
	private int id;

	/**
	 * Construye un cliente.
	 * 
	 * @throws RemoteException
	 */
	public ChatClientImpl(String nickName) throws RemoteException {
		super();
		this.nickName = nickName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.client.ChatClient#getId()
	 */
	public int getId() throws RemoteException {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.client.ChatClient#setId(int)
	 */
	public void setId(int id) throws RemoteException {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.client.ChatClient#receive(es.ubu.lsi.common.ChatMessage)
	 */
	public void receive(ChatMessage msg) throws RemoteException {
		System.out.println(msg.getNickname() + ": " + msg.getMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.ubu.lsi.client.ChatClient#getNickName()
	 */
	public String getNickName() throws RemoteException {
		return nickName;
	}

}
