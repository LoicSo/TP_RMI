package chatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

	static int counter = 0;
	String name;
	List<IParticipant> room;
	
	public ChatRoom() throws RemoteException{
		name = "chatRoom" + counter++;
		room = new ArrayList<IParticipant>();
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public void connect(IParticipant p) {
		room.add(p);
		
	}

	@Override
	public void leave(IParticipant p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] who() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(IParticipant p, String msg) {
		// TODO Auto-generated method stub
		
	}

}
