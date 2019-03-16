package chatServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

	private static final long serialVersionUID = -3505617332668629540L;
	String name;
	List<IParticipant> room;
	Registry reg;

	public ChatRoom(String name) throws RemoteException {
		this.name = name;
		room = new ArrayList<IParticipant>();
		reg = LocateRegistry.getRegistry(1099);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void connect(IParticipant p) throws RemoteException {
		if (p != null) {
			room.add(p);
			send(null, p.name() + " enter the room");
		}
	}

	@Override
	public void leave(IParticipant p) throws RemoteException {
		if (room.contains(p)) {
			send(null, p.name() + " leave the room");
			room.remove(p);
		}
	}

	@Override
	public String[] who() throws RemoteException {

		String[] listP = new String[room.size()];
		int i = 0;

		Iterator<IParticipant> iter = room.iterator();
		while (iter.hasNext()) {
			IParticipant participant = iter.next();
			listP[i++] = participant.name();
		}

		return listP;
	}

	@Override
	public synchronized void send(IParticipant p, String msg) throws RemoteException {

		if (p != null) {
			if (room.contains(p)) {
				Iterator<IParticipant> iter = room.iterator();
				while (iter.hasNext()) {
					IParticipant participant = iter.next();
					if (!participant.equals(p))
						participant.receive(p.name(), msg);
				}
			} else {
				p.receive(name, "You are not connected to this chatRoom.");
			}
		} else {
			Iterator<IParticipant> iter = room.iterator();
			while (iter.hasNext()) {
				IParticipant participant = iter.next();
				participant.receive(name, msg);
			}
		}

	}

}
