package chatServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	
	IChatRoom room;
	
	public Server() {
		try {
			room = new ChatRoom();
		} catch (RemoteException e) {
			System.err.println("ChatRoom creation failed");
			e.printStackTrace();
		}
	}
	
	void run() {
		
		try {	
			
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.bind(room.name(), room);
			
			System.err.println("Server ready");
			
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server serv = new Server();
		serv.run();
	}

}
