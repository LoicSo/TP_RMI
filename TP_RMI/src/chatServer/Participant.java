package chatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Participant extends UnicastRemoteObject implements IParticipant {

	private static final long serialVersionUID = -7186187182104106848L;
	String name;
	Client c;
	
	public Participant(String name, Client c) throws RemoteException{
		this.name = name;
		this.c = c;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public void receive(String name, String msg) {
		System.out.println(name + " : " + msg);
	}

	@Override
	public void forcedDisconnection() {
		c.forcedDisconnection();		
	}

}
