package chatServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

	static int counter = 0;
	String name;
	List<IParticipant> room;
	Registry reg;
	
	public ChatRoom() throws RemoteException{
		name = "chatRoom" + counter++;
		room = new ArrayList<IParticipant>();
		reg = LocateRegistry.getRegistry(1099);
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public void connect(IParticipant p) {
		
		IParticipant stub = null;
		try {
			stub = (IParticipant) reg.lookup(p.name());
		} catch (RemoteException e) {
			System.err.println("ChatRoom exception" + e.toString());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("Participant does not exist");
			e.printStackTrace();
		}
		room.add(stub);
	}

	@Override
	public void leave(IParticipant p) {
		
		IParticipant stub = null;
		try {
			stub = (IParticipant) reg.lookup(p.name());
		} catch (RemoteException e) {
			System.err.println("ChatRoom exception" + e.toString());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("Participant does not exist");
			e.printStackTrace();
		}
		room.remove(stub);		
	}

	@Override
	public String[] who() {
		return room.toArray(new String[0]);
	}

	@Override
	public synchronized void send(IParticipant p, String msg) throws RemoteException {
		
		if(room.contains(p)) {
			Iterator<IParticipant> iter = room.iterator();
			while(iter.hasNext()) {
				IParticipant participant = iter.next();
				participant.receive(p.name(), msg);
			}
		}
		else {
			p.receive(name, "You are not connected to this chatRoom.");
		}
		
		
	}

}
