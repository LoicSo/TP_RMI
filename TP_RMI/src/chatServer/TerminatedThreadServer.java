package chatServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

public class TerminatedThreadServer extends Thread {
	
	List<IChatRoom> rooms;
	Registry reg;
	
	public TerminatedThreadServer(List<IChatRoom> room, Registry reg) {
		rooms = room;
		this.reg = reg;
	}
	
	public void run() {
		for (Iterator<IChatRoom> iterator = rooms.iterator(); iterator.hasNext();) {
			IChatRoom iChatRoom = (IChatRoom) iterator.next();
			
			try {
				iChatRoom.forcedDisconnection();
				reg.unbind(iChatRoom.name());
			} catch (RemoteException | NotBoundException e) {
				System.err.println("Server exception: " + e.toString());
				e.printStackTrace();
				System.exit(-1);
			}			
		}
	}

}
