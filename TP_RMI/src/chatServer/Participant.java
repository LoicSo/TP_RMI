package chatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Participant extends UnicastRemoteObject implements IParticipant {

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
