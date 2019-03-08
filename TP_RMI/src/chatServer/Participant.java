package chatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Participant extends UnicastRemoteObject implements IParticipant {

	static int counter = 0;
	String name;
	
	public Participant() throws RemoteException{
		name = "participant" + counter++;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public void receive(String name, String msg) {
		// TODO Auto-generated method stub
		
	}

}
