package chatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Participant extends UnicastRemoteObject implements IParticipant {

	private static final long serialVersionUID = -7186187182104106848L;
	String name;
	
	public Participant(String name) throws RemoteException{
		this.name = name;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public void receive(String name, String msg) {
		System.out.println(name + " : " + msg);
	}

}
