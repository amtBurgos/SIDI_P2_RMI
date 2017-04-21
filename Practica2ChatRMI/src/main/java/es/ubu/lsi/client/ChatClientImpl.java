/**
 * 
 */
package es.ubu.lsi.client;

import java.rmi.RemoteException;

import es.ubu.lsi.common.ChatMessage;

/**
 * @author Andres
 *
 */
public class ChatClientImpl implements ChatClient {

	/* (non-Javadoc)
	 * @see es.ubu.lsi.client.ChatClient#getId()
	 */
	public int getId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see es.ubu.lsi.client.ChatClient#setId(int)
	 */
	public void setId(int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.ubu.lsi.client.ChatClient#receive(es.ubu.lsi.common.ChatMessage)
	 */
	public void receive(ChatMessage msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.ubu.lsi.client.ChatClient#getNickName()
	 */
	public String getNickName() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
